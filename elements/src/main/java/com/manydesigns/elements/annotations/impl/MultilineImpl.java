package com.manydesigns.elements.annotations.impl;

import com.manydesigns.elements.annotations.Multiline;

import java.lang.annotation.Annotation;

public class MultilineImpl implements Multiline {

	private boolean value;

	public MultilineImpl(boolean value) {
		this.value = value;
	}

	public boolean value() {
		return value;
	}

	public Class<? extends Annotation> annotationType() {
		return Multiline.class;
	}
}