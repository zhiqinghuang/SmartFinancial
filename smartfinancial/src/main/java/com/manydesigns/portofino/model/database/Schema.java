package com.manydesigns.portofino.model.database;

import com.manydesigns.portofino.model.Model;
import com.manydesigns.portofino.model.ModelObject;
import com.manydesigns.portofino.model.ModelObjectVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.NONE)
public class Schema implements ModelObject {
	protected Database database;
	protected final List<Table> tables;

	protected String schemaName;

	public static final Logger logger = LoggerFactory.getLogger(Schema.class);

	public Schema() {
		tables = new ArrayList<Table>();
	}

	public Schema(Database database) {
		this();
		this.database = database;
	}

	public void afterUnmarshal(Unmarshaller u, Object parent) {
		database = (Database) parent;
	}

	public String getQualifiedName() {
		if (getDatabaseName() == null) {
			return schemaName;
		}
		return MessageFormat.format("{0}.{1}", getDatabaseName(), schemaName);
	}

	public void reset() {
	}

	public void init(Model model) {
		assert database != null;
		assert schemaName != null;
	}

	public void link(Model model) {
	}

	public void visitChildren(ModelObjectVisitor visitor) {
		for (Table table : tables) {
			visitor.visit(table);
		}
	}

	public Database getDatabase() {
		return database;
	}

	public void setDatabase(Database database) {
		this.database = database;
	}

	public String getDatabaseName() {
		return database != null ? database.getDatabaseName() : null;
	}

	@XmlAttribute(required = true)
	public String getSchemaName() {
		return schemaName;
	}

	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}

	@XmlElementWrapper(name = "tables")
	@XmlElement(name = "table", type = com.manydesigns.portofino.model.database.Table.class)
	public List<Table> getTables() {
		return tables;
	}

	public List<Column> getAllColumns() {
		List<Column> result = new ArrayList<Column>();
		for (Table table : tables) {
			for (Column column : table.getColumns()) {
				result.add(column);
			}
		}
		return result;
	}

	public Table findTableByQualifiedName(String qualifiedTableName) {
		int lastDot = qualifiedTableName.lastIndexOf(".");
		String tableName = qualifiedTableName.substring(lastDot + 1);
		for (Table table : tables) {
			if (table.getTableName().equalsIgnoreCase(tableName)) {
				return table;
			}
		}
		logger.debug("Table not found: {}", qualifiedTableName);
		return null;
	}

	public Column findColumnByQualifiedName(String qualifiedColumnName) {
		int lastDot = qualifiedColumnName.lastIndexOf(".");
		String qualifiedTableName = qualifiedColumnName.substring(0, lastDot);
		String columnName = qualifiedColumnName.substring(lastDot + 1);
		Table table = findTableByQualifiedName(qualifiedTableName);
		if (table != null) {
			for (Column column : table.getColumns()) {
				if (column.getColumnName().equalsIgnoreCase(columnName)) {
					return column;
				}
			}
		}
		logger.debug("Column not found: {}", qualifiedColumnName);
		return null;
	}

	@Override
	public String toString() {
		return MessageFormat.format("schema {0}", getQualifiedName());
	}

}