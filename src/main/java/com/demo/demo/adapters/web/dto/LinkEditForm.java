package com.demo.demo.adapters.web.dto;

import com.demo.demo.domain.validation.ValidDescription;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

public class LinkEditForm {
	@NotBlank(message = "Por favor ingresa la URL de la imagen antes de continuar.")
	@URL(message = "La URL de la imagen no parece ser valida. Revisa e intentalo nuevamente.")
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
