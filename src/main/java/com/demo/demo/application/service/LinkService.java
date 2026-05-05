package com.demo.demo.application.service;

import com.demo.demo.application.port.in.CreateLinkUseCase;
import com.demo.demo.application.port.in.DeleteLinkUseCase;
import com.demo.demo.application.port.in.GetLinkForEditUseCase;
import com.demo.demo.application.port.in.ListLinksUseCase;
import com.demo.demo.application.port.in.RedirectLinkUseCase;
import com.demo.demo.application.port.in.UpdateLinkMetadataUseCase;
import com.demo.demo.application.port.in.dto.CreateLinkCommand;
import com.demo.demo.application.port.in.dto.LinkCreatedView;
import com.demo.demo.application.port.in.dto.LinkEditView;
import com.demo.demo.application.port.in.dto.LinkListItem;
import com.demo.demo.application.port.in.dto.UpdateLinkMetadataCommand;
import com.demo.demo.application.port.out.CodeGeneratorPort;
import com.demo.demo.application.port.out.LinkCachePort;
import com.demo.demo.application.port.out.LinkMetadataRepositoryPort;
import com.demo.demo.application.port.out.LinkRepositoryPort;
import com.demo.demo.domain.exception.DuplicateLinkException;
import com.demo.demo.domain.exception.InactiveLinkException;
import com.demo.demo.domain.exception.LinkNotFoundException;
import com.demo.demo.domain.model.Link;
import com.demo.demo.domain.model.LinkMetadata;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class LinkService implements CreateLinkUseCase, ListLinksUseCase, RedirectLinkUseCase,
		DeleteLinkUseCase, GetLinkForEditUseCase, UpdateLinkMetadataUseCase {
	private static final int MAX_CODE_ATTEMPTS = 5;
	private static final int CACHE_URL_MIN_LENGTH = 50;

	private final LinkRepositoryPort linkRepository;
	private final LinkCachePort cachePort;
	private final LinkMetadataRepositoryPort metadataRepository;
	private final CodeGeneratorPort codeGenerator;
	private final LinkAsyncTasks asyncTasks;

	public LinkService(LinkRepositoryPort linkRepository,
					  LinkCachePort cachePort,
					  LinkMetadataRepositoryPort metadataRepository,
					  CodeGeneratorPort codeGenerator,
					  LinkAsyncTasks asyncTasks) {
		this.linkRepository = linkRepository;
		this.cachePort = cachePort;
		this.metadataRepository = metadataRepository;
		this.codeGenerator = codeGenerator;
		this.asyncTasks = asyncTasks;
	}

	@Override
	@Transactional
	public LinkCreatedView create(CreateLinkCommand command) {
		if (linkRepository.existsByUrlOriginal(command.getOriginalUrl())) {
			throw new DuplicateLinkException("Ese enlace ya fue registrado. Puedes usar el existente o probar con otra URL.");
		}

		String code = generateUniqueCode();
		Instant now = Instant.now();
		Link saved = linkRepository.save(new Link(null, code, command.getOriginalUrl(), 0L, true, now));

		LinkMetadata metadata = new LinkMetadata(saved.getCode(), command.getImage(), command.getDescription(), now);
		asyncTasks.saveMetadata(metadata);

		if (command.getOriginalUrl().length() >= CACHE_URL_MIN_LENGTH) {
			asyncTasks.cacheLongUrl(saved.getCode(), command.getOriginalUrl());
		}

		return new LinkCreatedView(saved.getCode(), saved.getOriginalUrl());
	}

	@Override
	@Transactional(readOnly = true)
	public List<LinkListItem> listAll() {
		List<Link> links = linkRepository.findAll();
		List<LinkListItem> result = new ArrayList<>(links.size());
		for (Link link : links) {
			boolean cached = cachePort.isCached(link.getCode());
			LinkMetadata metadata = metadataRepository.findByCode(link.getCode()).orElse(null);
			String description = metadata != null ? metadata.getDescription() : "";
			String image = metadata != null ? metadata.getImage() : "";
			result.add(new LinkListItem(
					link.getCode(),
					link.getOriginalUrl(),
					description,
					image,
					link.getClicks(),
					link.isActive(),
					link.getCreatedAt(),
					cached));
		}
		return result;
	}

	@Override
	@Transactional(readOnly = true)
	public String resolveUrl(String code) {
		Link link = linkRepository.findByCode(code)
				.orElseThrow(() -> new LinkNotFoundException("No encontramos el enlace solicitado. Verifica el codigo e intenta nuevamente."));
		if (!link.isActive()) {
			throw new InactiveLinkException("Este enlace esta inactivo por el momento.");
		}
		String target = cachePort.get(code).orElse(link.getOriginalUrl());
		asyncTasks.incrementClicks(code);
		return target;
	}

	@Override
	@Transactional(readOnly = true)
	public LinkEditView getByCode(String code) {
		Link link = linkRepository.findByCode(code)
				.orElseThrow(() -> new LinkNotFoundException("No encontramos el enlace solicitado. Verifica el codigo e intenta nuevamente."));
		LinkMetadata metadata = metadataRepository.findByCode(code).orElse(null);
		String image = metadata != null ? metadata.getImage() : "";
		String description = metadata != null ? metadata.getDescription() : "";
		return new LinkEditView(link.getCode(), link.getOriginalUrl(), image, description);
	}

	@Override
	@Transactional
	public void update(UpdateLinkMetadataCommand command) {
		Link link = linkRepository.findByCode(command.getCode())
				.orElseThrow(() -> new LinkNotFoundException("No encontramos el enlace solicitado. Verifica el codigo e intenta nuevamente."));
		LinkMetadata current = metadataRepository.findByCode(command.getCode()).orElse(null);
		Instant createdAt = current != null ? current.getCreatedAt() : link.getCreatedAt();
		metadataRepository.save(new LinkMetadata(command.getCode(), command.getImage(), command.getDescription(), createdAt));
	}

	@Override
	@Transactional
	public void deleteByCode(String code) {
		Link link = linkRepository.findByCode(code)
				.orElseThrow(() -> new LinkNotFoundException("No encontramos el enlace solicitado. Verifica el codigo e intenta nuevamente."));
		linkRepository.deleteByCode(link.getCode());
		metadataRepository.deleteByCode(link.getCode());
		cachePort.evict(link.getCode());
	}

	private String generateUniqueCode() {
		for (int i = 0; i < MAX_CODE_ATTEMPTS; i++) {
			String candidate = codeGenerator.generate();
			if (!linkRepository.existsByCode(candidate)) {
				return candidate;
			}
		}
		throw new IllegalStateException("No fue posible generar un codigo unico.");
	}
}
