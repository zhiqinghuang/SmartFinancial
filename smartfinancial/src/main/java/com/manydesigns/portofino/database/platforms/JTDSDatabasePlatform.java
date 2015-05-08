package com.manydesigns.portofino.database.platforms;

import com.manydesigns.portofino.model.database.ConnectionProvider;
import org.hibernate.dialect.SQLServerDialect;

public class JTDSDatabasePlatform extends AbstractDatabasePlatform {
	public final static String DESCRIPTION = "Microsoft SQL Server (jTDS driver)";
	public final static String STANDARD_DRIVER_CLASS_NAME = "net.sourceforge.jtds.jdbc.Driver";

	public JTDSDatabasePlatform() {
		super(new SQLServerDialect(), "jdbc:jtds:sqlserver://<server>[:<port>][/<database>][;instance=<instance>]");
	}

	public String getDescription() {
		return DESCRIPTION;
	}

	public String getStandardDriverClassName() {
		return STANDARD_DRIVER_CLASS_NAME;
	}

	public void test() {
		super.test();
		if (status == STATUS_OK) {
			checkJdbc4();
		}
	}

	protected void checkJdbc4() {
		try {
			Class.forName("java.sql.NClob");
		} catch (ClassNotFoundException e) {
			status = STATUS_DRIVER_ERROR;
		}
	}

	public boolean isApplicable(ConnectionProvider connectionProvider) {
		return connectionProvider.getDatabaseProductName().startsWith("Microsoft SQL Server") && connectionProvider.getDriverName().contains("jTDS");
	}
}