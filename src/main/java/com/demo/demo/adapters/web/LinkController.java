package com.demo.demo.adapters.web;

import com.demo.demo.adapters.web.dto.LinkEditForm;
import com.demo.demo.adapters.web.dto.LinkForm;
import com.demo.demo.application.port.in.CreateLinkUseCase;
import com.demo.demo.application.port.in.DeleteLinkUseCase;
import com.demo.demo.application.port.in.GetLinkForEditUseCase;
import com.demo.demo.application.port.in.ListLinksUseCase;
import com.demo.demo.application.port.in.RedirectLinkUseCase;
import com.demo.demo.application.port.in.UpdateLinkMetadataUseCase;
import com.demo.demo.application.port.in.dto.CreateLinkCommand;
import com.demo.demo.application.port.in.dto.LinkCreatedView;
import com.demo.demo.application.port.in.dto.LinkEditView;
import com.demo.demo.application.port.in.dto.UpdateLinkMetadataCommand;
import com.demo.demo.domain.exception.DuplicateLinkException;
import com.demo.demo.domain.exception.InactiveLinkException;
import com.demo.demo.domain.exception.LinkNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping
public class LinkController {
	private final CreateLinkUseCase createLinkUseCase;
	private final ListLinksUseCase listLinksUseCase;
	private final RedirectLinkUseCase redirectLinkUseCase;
	private final GetLinkForEditUseCase getLinkForEditUseCase;
	private final UpdateLinkMetadataUseCase updateLinkMetadataUseCase;
	private final DeleteLinkUseCase deleteLinkUseCase;

	public LinkController(CreateLinkUseCase createLinkUseCase,
					  ListLinksUseCase listLinksUseCase,
					  RedirectLinkUseCase redirectLinkUseCase,
					  GetLinkForEditUseCase getLinkForEditUseCase,
					  UpdateLinkMetadataUseCase updateLinkMetadataUseCase,
					  DeleteLinkUseCase deleteLinkUseCase) {
		this.createLinkUseCase = createLinkUseCase;
		this.listLinksUseCase = listLinksUseCase;
		this.redirectLinkUseCase = redirectLinkUseCase;
		this.getLinkForEditUseCase = getLinkForEditUseCase;
		this.updateLinkMetadataUseCase = updateLinkMetadataUseCase;
		this.deleteLinkUseCase = deleteLinkUseCase;
	}

	@GetMapping("/")
	public String index(Model model) {
		if (!model.containsAttribute("linkForm")) {
			model.addAttribute("linkForm", new LinkForm());
		}
		return "index";
	}

	@GetMapping("/links")
	public String links(Model model, HttpServletRequest request) {
		model.addAttribute("links", listLinksUseCase.listAll());
		model.addAttribute("baseUrl", buildBaseUrl(request));
		return "links";
	}

	@PostMapping("/")
	public String create(
			@Valid @ModelAttribute("linkForm") LinkForm form,
			BindingResult bindingResult,
			RedirectAttributes redirectAttributes,
			HttpServletRequest request) {
		if (bindingResult.hasErrors()) {
			return "index";
		}
		try {
			LinkCreatedView created = createLinkUseCase.create(
					new CreateLinkCommand(form.getOriginalUrl(), form.getImage(), form.getDescription()));
			String shortUrl = buildBaseUrl(request) + "/r/" + created.getCode();
			LinkForm responseForm = new LinkForm();
			responseForm.setOriginalUrl(shortUrl);
			redirectAttributes.addFlashAttribute("linkForm", responseForm);
			redirectAttributes.addFlashAttribute("shortUrl", shortUrl);
			return "redirect:/";
		} catch (DuplicateLinkException ex) {
			bindingResult.rejectValue("originalUrl", "duplicate", ex.getMessage());
			return "index";
		}
	}

	@GetMapping("/r/{code}")
	public String redirect(@PathVariable String code) {
		String target = redirectLinkUseCase.resolveUrl(code);
		return "redirect:" + target;
	}

	@GetMapping("/links/{code}/edit")
	public String edit(@PathVariable String code, Model model) {
		LinkEditView view = getLinkForEditUseCase.getByCode(code);
		if (!model.containsAttribute("linkEditForm")) {
			LinkEditForm form = new LinkEditForm();
			form.setImage(view.getImage());
			form.setDescription(view.getDescription());
			model.addAttribute("linkEditForm", form);
		}
		model.addAttribute("code", view.getCode());
		model.addAttribute("originalUrl", view.getOriginalUrl());
		return "edit";
	}

	@PostMapping("/links/{code}/edit")
	public String update(
			@PathVariable String code,
			@Valid @ModelAttribute("linkEditForm") LinkEditForm form,
			BindingResult bindingResult,
			Model model) {
		if (bindingResult.hasErrors()) {
			LinkEditView view = getLinkForEditUseCase.getByCode(code);
			model.addAttribute("code", view.getCode());
			model.addAttribute("originalUrl", view.getOriginalUrl());
			return "edit";
		}
		updateLinkMetadataUseCase.update(new UpdateLinkMetadataCommand(code, form.getImage(), form.getDescription()));
		return "redirect:/links";
	}

	@PostMapping("/delete/{code}")
	public String delete(@PathVariable String code) {
		deleteLinkUseCase.deleteByCode(code);
		return "redirect:/links";
	}

	@ExceptionHandler({LinkNotFoundException.class, InactiveLinkException.class})
	public String handleNotFound(RuntimeException ex, Model model) {
		model.addAttribute("message", ex.getMessage());
		return "error";
	}

	private String buildBaseUrl(HttpServletRequest request) {
		String scheme = request.getScheme();
		String host = request.getServerName();
		int port = request.getServerPort();
		boolean isDefaultPort = ("http".equals(scheme) && port == 80)
				|| ("https".equals(scheme) && port == 443);
		return isDefaultPort ? scheme + "://" + host : scheme + "://" + host + ":" + port;
	}
}
