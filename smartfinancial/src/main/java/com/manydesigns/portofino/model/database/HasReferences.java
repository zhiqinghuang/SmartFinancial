package com.manydesigns.portofino.model.database;

import java.util.List;

public interface HasReferences {

	List<Reference> getReferences();

	Table getFromTable();

	void setFromTable(Table fromTable);

	Table getToTable();

}