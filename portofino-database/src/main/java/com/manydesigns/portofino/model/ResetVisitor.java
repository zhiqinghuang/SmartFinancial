package com.manydesigns.portofino.model;

public class ResetVisitor extends ModelObjectVisitor {
	@Override
	public void visitNodeBeforeChildren(ModelObject node) {
		node.reset();
	}
}