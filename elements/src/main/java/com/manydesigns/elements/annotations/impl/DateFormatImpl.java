package com.manydesigns.elements.annotations.impl;

import com.manydesigns.elements.annotations.DateFormat;

import java.lang.annotation.Annotation;

public class DateFormatImpl implements DateFormat {

	private final String value;

	public DateFormatImpl(String value) {
		this.value = value;
	}

	public String value() {
		return value;
	}

	public Class<? extends Annotation> annotationType() {
		return DateFormat.class;
	}
}