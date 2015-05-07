package com.manydesigns.mail.quartz;

import com.manydesigns.mail.setup.MailProperties;
import com.manydesigns.mail.setup.MailQueueSetup;
import org.apache.commons.configuration.Configuration;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MailScheduler {
	public static final Logger logger = LoggerFactory.getLogger(MailScheduler.class);
	public static final int DEFAULT_POLL_INTERVAL = 1000;

	public static void setupMailScheduler(MailQueueSetup mailQueueSetup) {
		String group = "portofino-mail";
		setupMailScheduler(mailQueueSetup, group);
	}

	public static void setupMailScheduler(MailQueueSetup mailQueueSetup, String group) {
		Configuration mailConfiguration = mailQueueSetup.getMailConfiguration();
		if (mailConfiguration != null) {
			if (mailConfiguration.getBoolean(MailProperties.MAIL_QUARTZ_ENABLED, false)) {
				logger.info("Scheduling mail sends with Quartz job");
				try {
					String serverUrl = mailConfiguration.getString(MailProperties.MAIL_SENDER_SERVER_URL);
					logger.info("Scheduling mail sends using URL: {}", serverUrl);

					Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
					JobDetail job = JobBuilder.newJob(URLInvokeJob.class).withIdentity("mail.sender", group).usingJobData(URLInvokeJob.URL_KEY, serverUrl).build();

					int pollInterval = mailConfiguration.getInt(MailProperties.MAIL_SENDER_POLL_INTERVAL, DEFAULT_POLL_INTERVAL);

					Trigger trigger = TriggerBuilder.newTrigger().withIdentity("mail.sender.trigger", group).startNow().withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInMilliseconds(pollInterval).repeatForever()).build();

					scheduler.scheduleJob(job, trigger);
				} catch (Exception e) {
					logger.error("Could not schedule mail sender job", e);
				}
			} else {
				logger.info("Mail scheduling using Quartz is disabled");
			}
		}
	}
}