package com.manydesigns.elements.annotations.impl;

import com.manydesigns.elements.annotations.Phone;

import java.lang.annotation.Annotation;

public class PhoneImpl implements Phone {
	public PhoneImpl() {
	}

	public Class<? extends Annotation> annotationType() {
		return Phone.class;
	}
}