package com.manydesigns.portofino.modules;

import com.manydesigns.portofino.database.platforms.DatabasePlatformsRegistry;
import com.manydesigns.portofino.di.Inject;
import com.manydesigns.portofino.di.Injections;
import com.manydesigns.portofino.persistence.Persistence;
import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import java.io.File;

public class DatabaseModule implements Module {
	@Inject(BaseModule.SERVLET_CONTEXT)
	public ServletContext servletContext;

	@Inject(BaseModule.PORTOFINO_CONFIGURATION)
	public Configuration configuration;

	@Inject(BaseModule.APPLICATION_DIRECTORY)
	public File applicationDirectory;

	protected Persistence persistence;

	protected ModuleStatus status = ModuleStatus.CREATED;

	public static final String PERSISTENCE = "com.manydesigns.portofino.modules.DatabaseModule.persistence";
	public static final String DATABASE_PLATFORMS_REGISTRY = "com.manydesigns.portofino.modules.DatabaseModule.databasePlatformsRegistry";
	// Liquibase properties
	public static final String LIQUIBASE_ENABLED = "liquibase.enabled";

	public static final Logger logger = LoggerFactory.getLogger(DatabaseModule.class);

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
		return 10;
	}

	@Override
	public String getId() {
		return "database";
	}

	@Override
	public String getName() {
		return "Database";
	}

	@Override
	public int install() {
		return 1;
	}

	@Override
	public void init() {
		logger.info("Initializing persistence");
		DatabasePlatformsRegistry databasePlatformsRegistry = new DatabasePlatformsRegistry(configuration);

		persistence = new Persistence(applicationDirectory, configuration, databasePlatformsRegistry);
		Injections.inject(persistence, servletContext, null);
		servletContext.setAttribute(DATABASE_PLATFORMS_REGISTRY, databasePlatformsRegistry);
		servletContext.setAttribute(PERSISTENCE, persistence);

		status = ModuleStatus.ACTIVE;
	}

	@Override
	public void start() {
		persistence.loadXmlModel();
		status = ModuleStatus.STARTED;
	}

	@Override
	public void stop() {
		persistence.shutdown();
		status = ModuleStatus.STOPPED;
	}

	@Override
	public void destroy() {
		logger.info("ManyDesigns Portofino database module stopping...");
		logger.info("ManyDesigns Portofino database module stopped.");
		status = ModuleStatus.DESTROYED;
	}

	@Override
	public ModuleStatus getStatus() {
		return status;
	}
}