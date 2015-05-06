package com.manydesigns.elements.annotations.impl;

import com.manydesigns.elements.annotations.Help;

import java.lang.annotation.Annotation;

public class HelpImpl implements Help {

	private final String value;

	public HelpImpl(String value) {
		this.value = value;
	}

	public String value() {
		return value;
	}

	public Class<? extends Annotation> annotationType() {
		return Help.class;
	}
}