package com.manydesigns.portofino.database.platforms;

import com.manydesigns.elements.annotations.Status;
import com.manydesigns.portofino.model.database.ConnectionProvider;
import org.hibernate.dialect.Dialect;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.List;

public interface DatabasePlatform {
	final static String STATUS_CREATED = "created";
	final static String STATUS_OK = "ok";
	final static String STATUS_DRIVER_NOT_FOUND = "driver not found";
	final static String STATUS_DRIVER_ERROR = "driver error";

	String getDescription();

	String getStandardDriverClassName();

	Dialect getHibernateDialect();

	/**
	 * Is Hibernate able to automatically the dialect from a JDBC connection for
	 * this database platform?
	 * 
	 * @return false if and only if the hibernate.dialect property must be
	 *         explicitly set in order to connect to an instance of this
	 *         database platform.
	 */
	boolean isDialectAutodetected();

	String getConnectionStringTemplate();

	@Status(red = { STATUS_DRIVER_ERROR }, amber = { STATUS_CREATED, STATUS_DRIVER_NOT_FOUND }, green = { STATUS_OK })
	String getStatus();

	void test();

	boolean isApplicable(ConnectionProvider connectionProvider);

	void shutdown(ConnectionProvider connectionProvider);

	List<String> getSchemaNames(DatabaseMetaData databaseMetaData) throws SQLException;

}