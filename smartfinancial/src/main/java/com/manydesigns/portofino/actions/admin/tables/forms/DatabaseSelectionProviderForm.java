package com.manydesigns.portofino.actions.admin.tables.forms;

import com.manydesigns.elements.annotations.FieldSize;
import com.manydesigns.elements.annotations.Multiline;
import com.manydesigns.elements.annotations.Required;
import com.manydesigns.portofino.model.database.DatabaseSelectionProvider;
import org.apache.commons.beanutils.BeanUtils;

public class DatabaseSelectionProviderForm extends DatabaseSelectionProvider {
	protected String columns;

	public DatabaseSelectionProviderForm(DatabaseSelectionProvider copyFrom) {
		try {
			BeanUtils.copyProperties(this, copyFrom);
		} catch (Exception e) {
			throw new Error(e);
		}
	}

	public DatabaseSelectionProvider copyTo(DatabaseSelectionProvider dsp) {
		try {
			BeanUtils.copyProperties(dsp, this);
		} catch (Exception e) {
			throw new Error(e);
		}
		return dsp;
	}

	@Override
	@Required
	@FieldSize(50)
	public String getName() {
		return super.getName();
	}

	@Override
	@Required
	public String getToDatabase() {
		return super.getToDatabase();
	}

	@Override
	@Multiline
	public String getHql() {
		return super.getHql();
	}

	@Override
	@Multiline
	public String getSql() {
		return super.getSql();
	}

	@FieldSize(75)
	@Required
	public String getColumns() {
		return columns;
	}

	public void setColumns(String columns) {
		this.columns = columns;
	}
}