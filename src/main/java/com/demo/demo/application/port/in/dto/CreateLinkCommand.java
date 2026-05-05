package com.demo.demo.application.port.in.dto;

public class CreateLinkCommand {
	private final String originalUrl;
	private final String image;
	private final String description;

	public CreateLinkCommand(String originalUrl, String image, String description) {
		this.originalUrl = originalUrl;
		this.image = image;
		this.description = description;
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
