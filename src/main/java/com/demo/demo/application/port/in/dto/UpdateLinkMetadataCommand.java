package com.demo.demo.application.port.in.dto;

public class UpdateLinkMetadataCommand {
	private final String code;
	private final String image;
	private final String description;

	public UpdateLinkMetadataCommand(String code, String image, String description) {
		this.code = code;
		this.image = image;
		this.description = description;
	}

	public String getCode() {
		return code;
	}

	public String getImage() {
		return image;
	}

	public String getDescription() {
		return description;
	}
}
