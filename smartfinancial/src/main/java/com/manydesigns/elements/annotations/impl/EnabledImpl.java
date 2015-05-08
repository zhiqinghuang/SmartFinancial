package com.manydesigns.elements.annotations.impl;

import java.lang.annotation.Annotation;

import com.manydesigns.elements.annotations.Enabled;

public class EnabledImpl implements Enabled {
	private boolean value;

	public EnabledImpl(boolean value) {
		this.value = value;
	}

	public boolean value() {
		return value;
	}

	public Class<? extends Annotation> annotationType() {
		return Enabled.class;
	}
}