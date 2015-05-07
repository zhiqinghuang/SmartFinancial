package com.manydesigns.portofino.model;

import javax.xml.bind.Unmarshaller;

public interface ModelObject {
	void afterUnmarshal(Unmarshaller u, Object parent);

	void reset();

	void init(Model model);

	void link(Model model);

	void visitChildren(ModelObjectVisitor visitor);
}