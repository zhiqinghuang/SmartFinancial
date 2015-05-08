package com.manydesigns.portofino.model.database;

import com.manydesigns.portofino.model.Model;
import com.manydesigns.portofino.util.Pair;
import org.apache.commons.lang.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import java.text.MessageFormat;

@XmlAccessorType(value = XmlAccessType.NONE)
public class ForeignKey extends DatabaseSelectionProvider implements HasReferences {
	protected String onUpdate;
	protected String onDelete;
	protected String toSchema;

	protected String manyPropertyName;
	protected String onePropertyName;
	protected String toTableName;

	protected String actualManyPropertyName;
	protected String actualOnePropertyName;
	protected Table toTable;

	public static final Logger logger = LoggerFactory.getLogger(ForeignKey.class);

	public ForeignKey() {
	}

	public ForeignKey(Table fromTable) {
		this();
		this.fromTable = fromTable;
	}

	@Override
	public String getQualifiedName() {
		return MessageFormat.format("{0}${1}", fromTable.getQualifiedName(), name);
	}

	@Override
	public void reset() {
		super.reset();
		toTable = null;
		actualManyPropertyName = null;
		actualOnePropertyName = null;
	}

	@Override
	public void init(Model model) {
		super.init(model);

		assert fromTable != null;
		assert name != null;
		assert toSchema != null;
		assert toTableName != null;

		if (references.isEmpty()) {
			throw new Error(MessageFormat.format("Foreign key {0} has no references", getQualifiedName()));
		}

	}

	@Override
	public void link(Model model) {
		super.link(model);
		toTable = DatabaseLogic.findTableByName(model, toDatabase, toSchema, toTableName);
		if (toTable != null) {
			// wire up Table.oneToManyRelationships
			toTable.getOneToManyRelationships().add(this);
			hql = "from " + toTable.getActualEntityName();

			actualManyPropertyName = (manyPropertyName == null) ? DatabaseLogic.getUniquePropertyName(toTable, DatabaseLogic.normalizeName(name)) : manyPropertyName;

		} else {
			logger.warn("Cannot find destination table '{}'", Table.composeQualifiedName(toDatabase, toSchema, toTableName));

			actualManyPropertyName = (manyPropertyName == null) ? DatabaseLogic.normalizeName(name) : manyPropertyName;
		}

		actualOnePropertyName = (onePropertyName == null) ? DatabaseLogic.getUniquePropertyName(fromTable, DatabaseLogic.normalizeName(name)) : onePropertyName;
	}

	public Reference findReferenceByColumnNamePair(Pair<String> columnNamePair) {
		for (Reference reference : references) {
			if (ObjectUtils.equals(reference.getFromColumn(), columnNamePair.left) && ObjectUtils.equals(reference.getToColumn(), columnNamePair.right)) {
				return reference;
			}
		}
		return null;
	}

	public String getFromDatabaseName() {
		return fromTable.getDatabaseName();
	}

	public String getFromSchemaName() {
		return fromTable.getSchemaName();
	}

	public String getFromTableName() {
		return fromTable.getTableName();
	}

	@XmlAttribute(required = true)
	public String getOnUpdate() {
		return onUpdate;
	}

	public void setOnUpdate(String onUpdate) {
		this.onUpdate = onUpdate;
	}

	@XmlAttribute(required = true)
	public String getOnDelete() {
		return onDelete;
	}

	public void setOnDelete(String onDelete) {
		this.onDelete = onDelete;
	}

	@XmlAttribute(required = false)
	public String getManyPropertyName() {
		return manyPropertyName;
	}

	public void setManyPropertyName(String manyPropertyName) {
		this.manyPropertyName = manyPropertyName;
	}

	@XmlAttribute(required = false)
	public String getOnePropertyName() {
		return onePropertyName;
	}

	public void setOnePropertyName(String onePropertyName) {
		this.onePropertyName = onePropertyName;
	}

	public String getActualManyPropertyName() {
		return actualManyPropertyName;
	}

	public String getActualOnePropertyName() {
		return actualOnePropertyName;
	}

	@Override
	@XmlTransient
	public String getHql() {
		return hql;
	}

	@XmlAttribute(required = true)
	public String getToSchema() {
		return toSchema;
	}

	public void setToSchema(String toSchema) {
		this.toSchema = toSchema;
	}

	public Table getToTable() {
		return toTable;
	}

	public void setToTable(Table toTable) {
		this.toTable = toTable;
	}

	@XmlAttribute(name = "toTable")
	public String getToTableName() {
		return toTableName;
	}

	public void setToTableName(String toTableName) {
		this.toTableName = toTableName;
	}

	@Override
	public String toString() {
		return MessageFormat.format("foreign key {0}", getQualifiedName());
	}
}