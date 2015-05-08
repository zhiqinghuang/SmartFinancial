package com.manydesigns.portofino.model.database;

import com.manydesigns.portofino.model.Model;
import com.manydesigns.portofino.model.ModelObject;
import com.manydesigns.portofino.model.ModelObjectVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.NONE)
public class PrimaryKeyColumn implements ModelObject {
	protected PrimaryKey primaryKey;
	protected String columnName;
	protected Generator generator;

	protected Column actualColumn;

	public static final Logger logger = LoggerFactory.getLogger(PrimaryKeyColumn.class);

	public PrimaryKeyColumn() {
	}

	public PrimaryKeyColumn(PrimaryKey primaryKey) {
		this();
		this.primaryKey = primaryKey;
	}

	public void afterUnmarshal(Unmarshaller u, Object parent) {
		primaryKey = (PrimaryKey) parent;
	}

	public void reset() {
		actualColumn = null;
	}

	public void link(Model model) {
	}

	public void visitChildren(ModelObjectVisitor visitor) {
	}

	public void init(Model model) {
		assert primaryKey != null;
		assert columnName != null;

		actualColumn = DatabaseLogic.findColumnByName(primaryKey.getTable(), columnName);
		if (actualColumn == null) {
			logger.warn("Cannor wire primary key column '{}' to primary key '{}'", columnName, primaryKey);

		}
	}

	public PrimaryKey getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(PrimaryKey primaryKey) {
		this.primaryKey = primaryKey;
	}

	@XmlAttribute(required = true)
	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public Column getActualColumn() {
		return actualColumn;
	}

	@XmlElements({ @XmlElement(name = "sequenceGenerator", type = SequenceGenerator.class), @XmlElement(name = "incrementGenerator", type = IncrementGenerator.class), @XmlElement(name = "tableGenerator", type = TableGenerator.class) })
	public Generator getGenerator() {
		return generator;
	}

	public void setGenerator(Generator generator) {
		this.generator = generator;
	}
}