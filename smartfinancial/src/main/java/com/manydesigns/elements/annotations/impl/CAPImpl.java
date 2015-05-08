package com.manydesigns.elements.annotations.impl;

import com.manydesigns.elements.annotations.CAP;

import java.lang.annotation.Annotation;

public class CAPImpl implements CAP {

	public CAPImpl() {
	}

	public Class<? extends Annotation> annotationType() {
		return CAP.class;
	}
}