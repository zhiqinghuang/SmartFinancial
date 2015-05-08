package com.manydesigns.mail.queue;

public class MailParseException extends QueueException {

	private static final long serialVersionUID = 2355537743207535545L;

	public MailParseException() {
	}

	public MailParseException(String message) {
		super(message);
	}

	public MailParseException(String message, Throwable cause) {
		super(message, cause);
	}

	public MailParseException(Throwable cause) {
		super(cause);
	}
}