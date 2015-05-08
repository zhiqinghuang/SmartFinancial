package com.manydesigns.elements.annotations.impl;

import com.manydesigns.elements.annotations.RegExp;

import java.lang.annotation.Annotation;

public class RegExpImpl implements RegExp {

	private final String value;
	private final String errorMessage;

	public RegExpImpl(String value, String errorMessage) {
		this.value = value;
		this.errorMessage = errorMessage;
	}

	public String value() {
		return value;
	}

	public String errorMessage() {
		return errorMessage;
	}

	public Class<? extends Annotation> annotationType() {
		return RegExp.class;
	}
}