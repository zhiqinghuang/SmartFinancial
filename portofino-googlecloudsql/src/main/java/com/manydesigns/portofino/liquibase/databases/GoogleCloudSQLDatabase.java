package com.manydesigns.portofino.liquibase.databases;

import liquibase.database.DatabaseConnection;
import liquibase.database.core.MySQLDatabase;
import liquibase.exception.DatabaseException;

public class GoogleCloudSQLDatabase extends MySQLDatabase {
	public int getPriority() {
		return PRIORITY_DEFAULT;
	}

	public boolean isCorrectDatabaseImplementation(DatabaseConnection conn) throws DatabaseException {
		return conn.getDatabaseProductName().contains("Google");
	}

	public String getDefaultDriver(String url) {
		if (url.startsWith("jdbc:google:rdbms")) {
			return "com.google.appengine.api.rdbms.AppEngineDriver";
		}
		return null;
	}
}