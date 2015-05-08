package com.manydesigns.elements.annotations;

import com.manydesigns.elements.options.DisplayMode;
import com.manydesigns.elements.options.SearchDisplayMode;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD })
public @interface Select {

	DisplayMode displayMode() default DisplayMode.DROPDOWN;

	SearchDisplayMode searchDisplayMode() default SearchDisplayMode.DROPDOWN;

	String[] values() default {};

	String[] labels() default {};

	boolean nullOption() default true;
}