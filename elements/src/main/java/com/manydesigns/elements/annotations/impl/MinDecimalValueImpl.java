package com.manydesigns.elements.annotations.impl;

import com.manydesigns.elements.annotations.MinDecimalValue;

import java.lang.annotation.Annotation;

public class MinDecimalValueImpl implements MinDecimalValue {

	private final double value;

	public MinDecimalValueImpl(double value) {
		this.value = value;
	}

	public double value() {
		return value;
	}

	public Class<? extends Annotation> annotationType() {
		return MinDecimalValue.class;
	}
}