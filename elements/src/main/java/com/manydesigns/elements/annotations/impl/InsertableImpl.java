package com.manydesigns.elements.annotations.impl;

import java.lang.annotation.Annotation;

import com.manydesigns.elements.annotations.Insertable;

public class InsertableImpl implements Insertable {

	private boolean value;

	public InsertableImpl(boolean value) {
		this.value = value;
	}

	public boolean value() {
		return value;
	}

	public Class<? extends Annotation> annotationType() {
		return Insertable.class;
	}
}