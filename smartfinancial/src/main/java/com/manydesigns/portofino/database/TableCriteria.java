package com.manydesigns.portofino.database;

import com.manydesigns.elements.fields.search.BaseCriteria;
import com.manydesigns.elements.fields.search.TextMatchMode;
import com.manydesigns.elements.reflection.PropertyAccessor;
import com.manydesigns.portofino.model.database.Table;

public class TableCriteria extends BaseCriteria {
	private static final long serialVersionUID = 4184840106189991072L;
	protected final Table table;

	public TableCriteria(Table table) {
		super();
		this.table = table;
	}

	public Table getTable() {
		return table;
	}

	@Override
	public TableCriteria eq(PropertyAccessor accessor, Object value) {
		return (TableCriteria) super.eq(accessor, value);
	}

	@Override
	public TableCriteria in(PropertyAccessor accessor, Object[] values) {
		return (TableCriteria) super.in(accessor, values);
	}

	@Override
	public TableCriteria ne(PropertyAccessor accessor, Object value) {
		return (TableCriteria) super.ne(accessor, value);
	}

	@Override
	public TableCriteria between(PropertyAccessor accessor, Object min, Object max) {
		return (TableCriteria) super.between(accessor, min, max);
	}

	@Override
	public TableCriteria gt(PropertyAccessor accessor, Object value) {
		return (TableCriteria) super.gt(accessor, value);
	}

	@Override
	public TableCriteria ge(PropertyAccessor accessor, Object value) {
		return (TableCriteria) super.ge(accessor, value);
	}

	@Override
	public TableCriteria lt(PropertyAccessor accessor, Object value) {
		return (TableCriteria) super.lt(accessor, value);
	}

	@Override
	public TableCriteria le(PropertyAccessor accessor, Object value) {
		return (TableCriteria) super.le(accessor, value);
	}

	@Override
	public TableCriteria like(PropertyAccessor accessor, String value, TextMatchMode textMatchMode) {
		return (TableCriteria) super.like(accessor, value, textMatchMode);
	}

	@Override
	public TableCriteria ilike(PropertyAccessor accessor, String value, TextMatchMode textMatchMode) {
		return (TableCriteria) super.ilike(accessor, value, textMatchMode);
	}

	@Override
	public TableCriteria isNull(PropertyAccessor accessor) {
		return (TableCriteria) super.isNull(accessor);
	}

	@Override
	public TableCriteria isNotNull(PropertyAccessor accessor) {
		return (TableCriteria) super.isNotNull(accessor);
	}

	@Override
	public TableCriteria orderBy(PropertyAccessor accessor, String direction) {
		return (TableCriteria) super.orderBy(accessor, direction);
	}
}