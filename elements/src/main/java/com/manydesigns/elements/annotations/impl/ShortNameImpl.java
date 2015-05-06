package com.manydesigns.elements.annotations.impl;

import com.manydesigns.elements.annotations.ShortName;

import java.lang.annotation.Annotation;

public class ShortNameImpl implements ShortName {

	private String value;

	public ShortNameImpl(String value) {
		this.value = value;
	}

	public String value() {
		return value;
	}

	public Class<? extends Annotation> annotationType() {
		return ShortName.class;
	}
}