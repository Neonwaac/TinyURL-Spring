package com.demo.demo.domain.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = DescriptionValidator.class)
@Target({FIELD, PARAMETER})
@Retention(RUNTIME)
public @interface ValidDescription {
	String message() default "La descripcion debe tener al menos 5 palabras y un maximo de 500 caracteres.";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
