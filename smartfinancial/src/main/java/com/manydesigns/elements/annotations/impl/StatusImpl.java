package com.manydesigns.elements.annotations.impl;

import com.manydesigns.elements.annotations.Status;

import java.lang.annotation.Annotation;

public class StatusImpl implements Status {

    private String[] red;
    private String[] amber;
    private String[] green;

    public StatusImpl(String[] red, String[] amber, String[] green) {
        this.red = red;
        this.amber = amber;
        this.green = green;
    }

    public String[] red() {
        return red;
    }

    public String[] amber() {
        return amber;
    }

    public String[] green() {
        return green;
    }

    public Class<? extends Annotation> annotationType() {
        return Status.class;
    }
}