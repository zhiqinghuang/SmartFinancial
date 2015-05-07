package com.manydesigns.portofino.shiro;

public class ExistingUserException extends RegistrationException {
	private static final long serialVersionUID = -7218566992034485849L;

	public ExistingUserException() {
	}

	public ExistingUserException(String message) {
		super(message);
	}

	public ExistingUserException(String message, Throwable cause) {
		super(message, cause);
	}

	public ExistingUserException(Throwable cause) {
		super(cause);
	}
}