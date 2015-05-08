package com.manydesigns.mail.queue;

import com.manydesigns.mail.queue.model.Email;

import java.util.List;

public interface MailQueue {
	String enqueue(Email email) throws QueueException;

	List<String> getEnqueuedEmailIds() throws QueueException;

	Email loadEmail(String id) throws QueueException;

	void markSent(String id) throws QueueException;

	void markFailed(String id) throws QueueException;

	boolean isKeepSent();

	void setKeepSent(boolean keepSent);
}