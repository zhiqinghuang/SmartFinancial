package com.manydesigns.elements.annotations.impl;

import com.manydesigns.elements.annotations.ForceNewRow;

import java.lang.annotation.Annotation;

public class ForceNewRowImpl implements ForceNewRow {

	public ForceNewRowImpl() {
	}

	public Class<? extends Annotation> annotationType() {
		return ForceNewRow.class;
	}
}