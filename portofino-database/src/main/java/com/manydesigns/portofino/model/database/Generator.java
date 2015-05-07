package com.manydesigns.portofino.model.database;

import com.manydesigns.portofino.model.Model;
import com.manydesigns.portofino.model.ModelObject;
import com.manydesigns.portofino.model.ModelObjectVisitor;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(value = XmlAccessType.NONE)
public abstract class Generator implements ModelObject {
	protected PrimaryKeyColumn primaryKeyColumn;

	public Generator() {
	}

	protected Generator(PrimaryKeyColumn primaryKeyColumn) {
		this.primaryKeyColumn = primaryKeyColumn;
	}

	public PrimaryKeyColumn getPrimaryKeyColumn() {
		return primaryKeyColumn;
	}

	public void setPrimaryKeyColumn(PrimaryKeyColumn primaryKeyColumn) {
		this.primaryKeyColumn = primaryKeyColumn;
	}

	public void afterUnmarshal(Unmarshaller u, Object parent) {
		primaryKeyColumn = (PrimaryKeyColumn) parent;
	}

	public void reset() {
	}

	public void init(Model model) {
		assert primaryKeyColumn != null;
	}

	public void link(Model model) {
	}

	public void visitChildren(ModelObjectVisitor visitor) {
	}
}