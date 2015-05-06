package com.manydesigns.elements.annotations.impl;

import java.lang.annotation.Annotation;

import com.manydesigns.elements.annotations.FieldSize;

public class FieldSizeImpl implements FieldSize {

    private final int value;

    public FieldSizeImpl(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

    public Class<? extends Annotation> annotationType() {
        return FieldSize.class;
    }
}