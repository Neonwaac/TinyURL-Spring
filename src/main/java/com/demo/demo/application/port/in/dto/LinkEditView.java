package com.demo.demo.application.port.in.dto;

public class LinkEditView {
	private final String code;
	private final String originalUrl;
	private final String image;
	private final String description;

	public LinkEditView(String code, String originalUrl, String image, String description) {
		this.code = code;
		this.originalUrl = originalUrl;
		this.image = image;
		this.description = description;
	}

	public String getCode() {
		return code;
	}

	public String getOriginalUrl() {
		return originalUrl;
	}

	public String getImage() {
		return image;
	}

	public String getDescription() {
		return description;
	}
}
