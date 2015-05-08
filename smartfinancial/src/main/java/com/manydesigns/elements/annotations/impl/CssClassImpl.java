package com.manydesigns.elements.annotations.impl;

import com.manydesigns.elements.annotations.CssClass;

import java.lang.annotation.Annotation;

public class CssClassImpl implements CssClass {

	private final String[] value;

	public CssClassImpl(String[] value) {
		this.value = value;
	}

	public String[] value() {
		return value;
	}

	public Class<? extends Annotation> annotationType() {
		return CssClass.class;
	}
}