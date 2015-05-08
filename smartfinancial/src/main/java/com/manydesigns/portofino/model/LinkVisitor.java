package com.manydesigns.portofino.model;

import com.manydesigns.portofino.model.Model;
import com.manydesigns.portofino.model.ModelObject;
import com.manydesigns.portofino.model.ModelObjectVisitor;

public class LinkVisitor extends ModelObjectVisitor {

	private Model model;

	public LinkVisitor(Model model) {
		this.model = model;
	}

	@Override
	public void visitNodeBeforeChildren(ModelObject node) {
		node.link(model);
	}
}