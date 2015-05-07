package com.manydesigns.portofino.model.database;

import com.manydesigns.portofino.model.Model;
import com.manydesigns.portofino.model.ModelObject;
import com.manydesigns.portofino.model.ModelObjectVisitor;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(value = XmlAccessType.NONE)
public class Reference implements ModelObject {
	protected HasReferences owner;
	protected String fromColumn;
	protected String toColumn;

	protected Column actualFromColumn;
	protected Column actualToColumn;

	public Reference() {
	}

	public Reference(HasReferences owner) {
		this.owner = owner;
	}

	public void afterUnmarshal(Unmarshaller u, Object parent) {
		this.owner = (HasReferences) parent;
	}

	public void reset() {
		actualFromColumn = null;
		actualToColumn = null;
	}

	public void init(Model model) {
		assert owner != null;
		assert fromColumn != null;
	}

	public void link(Model model) {
		actualFromColumn = DatabaseLogic.findColumnByName(owner.getFromTable(), fromColumn);
		if (actualFromColumn == null) {
			throw new InternalError("Cannot resolve column: " + fromColumn);
		}

		Table toTable = owner.getToTable();
		if (toTable != null) {
			actualToColumn = DatabaseLogic.findColumnByName(toTable, toColumn);
		}
	}

	public void visitChildren(ModelObjectVisitor visitor) {
	}

	public HasReferences getOwner() {
		return owner;
	}

	public void setOwner(HasReferences owner) {
		this.owner = owner;
	}

	@XmlAttribute(required = true)
	public String getFromColumn() {
		return fromColumn;
	}

	public void setFromColumn(String fromColumn) {
		this.fromColumn = fromColumn;
	}

	@XmlAttribute(required = true)
	public String getToColumn() {
		return toColumn;
	}

	public void setToColumn(String toColumn) {
		this.toColumn = toColumn;
	}

	public Column getActualFromColumn() {
		return actualFromColumn;
	}

	public Column getActualToColumn() {
		return actualToColumn;
	}
}