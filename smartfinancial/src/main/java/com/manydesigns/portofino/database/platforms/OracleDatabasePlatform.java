package com.manydesigns.portofino.database.platforms;

import com.manydesigns.portofino.model.database.ConnectionProvider;
import org.hibernate.dialect.Oracle9iDialect;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.List;

public class OracleDatabasePlatform extends AbstractDatabasePlatform {
	public final static String DESCRIPTION = "Oracle";
	public final static String STANDARD_DRIVER_CLASS_NAME = "oracle.jdbc.driver.OracleDriver";

	public OracleDatabasePlatform() {
		super(new Oracle9iDialect(), "jdbc:oracle:thin:@//<host>:<port, default 1521>:<sid>");
	}

	public String getDescription() {
		return DESCRIPTION;
	}

	public String getStandardDriverClassName() {
		return STANDARD_DRIVER_CLASS_NAME;
	}

	public boolean isApplicable(ConnectionProvider connectionProvider) {
		return "Oracle".equals(connectionProvider.getDatabaseProductName());
	}

	@Override
	public List<String> getSchemaNames(DatabaseMetaData databaseMetaData) throws SQLException {
		List<String> schemaNames = super.getSchemaNames(databaseMetaData);
		schemaNames.remove("SYSTEM");
		schemaNames.remove("SYS");
		return schemaNames;
	}
}