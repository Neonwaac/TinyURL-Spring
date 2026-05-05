package com.demo.demo.domain.exception;

public class LinkNotFoundException extends RuntimeException {
	public LinkNotFoundException(String message) {
		super(message);
	}
}
