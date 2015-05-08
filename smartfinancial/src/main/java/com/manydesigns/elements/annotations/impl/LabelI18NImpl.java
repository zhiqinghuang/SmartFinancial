package com.manydesigns.elements.annotations.impl;

import com.manydesigns.elements.annotations.LabelI18N;

import java.lang.annotation.Annotation;

public class LabelI18NImpl implements LabelI18N {

	private final String value;

	public LabelI18NImpl(String value) {
		this.value = value;
	}

	public String value() {
		return value;
	}

	public Class<? extends Annotation> annotationType() {
		return LabelI18N.class;
	}
}