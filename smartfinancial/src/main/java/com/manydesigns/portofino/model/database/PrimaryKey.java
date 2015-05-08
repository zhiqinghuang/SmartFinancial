package com.manydesigns.portofino.model.database;

import com.manydesigns.elements.annotations.Required;
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

@XmlAccessorType(value = XmlAccessType.NONE)
public class PrimaryKey implements ModelObject {
	protected final List<PrimaryKeyColumn> primaryKeyColumns;

	protected Table table;
	protected String primaryKeyName;

	protected final List<Column> columns;
	protected boolean valid;

	public static final Logger logger = LoggerFactory.getLogger(Table.class);

	public PrimaryKey() {
		columns = new ArrayList<Column>();
		primaryKeyColumns = new ArrayList<PrimaryKeyColumn>();
	}

	public PrimaryKey(Table table) {
		this();
		this.table = table;
	}

	public String getQualifiedName() {
		return MessageFormat.format("{0}#{1}", table.getQualifiedName(), primaryKeyName);
	}

	public void afterUnmarshal(Unmarshaller u, Object parent) {
		table = (Table) parent;
	}

	public void reset() {
		columns.clear();
		valid = true;
	}

	public void init(Model model) {
		assert table != null;

		if (primaryKeyColumns.isEmpty()) {
			throw new Error(MessageFormat.format("Primary key {0} has no columns", getQualifiedName()));
		}

	}

	public void link(Model model) {
		for (PrimaryKeyColumn pkc : primaryKeyColumns) {
			Column column = pkc.getActualColumn();
			if (column == null) {
				valid = false;
				logger.error("Invalid primary key column: {}-{}", getQualifiedName(), pkc.getColumnName());
			} else {
				columns.add(column);
			}
		}

		if (columns.isEmpty()) {
			logger.warn("Primary key '{}' has no columns", this);
		}
	}

	public void visitChildren(ModelObjectVisitor visitor) {
		for (PrimaryKeyColumn pkc : primaryKeyColumns) {
			visitor.visit(pkc);
		}
	}

	public PrimaryKeyColumn findPrimaryKeyColumnByNameIgnoreCase(String columnName) {
		for (PrimaryKeyColumn primaryKeyColumn : primaryKeyColumns) {
			if (primaryKeyColumn.getColumnName().equalsIgnoreCase(columnName)) {
				return primaryKeyColumn;
			}
		}
		return null;
	}

	public PrimaryKeyColumn findPrimaryKeyColumnByName(String columnName) {
		for (PrimaryKeyColumn primaryKeyColumn : primaryKeyColumns) {
			if (primaryKeyColumn.getColumnName().equals(columnName)) {
				return primaryKeyColumn;
			}
		}
		return null;
	}

	public Table getTable() {
		return table;
	}

	public void setTable(Table table) {
		this.table = table;
	}

	public List<Column> getColumns() {
		return columns;
	}

	public String getDatabaseName() {
		return table.getTableName();
	}

	public String getSchemaName() {
		return table.getSchemaName();
	}

	public String getTableName() {
		return table.getTableName();
	}

	@Required
	@XmlAttribute(required = true)
	public String getPrimaryKeyName() {
		return primaryKeyName;
	}

	public void setPrimaryKeyName(String primaryKeyName) {
		this.primaryKeyName = primaryKeyName;
	}

	@XmlElement(name = "column", type = PrimaryKeyColumn.class)
	public List<PrimaryKeyColumn> getPrimaryKeyColumns() {
		return primaryKeyColumns;
	}

	public boolean isValid() {
		return valid;
	}

	@Override
	public String toString() {
		return MessageFormat.format("primary key {0}", getQualifiedName());
	}
}