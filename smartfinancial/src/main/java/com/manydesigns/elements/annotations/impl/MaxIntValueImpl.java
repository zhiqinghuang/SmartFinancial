package com.manydesigns.elements.annotations.impl;

import com.manydesigns.elements.annotations.MaxIntValue;

import java.lang.annotation.Annotation;

public class MaxIntValueImpl implements MaxIntValue {

	private final int value;

	public MaxIntValueImpl(int value) {
		this.value = value;
	}

	public int value() {
		return value;
	}

	public Class<? extends Annotation> annotationType() {
		return MaxIntValue.class;
	}
}