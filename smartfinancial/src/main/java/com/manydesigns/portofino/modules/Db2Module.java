package com.manydesigns.portofino.modules;

import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.manydesigns.portofino.database.platforms.DatabasePlatformsRegistry;
import com.manydesigns.portofino.database.platforms.IbmDb2DatabasePlatform;
import com.manydesigns.portofino.di.Inject;

public class Db2Module implements Module {
	@Inject(BaseModule.PORTOFINO_CONFIGURATION)
	public Configuration configuration;

	@Inject(DatabaseModule.DATABASE_PLATFORMS_REGISTRY)
	DatabasePlatformsRegistry databasePlatformsRegistry;

	protected ModuleStatus status = ModuleStatus.CREATED;

	public static final Logger logger = LoggerFactory.getLogger(Db2Module.class);

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
		return "db2";
	}

	@Override
	public String getName() {
		return "DB2";
	}

	@Override
	public int install() {
		return 1;
	}

	@Override
	public void init() {
		databasePlatformsRegistry.addDatabasePlatform(new IbmDb2DatabasePlatform());
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