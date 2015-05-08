package com.manydesigns.portofino.modules;

import com.manydesigns.portofino.di.Inject;
import com.manydesigns.portofino.pageactions.openid.OpenIdLoginAction;
import com.manydesigns.portofino.pageactions.registry.PageActionRegistry;
import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OpenIdModule implements Module {
	@Inject(BaseModule.PORTOFINO_CONFIGURATION)
	public Configuration configuration;

	@Inject(PageactionsModule.PAGE_ACTIONS_REGISTRY)
	public PageActionRegistry pageActionRegistry;

	protected ModuleStatus status = ModuleStatus.CREATED;

	public static final Logger logger = LoggerFactory.getLogger(OpenIdModule.class);

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
		return "openid";
	}

	@Override
	public String getName() {
		return "OpenID";
	}

	@Override
	public int install() {
		return 1;
	}

	@Override
	public void init() {
		pageActionRegistry.register(OpenIdLoginAction.class);
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