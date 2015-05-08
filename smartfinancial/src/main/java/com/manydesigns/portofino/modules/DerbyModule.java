package com.manydesigns.portofino.modules;

import com.manydesigns.portofino.database.platforms.ApacheDerbyDatabasePlatform;
import com.manydesigns.portofino.database.platforms.DatabasePlatformsRegistry;
import com.manydesigns.portofino.di.Inject;
import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DerbyModule implements Module {
	@Inject(BaseModule.PORTOFINO_CONFIGURATION)
	public Configuration configuration;

	@Inject(DatabaseModule.DATABASE_PLATFORMS_REGISTRY)
	DatabasePlatformsRegistry databasePlatformsRegistry;

	protected ModuleStatus status = ModuleStatus.CREATED;

	public static final Logger logger = LoggerFactory.getLogger(DerbyModule.class);

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
		return "derby";
	}

	@Override
	public String getName() {
		return "Derby";
	}

	@Override
	public int install() {
		return 1;
	}

	@Override
	public void init() {
		databasePlatformsRegistry.addDatabasePlatform(new ApacheDerbyDatabasePlatform());
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