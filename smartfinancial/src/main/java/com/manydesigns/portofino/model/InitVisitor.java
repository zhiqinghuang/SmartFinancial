package com.manydesigns.portofino.model;

import com.manydesigns.portofino.model.Model;
import com.manydesigns.portofino.model.ModelObject;
import com.manydesigns.portofino.model.ModelObjectVisitor;

public class InitVisitor extends ModelObjectVisitor {

	private Model model;

	public InitVisitor(Model model) {
		this.model = model;
	}

	@Override
	public void visitNodeBeforeChildren(ModelObject node) {
		node.init(model);
	}
}