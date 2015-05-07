package com.manydesigns.portofino.database.platforms;

import com.manydesigns.portofino.model.database.ConnectionProvider;
import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class DatabasePlatformsRegistry {
	protected final Configuration portofinoConfiguration;
	protected final List<DatabasePlatform> databasePlatformList = new ArrayList<DatabasePlatform>();

	public static final Logger logger = LoggerFactory.getLogger(DatabasePlatformsRegistry.class);

	public DatabasePlatformsRegistry(Configuration portofinoConfiguration) {
		this.portofinoConfiguration = portofinoConfiguration;
	}

	public void addDatabasePlatform(DatabasePlatform databasePlatform) {
		logger.debug("Adding database platform: {}", databasePlatform);
		databasePlatform.test();
		databasePlatformList.add(databasePlatform);
	}

	public DatabasePlatform findApplicableAbstraction(ConnectionProvider connectionProvider) {
		for (DatabasePlatform current : databasePlatformList) {
			if (current.isApplicable(connectionProvider)) {
				return current;
			}
		}
		return null;
	}

	public DatabasePlatform[] getDatabasePlatforms() {
		DatabasePlatform[] result = new DatabasePlatform[databasePlatformList.size()];
		databasePlatformList.toArray(result);
		return result;
	}

	public Configuration getPortofinoConfiguration() {
		return portofinoConfiguration;
	}
}