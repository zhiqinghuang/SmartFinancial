package com.manydesigns.portofino.buttons.annotations;

import com.manydesigns.portofino.buttons.GuardType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface Guard {
	String test();

	GuardType type() default GuardType.ENABLED;
}