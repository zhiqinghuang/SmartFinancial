package com.manydesigns.portofino.reflection;

import com.manydesigns.portofino.model.Annotation;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

public abstract class AbstractAnnotatedAccessor extends com.manydesigns.elements.reflection.AbstractAnnotatedAccessor {
    public static final Logger logger =
            LoggerFactory.getLogger(AbstractAnnotatedAccessor.class);


    public AbstractAnnotatedAccessor(@Nullable Collection<Annotation> annotations) {
        super();

        if (annotations == null) {
            return;
        }

        for (Annotation annotation : annotations) {
            Class annotationClass = annotation.getJavaAnnotationClass();
            java.lang.annotation.Annotation javaAnnotation = annotation.getJavaAnnotation();
            this.annotations.put(annotationClass, javaAnnotation);
        }
    }
}