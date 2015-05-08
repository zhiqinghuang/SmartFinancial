package com.manydesigns.elements.annotations.impl;

import com.manydesigns.elements.annotations.FieldSet;

import java.lang.annotation.Annotation;

public class FieldSetImpl implements FieldSet {

	private final String value;

	public FieldSetImpl(String value) {
		this.value = value;
	}

	public String value() {
		return value;
	}

	public Class<? extends Annotation> annotationType() {
		return FieldSet.class;
	}
}