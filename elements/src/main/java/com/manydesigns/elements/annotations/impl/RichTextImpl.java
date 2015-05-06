package com.manydesigns.elements.annotations.impl;

import com.manydesigns.elements.annotations.RichText;

import java.lang.annotation.Annotation;

public class RichTextImpl implements RichText {

	private boolean value;

	public RichTextImpl(boolean value) {
		this.value = value;
	}

	public boolean value() {
		return value;
	}

	public Class<? extends Annotation> annotationType() {
		return RichText.class;
	}
}