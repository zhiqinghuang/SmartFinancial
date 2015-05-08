package com.manydesigns.elements.annotations.impl;

import com.manydesigns.elements.annotations.InSummary;

import java.lang.annotation.Annotation;

public class InSummaryImpl implements InSummary {

	private boolean value;

	public InSummaryImpl(boolean value) {
		this.value = value;
	}

	public boolean value() {
		return value;
	}

	public Class<? extends Annotation> annotationType() {
		return InSummary.class;
	}
}