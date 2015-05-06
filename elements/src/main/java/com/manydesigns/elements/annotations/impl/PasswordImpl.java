package com.manydesigns.elements.annotations.impl;

import com.manydesigns.elements.annotations.Password;

import java.lang.annotation.Annotation;

public class PasswordImpl implements Password {

	private boolean confirmationRequired;

	public PasswordImpl() {
		this(false);
	}

	public PasswordImpl(boolean confirmationRequired) {
		this.confirmationRequired = confirmationRequired;
	}

	public boolean confirmationRequired() {
		return confirmationRequired;
	}

	public Class<? extends Annotation> annotationType() {
		return Password.class;
	}
}