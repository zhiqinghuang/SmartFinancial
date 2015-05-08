package com.manydesigns.portofino.model;

public abstract class ModelObjectVisitor {
	public void visitNodeBeforeChildren(ModelObject node) {
	}

	public void visitNodeAfterChildren(ModelObject node) {
	}

	public void visit(ModelObject node) {
		visitNodeBeforeChildren(node);
		node.visitChildren(this);
		visitNodeAfterChildren(node);
	}
}