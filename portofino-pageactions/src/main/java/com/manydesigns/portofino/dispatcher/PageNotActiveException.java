package com.manydesigns.portofino.dispatcher;

public class PageNotActiveException extends Exception {

	public PageNotActiveException() {
	}

	public PageNotActiveException(String message) {
		super(message);
	}

	public PageNotActiveException(String message, Throwable cause) {
		super(message, cause);
	}

	public PageNotActiveException(Throwable cause) {
		super(cause);
	}
}