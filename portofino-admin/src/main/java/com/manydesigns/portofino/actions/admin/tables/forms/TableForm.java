package com.manydesigns.portofino.actions.admin.tables.forms;

import com.manydesigns.elements.annotations.*;
import com.manydesigns.portofino.model.database.Table;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;

public class TableForm extends Table {

	public TableForm(Table copyFrom) {
		try {
			BeanUtils.copyProperties(this, copyFrom);
			actualEntityName = copyFrom.getActualEntityName();
		} catch (Exception e) {
			throw new Error(e);
		}
	}

	public Table copyTo(Table table) {
		try {
			BeanUtils.copyProperties(table, this);
		} catch (Exception e) {
			throw new Error(e);
		}
		return table;
	}

	@Override
	@FieldSize(50)
	@RegExp(value = "(_|$|[a-z]|[A-Z]|[\u0080-\ufffe])(_|$|[a-z]|[A-Z]|[\u0080-\ufffe]|[0-9])*", errorMessage = "invalid.entity.name")
	@LabelI18N("entity.name")
	public String getEntityName() {
		return super.getEntityName();
	}

	@Override
	@FieldSize(50)
	@LabelI18N("java.class")
	public String getJavaClass() {
		return super.getJavaClass();
	}

	@Override
	@FieldSize(50)
	@LabelI18N("short.name")
	public String getShortName() {
		return super.getShortName();
	}

	@Insertable(false)
	@Updatable(false)
	public String getHqlQuery() {
		return "from " + StringUtils.defaultIfEmpty(entityName, actualEntityName);
	}

	@Override
	public String toString() {
		return "Not used.";
	}
}