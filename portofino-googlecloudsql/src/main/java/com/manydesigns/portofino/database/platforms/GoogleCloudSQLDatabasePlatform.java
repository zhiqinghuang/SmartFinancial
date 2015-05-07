package com.manydesigns.portofino.database.platforms;

import com.manydesigns.portofino.model.database.ConnectionProvider;
import org.apache.commons.dbutils.DbUtils;
import org.hibernate.dialect.MySQLDialect;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GoogleCloudSQLDatabasePlatform extends AbstractDatabasePlatform {
	public final static String DESCRIPTION = "Google Cloud SQL";
	public final static String STANDARD_DRIVER_CLASS_NAME = "com.google.appengine.api.rdbms.AppEngineDriver";

	public GoogleCloudSQLDatabasePlatform() {
		super(new MySQLDialect(), "jdbc:google:rdbms://<instance-name>/<database>");
		try {
			DriverManager.registerDriver((Driver) Class.forName("com.google.cloud.sql.Driver").newInstance());
		} catch (Exception e) {
			logger.debug("The driver to connect to Google Cloud SQL from a non-GAE application was not found", e);
		}
	}

	public String getDescription() {
		return DESCRIPTION;
	}

	public String getStandardDriverClassName() {
		return STANDARD_DRIVER_CLASS_NAME;
	}

	public boolean isApplicable(ConnectionProvider connectionProvider) {
		return connectionProvider.getDatabaseProductName().contains("Google");
	}

	public List<String> getSchemaNames(DatabaseMetaData databaseMetaData) throws SQLException {
		ResultSet rs = databaseMetaData.getCatalogs();
		List<String> schemaNames = new ArrayList<String>();
		try {
			while (rs.next()) {
				String schemaName = rs.getString(TABLE_CAT);
				schemaNames.add(schemaName);
			}
		} finally {
			DbUtils.closeQuietly(rs);
		}
		return schemaNames;
	}

	@Override
	public boolean isDialectAutodetected() {
		return false;
	}
}