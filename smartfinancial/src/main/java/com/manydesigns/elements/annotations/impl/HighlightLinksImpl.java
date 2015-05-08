package com.manydesigns.elements.annotations.impl;

import com.manydesigns.elements.annotations.HighlightLinks;

import java.lang.annotation.Annotation;

public class HighlightLinksImpl implements HighlightLinks {

	private boolean value;

	public HighlightLinksImpl(boolean value) {
		this.value = value;
	}

	public boolean value() {
		return value;
	}

	public Class<? extends Annotation> annotationType() {
		return HighlightLinks.class;
	}
}