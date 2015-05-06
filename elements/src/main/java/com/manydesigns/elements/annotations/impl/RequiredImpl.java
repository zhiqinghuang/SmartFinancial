package com.manydesigns.elements.annotations.impl;

import com.manydesigns.elements.annotations.Required;

import java.lang.annotation.Annotation;

public class RequiredImpl implements Required {

	private boolean value;

	public RequiredImpl(boolean value) {
		this.value = value;
	}

	public boolean value() {
		return value;
	}

	public Class<? extends Annotation> annotationType() {
		return Required.class;
	}
}