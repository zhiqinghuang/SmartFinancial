package com.manydesigns.elements.annotations.impl;

import com.manydesigns.elements.annotations.MinIntValue;

import java.lang.annotation.Annotation;

public class MinIntValueImpl implements MinIntValue {

	private final int value;

	public MinIntValueImpl(int value) {
		this.value = value;
	}

	public int value() {
		return value;
	}

	public Class<? extends Annotation> annotationType() {
		return MinIntValue.class;
	}
}