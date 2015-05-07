package com.manydesigns.portofino.persistence;

public class ModelObjectNotFoundError extends Error {
	private static final long serialVersionUID = -3510109159931307837L;

	public ModelObjectNotFoundError() {
	}

	public ModelObjectNotFoundError(String s) {
		super(s);
	}

	public ModelObjectNotFoundError(String s, Throwable throwable) {
		super(s, throwable);
	}

	public ModelObjectNotFoundError(Throwable throwable) {
		super(throwable);
	}
}