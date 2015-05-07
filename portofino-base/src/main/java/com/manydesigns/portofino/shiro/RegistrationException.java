package com.manydesigns.portofino.shiro;

public class RegistrationException extends RuntimeException {

	private static final long serialVersionUID = 8067926900999407041L;

	public RegistrationException() {
	}

	public RegistrationException(String message) {
		super(message);
	}

	public RegistrationException(String message, Throwable cause) {
		super(message, cause);
	}

	public RegistrationException(Throwable cause) {
		super(cause);
	}
}