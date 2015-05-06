package com.manydesigns.elements.annotations.impl;

import com.manydesigns.elements.annotations.PrecisionScale;

import java.lang.annotation.Annotation;

public class PrecisionScaleImpl implements PrecisionScale {

	private final int precision;
	private final int scale;

	public PrecisionScaleImpl(int precision, int scale) {
		this.precision = precision;
		this.scale = scale;
	}

	public int precision() {
		return precision;
	}

	public int scale() {
		return scale;
	}

	public Class<? extends Annotation> annotationType() {
		return PrecisionScale.class;
	}
}