package com.manydesigns.elements.annotations.impl;

import com.manydesigns.elements.annotations.Memory;

import java.lang.annotation.Annotation;

public class MemoryImpl implements Memory {

	public MemoryImpl() {
	}

	public Class<? extends Annotation> annotationType() {
		return Memory.class;
	}
}