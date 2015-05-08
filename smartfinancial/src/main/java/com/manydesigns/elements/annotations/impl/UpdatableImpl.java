package com.manydesigns.elements.annotations.impl;

import java.lang.annotation.Annotation;

import com.manydesigns.elements.annotations.Updatable;

public class UpdatableImpl implements Updatable {

	private boolean value;

	public UpdatableImpl(boolean value) {
		this.value = value;
	}

	public boolean value() {
		return value;
	}

	public Class<? extends Annotation> annotationType() {
		return Updatable.class;
	}
}