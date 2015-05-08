package com.manydesigns.portofino.database.platforms;

import com.manydesigns.portofino.model.database.ConnectionProvider;
import org.hibernate.dialect.SQLServerDialect;

public class MSSqlServerDatabasePlatform extends AbstractDatabasePlatform {
	public final static String DESCRIPTION = "Microsoft SQL Server";
	public final static String STANDARD_DRIVER_CLASS_NAME = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

	public MSSqlServerDatabasePlatform() {
		super(new SQLServerDialect(), "jdbc:sqlserver://<host>[:<port>];database=<databaseName>");
	}

	public String getDescription() {
		return DESCRIPTION;
	}

	public String getStandardDriverClassName() {
		return STANDARD_DRIVER_CLASS_NAME;
	}

	public boolean isApplicable(ConnectionProvider connectionProvider) {
		return connectionProvider.getDatabaseProductName().startsWith("Microsoft SQL Server") && !connectionProvider.getDriverName().contains("jTDS");
	}
}