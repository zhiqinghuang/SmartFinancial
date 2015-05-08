package com.manydesigns.elements.annotations.impl;

import com.manydesigns.elements.annotations.Label;

import java.lang.annotation.Annotation;

public class LabelImpl implements Label {

	private final String value;

	public LabelImpl(String value) {
		this.value = value;
	}

	public String value() {
		return value;
	}

	public Class<? extends Annotation> annotationType() {
		return Label.class;
	}
}