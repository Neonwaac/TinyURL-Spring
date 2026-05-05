package com.demo.demo.application.port.in.dto;

import java.time.Instant;

public class LinkListItem {
	private final String code;
	private final String originalUrl;
	private final String description;
	private final String image;
	private final long clicks;
	private final boolean active;
	private final Instant createdAt;
	private final boolean cached;

	public LinkListItem(String code,
					   String originalUrl,
					   String description,
					   String image,
					   long clicks,
					   boolean active,
					   Instant createdAt,
					   boolean cached) {
		this.code = code;
		this.originalUrl = originalUrl;
		this.description = description;
		this.image = image;
		this.clicks = clicks;
		this.active = active;
		this.createdAt = createdAt;
		this.cached = cached;
	}

	public String getCode() {
		return code;
	}

	public String getOriginalUrl() {
		return originalUrl;
	}

	public String getDescription() {
		return description;
	}

	public String getImage() {
		return image;
	}

	public long getClicks() {
		return clicks;
	}

	public boolean isActive() {
		return active;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public boolean isCached() {
		return cached;
	}
}
