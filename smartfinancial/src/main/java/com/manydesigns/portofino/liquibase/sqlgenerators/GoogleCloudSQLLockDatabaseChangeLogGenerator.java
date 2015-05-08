package com.manydesigns.portofino.liquibase.sqlgenerators;

import com.manydesigns.portofino.liquibase.databases.GoogleCloudSQLDatabase;
import liquibase.database.Database;
import liquibase.datatype.DataTypeFactory;
import liquibase.exception.ValidationErrors;
import liquibase.sql.Sql;
import liquibase.sqlgenerator.SqlGeneratorChain;
import liquibase.sqlgenerator.SqlGeneratorFactory;
import liquibase.sqlgenerator.core.AbstractSqlGenerator;
import liquibase.statement.core.LockDatabaseChangeLogStatement;
import liquibase.statement.core.UpdateStatement;

import java.sql.Timestamp;

public class GoogleCloudSQLLockDatabaseChangeLogGenerator extends AbstractSqlGenerator<LockDatabaseChangeLogStatement> {
	@Override
	public int getPriority() {
		return PRIORITY_DATABASE;
	}

	@Override
	public boolean supports(LockDatabaseChangeLogStatement statement, Database database) {
		return database instanceof GoogleCloudSQLDatabase;
	}

	public ValidationErrors validate(LockDatabaseChangeLogStatement statement, Database database, SqlGeneratorChain sqlGeneratorChain) {
		return new ValidationErrors();
	}

	public Sql[] generateSql(LockDatabaseChangeLogStatement statement, Database database, SqlGeneratorChain sqlGeneratorChain) {
		String liquibaseCatalog = database.getLiquibaseCatalogName();
		String liquibaseSchema = database.getLiquibaseSchemaName();

		UpdateStatement updateStatement = new UpdateStatement(liquibaseCatalog, liquibaseSchema, database.getDatabaseChangeLogLockTableName());
		updateStatement.addNewColumnValue("locked", true);
		updateStatement.addNewColumnValue("lockgranted", new Timestamp(new java.util.Date().getTime()));
		updateStatement.addNewColumnValue("lockedby", "Google Cloud SQL");
		updateStatement.setWhereClause(database.escapeColumnName(liquibaseCatalog, liquibaseSchema, database.getDatabaseChangeLogTableName(), "ID") + " = 1 AND " + database.escapeColumnName(liquibaseCatalog, liquibaseSchema, database.getDatabaseChangeLogTableName(), "LOCKED") + " = " + DataTypeFactory.getInstance().fromDescription("boolean", database).objectToSql(false, database));

		return SqlGeneratorFactory.getInstance().generateSql(updateStatement, database);

	}
}