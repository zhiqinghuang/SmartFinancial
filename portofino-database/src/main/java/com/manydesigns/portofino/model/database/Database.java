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
public class Database implements ModelObject {
	protected final List<Schema> schemas;

	protected String databaseName;

	protected String trueString = null;
	protected String falseString = null;

	protected ConnectionProvider connectionProvider;

	public static final Logger logger = LoggerFactory.getLogger(Database.class);

	public Database() {
		this.schemas = new ArrayList<Schema>();
	}

	public void afterUnmarshal(Unmarshaller u, Object parent) {
	}

	public String getQualifiedName() {
		return databaseName;
	}

	public void reset() {
	}

	public void init(Model model) {
		assert databaseName != null;
	}

	public void link(Model model) {
	}

	public void visitChildren(ModelObjectVisitor visitor) {
		for (Schema schema : schemas) {
			visitor.visit(schema);
		}
	}

	@XmlAttribute(required = true)
	public String getDatabaseName() {
		return databaseName;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	@XmlElementWrapper(name = "schemas")
	@XmlElement(name = "schema", type = com.manydesigns.portofino.model.database.Schema.class)
	public List<Schema> getSchemas() {
		return schemas;
	}

	public List<Table> getAllTables() {
		List<Table> result = new ArrayList<Table>();
		for (Schema schema : schemas) {
			for (Table table : schema.getTables()) {
				result.add(table);
			}
		}
		return result;
	}

	public List<Column> getAllColumns() {
		List<Column> result = new ArrayList<Column>();
		for (Schema schema : schemas) {
			for (Table table : schema.getTables()) {
				for (Column column : table.getColumns()) {
					result.add(column);
				}
			}
		}
		return result;
	}

	public List<ForeignKey> getAllForeignKeys() {
		List<ForeignKey> result = new ArrayList<ForeignKey>();
		for (Schema schema : schemas) {
			for (Table table : schema.getTables()) {
				for (ForeignKey foreignKey : table.getForeignKeys()) {
					result.add(foreignKey);
				}
			}
		}
		return result;
	}

	@Override
	public String toString() {
		return MessageFormat.format("database {0}", getQualifiedName());
	}

	@XmlAttribute(required = false)
	public String getTrueString() {
		return trueString;
	}

	public void setTrueString(String trueString) {
		this.trueString = trueString;
	}

	@XmlAttribute(required = false)
	public String getFalseString() {
		return falseString;
	}

	public void setFalseString(String falseString) {
		this.falseString = falseString;
	}

	@XmlElements({ @XmlElement(name = "jdbcConnection", type = JdbcConnectionProvider.class), @XmlElement(name = "jndiConnection", type = JndiConnectionProvider.class) })
	public ConnectionProvider getConnectionProvider() {
		return connectionProvider;
	}

	public void setConnectionProvider(ConnectionProvider connectionProvider) {
		this.connectionProvider = connectionProvider;
	}
}