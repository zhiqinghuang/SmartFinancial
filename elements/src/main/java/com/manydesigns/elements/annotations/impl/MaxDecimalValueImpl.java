package com.manydesigns.elements.annotations.impl;

import com.manydesigns.elements.annotations.MaxDecimalValue;

import java.lang.annotation.Annotation;

public class MaxDecimalValueImpl implements MaxDecimalValue {

	private final double value;

	public MaxDecimalValueImpl(double value) {
		this.value = value;
	}

	public double value() {
		return value;
	}

	public Class<? extends Annotation> annotationType() {
		return MaxDecimalValue.class;
	}
}