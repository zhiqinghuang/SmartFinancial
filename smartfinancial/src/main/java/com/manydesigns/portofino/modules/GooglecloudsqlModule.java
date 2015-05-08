package com.manydesigns.portofino.modules;

import com.manydesigns.portofino.database.platforms.DatabasePlatformsRegistry;
import com.manydesigns.portofino.database.platforms.GoogleCloudSQLDatabasePlatform;
import com.manydesigns.portofino.di.Inject;
import com.manydesigns.portofino.liquibase.databases.GoogleCloudSQLDatabase;
import com.manydesigns.portofino.liquibase.sqlgenerators.GoogleCloudSQLLockDatabaseChangeLogGenerator;
import liquibase.database.DatabaseFactory;
import liquibase.sqlgenerator.SqlGeneratorFactory;
import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GooglecloudsqlModule implements Module {
	@Inject(BaseModule.PORTOFINO_CONFIGURATION)
	public Configuration configuration;

	@Inject(DatabaseModule.DATABASE_PLATFORMS_REGISTRY)
	DatabasePlatformsRegistry databasePlatformsRegistry;

	protected ModuleStatus status = ModuleStatus.CREATED;

	public static final Logger logger = LoggerFactory.getLogger(GooglecloudsqlModule.class);

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
		return "googlecloudsql";
	}

	@Override
	public String getName() {
		return "Google Cloud SQL";
	}

	@Override
	public int install() {
		return 1;
	}

	@Override
	public void init() {
		if (configuration.getBoolean(DatabaseModule.LIQUIBASE_ENABLED, true)) {
			logger.debug("Registering Google Cloud SQL");
			DatabaseFactory.getInstance().register(new GoogleCloudSQLDatabase());
			logger.debug("Registering GoogleCloudSQLLockDatabaseChangeLogGenerator");
			SqlGeneratorFactory.getInstance().register(new GoogleCloudSQLLockDatabaseChangeLogGenerator());
		}
		databasePlatformsRegistry.addDatabasePlatform(new GoogleCloudSQLDatabasePlatform());
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