package com.manydesigns.portofino.model.database;

import com.manydesigns.portofino.model.Model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.NONE)
public class IncrementGenerator extends Generator {
	public IncrementGenerator() {
		super();
	}

	public IncrementGenerator(PrimaryKeyColumn primaryKeyColumn) {
		super(primaryKeyColumn);
	}

	public void reset() {
		super.reset();
	}

	public void init(Model model) {
		super.init(model);
	}
}