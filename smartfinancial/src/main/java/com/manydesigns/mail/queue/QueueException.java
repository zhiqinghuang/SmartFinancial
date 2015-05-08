package com.manydesigns.mail.queue;

public class QueueException extends Exception {

	private static final long serialVersionUID = 9081793252560858104L;

	public QueueException() {
	}

	public QueueException(String message) {
		super(message);
	}

	public QueueException(String message, Throwable cause) {
		super(message, cause);
	}

	public QueueException(Throwable cause) {
		super(cause);
	}
}