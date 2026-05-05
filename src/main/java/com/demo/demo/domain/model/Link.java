package com.demo.demo.domain.model;

import java.time.Instant;

public class Link {
	private final Long id;
	private final String code;
	private final String originalUrl;
	private final long clicks;
	private final boolean active;
	private final Instant createdAt;

	public Link(Long id, String code, String originalUrl, long clicks, boolean active, Instant createdAt) {
		this.id = id;
		this.code = code;
		this.originalUrl = originalUrl;
		this.clicks = clicks;
		this.active = active;
		this.createdAt = createdAt;
	}

	public Long getId() {
		return id;
	}

	public String getCode() {
		return code;
	}

	public String getOriginalUrl() {
		return originalUrl;
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
}
