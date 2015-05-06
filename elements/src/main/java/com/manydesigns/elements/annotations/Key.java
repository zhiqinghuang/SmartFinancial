package com.manydesigns.elements.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.TYPE })
public @interface Key {

	public static final String DEFAULT_NAME = "default";

	String name() default DEFAULT_NAME;

	int order() default Integer.MIN_VALUE;
}