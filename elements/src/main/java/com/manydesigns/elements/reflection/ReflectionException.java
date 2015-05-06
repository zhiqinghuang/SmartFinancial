package com.manydesigns.elements.reflection;

public class ReflectionException extends RuntimeException {
	public static final String copyright = "Copyright (c) 2005-2015, ManyDesigns srl";

	public ReflectionException() {
	}

	public ReflectionException(String s) {
		super(s);
	}

	public ReflectionException(String s, Throwable throwable) {
		super(s, throwable);
	}

	public ReflectionException(Throwable throwable) {
		super(throwable);
	}
}