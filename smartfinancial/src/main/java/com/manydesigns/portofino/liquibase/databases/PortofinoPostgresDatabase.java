package com.manydesigns.portofino.liquibase.databases;

import liquibase.database.core.PostgresDatabase;

public class PortofinoPostgresDatabase extends PostgresDatabase {

	public PortofinoPostgresDatabase() {
		super();
		unmodifiableDataTypes.clear(); // Do read column length from the database
	}

	@Override
	public int getPriority() {
		return PRIORITY_DATABASE;
	}
}