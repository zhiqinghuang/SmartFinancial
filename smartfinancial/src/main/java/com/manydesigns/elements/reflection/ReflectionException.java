package com.manydesigns.elements.reflection;

public class ReflectionException extends RuntimeException {

	private static final long serialVersionUID = -1710345914935142860L;

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