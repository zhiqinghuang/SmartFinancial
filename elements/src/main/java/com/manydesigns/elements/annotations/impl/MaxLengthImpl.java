package com.manydesigns.elements.annotations.impl;

import com.manydesigns.elements.annotations.MaxLength;

import java.lang.annotation.Annotation;

public class MaxLengthImpl implements MaxLength {

	private final int value;

	public MaxLengthImpl(int value) {
		this.value = value;
	}

	public int value() {
		return value;
	}

	public Class<? extends Annotation> annotationType() {
		return MaxLength.class;
	}
}