package com.manydesigns.elements.annotations.impl;

import com.manydesigns.elements.annotations.CodiceFiscale;

import java.lang.annotation.Annotation;

public class CodiceFiscaleImpl implements CodiceFiscale {

	public CodiceFiscaleImpl() {
	}

	public Class<? extends Annotation> annotationType() {
		return CodiceFiscale.class;
	}
}