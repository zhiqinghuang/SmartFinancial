package com.manydesigns.portofino.actions.systemadmin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Handler;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import com.manydesigns.portofino.di.Inject;
import com.manydesigns.portofino.modules.BaseModule;
import com.manydesigns.portofino.servlets.ServerInfo;
import com.manydesigns.portofino.stripes.AbstractActionBean;

public class LogsAction extends AbstractActionBean {

	public LogManager logManager;
	public List<Logger> loggers;
	public Set<Handler> handlers;

	public String execute() {
		logManager = LogManager.getLogManager();
		loggers = new ArrayList<Logger>();
		handlers = new HashSet<Handler>();

		Enumeration<String> loggerNames = logManager.getLoggerNames();
		while (loggerNames.hasMoreElements()) {
			String loggerName = loggerNames.nextElement();
			Logger logger = logManager.getLogger(loggerName);
			loggers.add(logger);

			Handler[] handlers = logger.getHandlers();
			this.handlers.addAll(Arrays.asList(handlers));
		}
		Collections.sort(loggers, new LoggerComparator());
		return "SUCCESS";
	}

	@Inject(BaseModule.SERVER_INFO)
	public ServerInfo serverInfo;

	public class LoggerComparator implements Comparator<Logger> {
		public int compare(Logger l1, Logger l2) {
			return l1.getName().compareTo(l2.getName());
		}
	}
}