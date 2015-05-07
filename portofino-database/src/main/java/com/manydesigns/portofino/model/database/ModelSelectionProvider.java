package com.manydesigns.portofino.model.database;

import com.manydesigns.portofino.model.ModelObject;

public interface ModelSelectionProvider extends ModelObject, HasReferences {
	String getName();

	void setName(String name);

	String getToDatabase();

	void setToDatabase(String toDatabase);

}