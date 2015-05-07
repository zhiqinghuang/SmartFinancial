package com.manydesigns.portofino.database.platforms;

import com.manydesigns.portofino.model.database.ConnectionProvider;
import org.hibernate.dialect.H2Dialect;

import java.sql.Connection;

public class H2DatabasePlatform extends AbstractDatabasePlatform {
	public final static String DESCRIPTION = "H2";
	public final static String STANDARD_DRIVER_CLASS_NAME = "org.h2.Driver";

	public H2DatabasePlatform() {
		super(new H2Dialect(), "jdbc:h2:<database or connection spec>");
	}

	public String getDescription() {
		return DESCRIPTION;
	}

	public String getStandardDriverClassName() {
		return STANDARD_DRIVER_CLASS_NAME;
	}

	public boolean isApplicable(ConnectionProvider connectionProvider) {
		return "H2".equals(connectionProvider.getDatabaseProductName());
	}

	@Override
	public void shutdown(ConnectionProvider connectionProvider) {
		super.shutdown(connectionProvider);
		Connection connection = null;
		try {
			connection = connectionProvider.acquireConnection();
			connection.createStatement().execute("SHUTDOWN");
		} catch (Exception e) {
			logger.warn("Could not shutdown connection provider: {}", connectionProvider.getDatabase().getDatabaseName());
		} finally {
			connectionProvider.releaseConnection(connection);
		}
	}
}