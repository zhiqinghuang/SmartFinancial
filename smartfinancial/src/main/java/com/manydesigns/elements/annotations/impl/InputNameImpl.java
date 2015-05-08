package com.manydesigns.elements.annotations.impl;

import java.lang.annotation.Annotation;

import com.manydesigns.elements.annotations.InputName;

public class InputNameImpl implements InputName {

    private final String value;

    public InputNameImpl(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public Class<? extends Annotation> annotationType() {
        return InputName.class;
    }
}