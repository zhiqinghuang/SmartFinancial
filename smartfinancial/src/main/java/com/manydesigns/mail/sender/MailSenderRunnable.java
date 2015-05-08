package com.manydesigns.mail.sender;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

public class MailSenderRunnable implements Runnable {
	protected final MailSender sender;

	protected boolean alive;
	protected int pollInterval = 1000;

	public static final Logger logger = LoggerFactory.getLogger(MailSenderRunnable.class);

	public MailSenderRunnable(MailSender sender) {
		this.sender = sender;
	}

	public void run() {
		alive = true;
		try {
			mainLoop();
		} catch (InterruptedException e) {
			stop();
		}
	}

	protected void mainLoop() throws InterruptedException {
		Set<String> idsToMarkAsSent = new HashSet<String>();
		int pollIntervalMultiplier = 1;
		while (alive) {
			long now = System.currentTimeMillis();
			int serverErrors = sender.runOnce(idsToMarkAsSent);
			if (serverErrors < 0) {
				continue;
			} else if (serverErrors > 0) {
				if (pollIntervalMultiplier < 10) {
					pollIntervalMultiplier++;
					logger.debug("{} server errors, increased poll interval multiplier to {}", serverErrors, pollIntervalMultiplier);
				}
			} else {
				pollIntervalMultiplier = 1;
				logger.debug("No server errors, poll interval multiplier reset");
			}
			long sleep = pollInterval * pollIntervalMultiplier - (System.currentTimeMillis() - now);
			if (sleep > 0) {
				logger.debug("Sleeping for {}ms", sleep);
				Thread.sleep(sleep);
			}
		}
	}

	public void stop() {
		alive = false;
	}

	public int getPollInterval() {
		return pollInterval;
	}

	public void setPollInterval(int pollInterval) {
		this.pollInterval = pollInterval;
	}

	public boolean isAlive() {
		return alive;
	}

}