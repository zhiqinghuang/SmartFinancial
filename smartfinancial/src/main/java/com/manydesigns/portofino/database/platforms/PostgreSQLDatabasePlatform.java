package com.manydesigns.portofino.database.platforms;

import com.manydesigns.portofino.model.database.ConnectionProvider;
import org.hibernate.dialect.PostgreSQL82Dialect;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

public class PostgreSQLDatabasePlatform extends AbstractDatabasePlatform {

	public final static String DESCRIPTION = "PostgreSQL";
	public final static String STANDARD_DRIVER_CLASS_NAME = "org.postgresql.Driver";

	public PostgreSQLDatabasePlatform() {
		super(new PostgreSQL82Dialect(), "jdbc:postgresql://<host>[:<port, default 5432>]/<database>");
	}

	public String getDescription() {
		return DESCRIPTION;
	}

	public String getStandardDriverClassName() {
		return STANDARD_DRIVER_CLASS_NAME;
	}

	public boolean isApplicable(ConnectionProvider connectionProvider) {
		return "PostgreSQL".equals(connectionProvider.getDatabaseProductName());
	}

	@Override
	public List<String> getSchemaNames(DatabaseMetaData databaseMetaData) throws SQLException {
		List<String> schemaNames = super.getSchemaNames(databaseMetaData);
		Iterator<String> it = schemaNames.iterator();
		while (it.hasNext()) {
			String next = it.next();
			if ("information_schema".equalsIgnoreCase(next) || next.startsWith("pg_")) {
				it.remove();
			}
		}
		return schemaNames;
	}
}