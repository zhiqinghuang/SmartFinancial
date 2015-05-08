package com.manydesigns.portofino.persistence.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HibernateDatabaseSetup {
	protected final Configuration configuration;
	protected final SessionFactory sessionFactory;
	protected final ThreadLocal<Session> threadSessions;

	public static final Logger logger = LoggerFactory.getLogger(HibernateDatabaseSetup.class);

	public HibernateDatabaseSetup(Configuration configuration, SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
		this.configuration = configuration;
		threadSessions = new ThreadLocal<Session>();
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public Configuration getConfiguration() {
		return configuration;
	}

	public ThreadLocal<Session> getThreadSessions() {
		return threadSessions;
	}

	public Session getThreadSession() {
		return getThreadSession(true);
	}

	public Session getThreadSession(boolean create) {
		Session session = threadSessions.get();
		if (session == null && create) {
			if (logger.isDebugEnabled()) {
				logger.debug("Creating thread-local session for {}", Thread.currentThread());
			}
			session = sessionFactory.openSession();
			session.beginTransaction();
			threadSessions.set(session);
		}
		return session;
	}

	public void setThreadSession(Session session) {
		threadSessions.set(session);
	}

	public void removeThreadSession() {
		threadSessions.remove();
	}
}