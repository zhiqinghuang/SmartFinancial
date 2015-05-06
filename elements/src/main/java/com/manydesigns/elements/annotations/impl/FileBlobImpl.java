package com.manydesigns.elements.annotations.impl;

import com.manydesigns.elements.annotations.FileBlob;

import java.lang.annotation.Annotation;

public class FileBlobImpl implements FileBlob {

    public FileBlobImpl() {}

    public Class<? extends Annotation> annotationType() {
        return FileBlob.class;
    }
}