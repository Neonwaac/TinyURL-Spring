package com.demo.demo.domain.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DescriptionValidator implements ConstraintValidator<ValidDescription, String> {
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value == null) {
			return false;
		}
		String trimmed = value.trim();
		if (trimmed.length() < 1 || trimmed.length() > 500) {
			return false;
		}
		String[] words = trimmed.split("\\s+");
		return words.length >= 5;
	}
}
