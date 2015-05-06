package com.manydesigns.elements.annotations.impl;

import com.manydesigns.elements.annotations.Searchable;

import java.lang.annotation.Annotation;

public class SearchableImpl implements Searchable {

	private boolean value;

	public SearchableImpl(boolean value) {
		this.value = value;
	}

	public boolean value() {
		return value;
	}

	public Class<? extends Annotation> annotationType() {
		return Searchable.class;
	}
}