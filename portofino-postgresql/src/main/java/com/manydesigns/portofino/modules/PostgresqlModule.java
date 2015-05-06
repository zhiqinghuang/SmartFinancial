package com.manydesigns.portofino.modules;

import com.manydesigns.portofino.database.platforms.DatabasePlatformsRegistry;
import com.manydesigns.portofino.database.platforms.PostgreSQLDatabasePlatform;
import com.manydesigns.portofino.di.Inject;
import com.manydesigns.portofino.liquibase.databases.PortofinoPostgresDatabase;
import liquibase.database.DatabaseFactory;
import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PostgresqlModule implements Module {
	@Inject(BaseModule.PORTOFINO_CONFIGURATION)
	public Configuration configuration;

	@Inject(DatabaseModule.DATABASE_PLATFORMS_REGISTRY)
	DatabasePlatformsRegistry databasePlatformsRegistry;

	protected ModuleStatus status = ModuleStatus.CREATED;

	public static final Logger logger = LoggerFactory.getLogger(PostgresqlModule.class);

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
		return "postgresql";
	}

	@Override
	public String getName() {
		return "PostgreSQL";
	}

	@Override
	public int install() {
		return 1;
	}

	@Override
	public void init() {
		databasePlatformsRegistry.addDatabasePlatform(new PostgreSQLDatabasePlatform());
		DatabaseFactory.getInstance().register(new PortofinoPostgresDatabase());
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