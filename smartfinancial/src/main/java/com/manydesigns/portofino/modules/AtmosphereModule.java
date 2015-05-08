package com.manydesigns.portofino.modules;

import com.manydesigns.portofino.atmosphere.notifications.NotificationService;
import com.manydesigns.portofino.di.Inject;
import org.atmosphere.cpr.AtmosphereFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;

public class AtmosphereModule implements Module {
	@Inject(BaseModule.SERVLET_CONTEXT)
	public ServletContext servletContext;

	public static final String NOTIFICATION_SERVICE = "com.manydesigns.portofino.modules.AtmosphereModule.notificationService";

	protected ModuleStatus status = ModuleStatus.CREATED;

	public static final Logger logger = LoggerFactory.getLogger(AtmosphereModule.class);

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
		return "atmosphere";
	}

	@Override
	public String getName() {
		return "Atmosphere notifications";
	}

	@Override
	public int install() {
		return 1;
	}

	@Override
	public void init() {
		AtmosphereFramework framework = (AtmosphereFramework) servletContext.getAttribute("AtmosphereServlet");
		servletContext.setAttribute(NOTIFICATION_SERVICE, new NotificationService(framework));
		status = ModuleStatus.ACTIVE;
	}

	@Override
	public void start() {
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