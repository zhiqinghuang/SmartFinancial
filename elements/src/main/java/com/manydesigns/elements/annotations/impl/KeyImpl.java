package com.manydesigns.elements.annotations.impl;

import com.manydesigns.elements.annotations.Key;

import java.lang.annotation.Annotation;

public class KeyImpl implements Key {

	private final String name;

	private final int order;

	public KeyImpl(String name, int order) {
		this.name = name;
		this.order = order;
	}

	public String name() {
		return name;
	}

	public int order() {
		return order;
	}

	public Class<? extends Annotation> annotationType() {
		return Key.class;
	}
}