package com.manydesigns.portofino.database.platforms;

import org.hibernate.dialect.DerbyDialect;

import com.manydesigns.portofino.model.database.ConnectionProvider;

public class ApacheDerbyDatabasePlatform extends AbstractDatabasePlatform {
	public final static String DESCRIPTION = "Apache Derby";
	public final static String STANDARD_DRIVER_CLASS_NAME = "org.apache.derby.jdbc.EmbeddedDriver";

	public ApacheDerbyDatabasePlatform() {
		super(new DerbyDialect(), "jdbc:derby:<databaseName>");
	}

	public String getDescription() {
		return DESCRIPTION;
	}

	public String getStandardDriverClassName() {
		return STANDARD_DRIVER_CLASS_NAME;
	}

	public boolean isApplicable(ConnectionProvider connectionProvider) {
		return "Apache Derby".equals(connectionProvider.getDatabaseProductName());
	}
}