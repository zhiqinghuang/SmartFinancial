package com.manydesigns.portofino.modules;

import com.manydesigns.mail.quartz.MailScheduler;
import com.manydesigns.mail.queue.MailQueue;
import com.manydesigns.mail.setup.MailQueueSetup;
import com.manydesigns.portofino.di.Inject;
import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;

public class MailModule implements Module {

	@Inject(BaseModule.SERVLET_CONTEXT)
	public ServletContext servletContext;

	@Inject(BaseModule.PORTOFINO_CONFIGURATION)
	public Configuration configuration;

	protected MailQueueSetup mailQueueSetup;

	protected ModuleStatus status = ModuleStatus.CREATED;

	public final static String MAIL_QUEUE = "com.manydesigns.mail.queue";
	public final static String MAIL_SENDER = "com.manydesigns.mail.sender";

	public static final Logger logger = LoggerFactory.getLogger(MailModule.class);

	@Override
	public String getModuleVersion() {
		return ModuleRegistry.getPortofinoVersion();
	}

	@Override
	public int getMigrationVersion() {
		return 1;
	}

	@Override
	public double getPriority() {
		return 20;
	}

	@Override
	public String getId() {
		return "mail";
	}

	@Override
	public String getName() {
		return "Mail";
	}

	@Override
	public int install() {
		return 1;
	}

	@Override
	public void init() {
		mailQueueSetup = new MailQueueSetup(configuration);
		mailQueueSetup.setup();

		MailQueue mailQueue = mailQueueSetup.getMailQueue();
		if (mailQueue == null) {
			logger.info("Mail queue not enabled");
			return;
		}

		servletContext.setAttribute(MAIL_QUEUE, mailQueue);
		servletContext.setAttribute(MAIL_SENDER, mailQueueSetup.getMailSender());

		status = ModuleStatus.ACTIVE;
	}

	@Override
	public void start() {
		// Quartz integration (optional)
		try {
			// In classe separata per permettere al modulo di essere caricato
			// anche in assenza di Quartz a runtime
			MailScheduler.setupMailScheduler(mailQueueSetup);
		} catch (NoClassDefFoundError e) {
			logger.debug(e.getMessage(), e);
			logger.info("Quartz is not available, mail scheduler not started");
		}
		status = ModuleStatus.STARTED;
	}

	@Override
	public void stop() {
		status = ModuleStatus.STOPPED;
	}

	@Override
	public void destroy() {
		status = ModuleStatus.DESTROYED;
	}

	@Override
	public ModuleStatus getStatus() {
		return status;
	}
}