package com.manydesigns.portofino.model.database;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

import com.manydesigns.portofino.model.Model;

@XmlAccessorType(value = XmlAccessType.NONE)
public class TableGenerator extends Generator {
	protected String table;
	protected String keyColumn;
	protected String keyValue;
	protected String valueColumn;

	public TableGenerator() {
		super();
	}

	public TableGenerator(PrimaryKeyColumn primaryKeyColumn) {
		super(primaryKeyColumn);
	}

	public void reset() {
		super.reset();
	}

	public void init(Model model) {
		super.init(model);
		assert table != null;
		assert keyColumn != null;
		assert keyValue != null;
		assert valueColumn != null;
	}

	@XmlAttribute(required = true)
	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	@XmlAttribute(required = true)
	public String getKeyColumn() {
		return keyColumn;
	}

	public void setKeyColumn(String keyColumn) {
		this.keyColumn = keyColumn;
	}

	@XmlAttribute(required = true)
	public String getKeyValue() {
		return keyValue;
	}

	public void setKeyValue(String keyValue) {
		this.keyValue = keyValue;
	}

	@XmlAttribute(required = true)
	public String getValueColumn() {
		return valueColumn;
	}

	public void setValueColumn(String valueColumn) {
		this.valueColumn = valueColumn;
	}
}