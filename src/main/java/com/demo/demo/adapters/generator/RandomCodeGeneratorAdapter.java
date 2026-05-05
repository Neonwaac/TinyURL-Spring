package com.demo.demo.adapters.generator;

import com.demo.demo.application.port.out.CodeGeneratorPort;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class RandomCodeGeneratorAdapter implements CodeGeneratorPort {
	private static final String ALPHABET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final int DEFAULT_LENGTH = 7;

	private final SecureRandom random = new SecureRandom();

	@Override
	public String generate() {
		StringBuilder builder = new StringBuilder(DEFAULT_LENGTH);
		for (int i = 0; i < DEFAULT_LENGTH; i++) {
			int index = random.nextInt(ALPHABET.length());
			builder.append(ALPHABET.charAt(index));
		}
		return builder.toString();
	}
}
