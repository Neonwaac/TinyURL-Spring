package com.demo.demo.application.port.in.dto;

public class LinkCreatedView {
	private final String code;
	private final String originalUrl;

	public LinkCreatedView(String code, String originalUrl) {
		this.code = code;
		this.originalUrl = originalUrl;
	}

	public String getCode() {
		return code;
	}

	public String getOriginalUrl() {
		return originalUrl;
	}
}
