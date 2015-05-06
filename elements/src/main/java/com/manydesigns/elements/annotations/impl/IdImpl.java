package com.manydesigns.elements.annotations.impl;

import com.manydesigns.elements.annotations.Id;

import java.lang.annotation.Annotation;

public class IdImpl implements Id {

	private final String value;

	public IdImpl(String value) {
		this.value = value;
	}

	public String value() {
		return value;
	}

	public Class<? extends Annotation> annotationType() {
		return Id.class;
	}
}