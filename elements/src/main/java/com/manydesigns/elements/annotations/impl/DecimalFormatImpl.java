package com.manydesigns.elements.annotations.impl;

import com.manydesigns.elements.annotations.DecimalFormat;

import java.lang.annotation.Annotation;

public class DecimalFormatImpl implements DecimalFormat {

	private final String value;

	public DecimalFormatImpl(String value) {
		this.value = value;
	}

	public String value() {
		return value;
	}

	public Class<? extends Annotation> annotationType() {
		return DecimalFormat.class;
	}
}