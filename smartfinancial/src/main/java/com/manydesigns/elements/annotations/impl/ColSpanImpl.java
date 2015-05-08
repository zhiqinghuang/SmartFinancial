package com.manydesigns.elements.annotations.impl;

import com.manydesigns.elements.annotations.ColSpan;

import java.lang.annotation.Annotation;

public class ColSpanImpl implements ColSpan {

	private final int value;

	public ColSpanImpl(int value) {
		this.value = value;
	}

	public int value() {
		return value;
	}

	public Class<? extends Annotation> annotationType() {
		return ColSpan.class;
	}
}