package com.demo.demo.domain.exception;

public class DuplicateLinkException extends RuntimeException {
	public DuplicateLinkException(String message) {
		super(message);
	}
}
