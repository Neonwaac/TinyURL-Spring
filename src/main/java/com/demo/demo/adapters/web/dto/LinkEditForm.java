package com.demo.demo.adapters.web.dto;

import com.demo.demo.domain.validation.ValidDescription;
import jakarta.validation.constraints.NotBlank;

public class LinkEditForm {
	@NotBlank(message = "Por favor ingresa la URL de la imagen antes de continuar.")
	private String image;

	@NotBlank(message = "Por favor escribe una descripcion antes de continuar.")
	@ValidDescription
	private String description;

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
