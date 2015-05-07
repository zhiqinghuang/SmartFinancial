package com.manydesigns.portofino.database.platforms;

import com.manydesigns.portofino.model.database.ConnectionProvider;
import org.apache.commons.dbutils.DbUtils;
import org.hibernate.dialect.MySQLDialect;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MySql5DatabasePlatform extends AbstractDatabasePlatform {
	public final static String DESCRIPTION = "MySQL 5.x";
	public final static String STANDARD_DRIVER_CLASS_NAME = "com.mysql.jdbc.Driver";

	public MySql5DatabasePlatform() {
		super(new MySQLDialect(), "jdbc:mysql://<host>[:<port>]/<database>");
	}

	public String getDescription() {
		return DESCRIPTION;
	}

	public String getStandardDriverClassName() {
		return STANDARD_DRIVER_CLASS_NAME;
	}

	public boolean isApplicable(ConnectionProvider connectionProvider) {
		return "MySQL".equals(connectionProvider.getDatabaseProductName());
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
}