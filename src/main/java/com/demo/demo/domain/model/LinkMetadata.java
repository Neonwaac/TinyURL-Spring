package com.demo.demo.domain.model;

import java.time.Instant;

public class LinkMetadata {
	private final String code;
	private final String image;
	private final String description;
	private final Instant createdAt;

	public LinkMetadata(String code, String image, String description, Instant createdAt) {
		this.code = code;
		this.image = image;
		this.description = description;
		this.createdAt = createdAt;
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

	public Instant getCreatedAt() {
		return createdAt;
	}
}
