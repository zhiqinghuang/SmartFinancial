package com.manydesigns.portofino.database.platforms;

import com.manydesigns.portofino.model.database.ConnectionProvider;
import org.hibernate.dialect.DB2Dialect;

public class IbmDb2DatabasePlatform extends AbstractDatabasePlatform {
	public final static String DESCRIPTION = "IBM DB2";
	public final static String STANDARD_DRIVER_CLASS_NAME = "com.ibm.db2.jcc.DB2Driver";

	public IbmDb2DatabasePlatform() {
		super(new DB2Dialect(), "jdbc:db2://<host>[:<port>]/<database_name>");
	}

	public String getDescription() {
		return DESCRIPTION;
	}

	public String getStandardDriverClassName() {
		return STANDARD_DRIVER_CLASS_NAME;
	}

	public boolean isApplicable(ConnectionProvider connectionProvider) {
		return connectionProvider.getDatabaseProductName().startsWith("DB2/");
	}
}