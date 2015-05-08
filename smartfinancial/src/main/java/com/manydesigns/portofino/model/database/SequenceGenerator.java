package com.manydesigns.portofino.model.database;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

import com.manydesigns.portofino.model.Model;

@XmlAccessorType(value = XmlAccessType.NONE)
public class SequenceGenerator extends Generator {
	protected String name;

	public SequenceGenerator() {
		super();
	}

	public SequenceGenerator(PrimaryKeyColumn primaryKeyColumn) {
		super(primaryKeyColumn);
	}

	public void reset() {
		super.reset();
	}

	public void init(Model model) {
		super.init(model);
		assert name != null;
	}

	@XmlAttribute(required = true)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}