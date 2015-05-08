package com.manydesigns.elements.annotations.impl;

import com.manydesigns.elements.annotations.PartitaIva;

import java.lang.annotation.Annotation;

public class PartitaIvaImpl implements PartitaIva {
	public PartitaIvaImpl() {
	}

	public Class<? extends Annotation> annotationType() {
		return PartitaIva.class;
	}
}