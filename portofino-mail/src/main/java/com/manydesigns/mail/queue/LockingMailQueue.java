package com.manydesigns.mail.queue;

import com.manydesigns.mail.queue.model.Email;

import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LockingMailQueue implements MailQueue {
	protected final MailQueue mailQueue;
	protected final ReadWriteLock lock = new ReentrantReadWriteLock(true);

	public LockingMailQueue(MailQueue mailQueue) {
		this.mailQueue = mailQueue;
	}

	public String enqueue(Email email) throws QueueException {
		lock.writeLock().lock();
		try {
			return mailQueue.enqueue(email);
		} finally {
			lock.writeLock().unlock();
		}
	}

	public List<String> getEnqueuedEmailIds() throws QueueException {
		lock.readLock().lock();
		try {
			return mailQueue.getEnqueuedEmailIds();
		} finally {
			lock.readLock().unlock();
		}
	}

	public Email loadEmail(String id) throws QueueException {
		lock.readLock().lock();
		try {
			return mailQueue.loadEmail(id);
		} finally {
			lock.readLock().unlock();
		}
	}

	public void markSent(String id) throws QueueException {
		lock.writeLock().lock();
		try {
			mailQueue.markSent(id);
		} finally {
			lock.writeLock().unlock();
		}
	}

	public void markFailed(String id) throws QueueException {
		lock.writeLock().lock();
		try {
			mailQueue.markFailed(id);
		} finally {
			lock.writeLock().unlock();
		}
	}

	public void setKeepSent(boolean keepSent) {
		mailQueue.setKeepSent(keepSent);
	}

	public boolean isKeepSent() {
		return mailQueue.isKeepSent();
	}
}