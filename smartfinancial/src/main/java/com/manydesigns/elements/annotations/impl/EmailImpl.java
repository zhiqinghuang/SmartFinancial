package com.manydesigns.elements.annotations.impl;

import com.manydesigns.elements.annotations.Email;

import java.lang.annotation.Annotation;

public class EmailImpl implements Email {

	public EmailImpl() {
	}

	public Class<? extends Annotation> annotationType() {
		return Email.class;
	}
}