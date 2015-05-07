package com.manydesigns.portofino.actions.admin.database.forms;

import com.manydesigns.elements.annotations.Label;
import com.manydesigns.elements.annotations.Updatable;

public class SelectableSchema {
	@Updatable(false)
	public String schemaName;
	@Label("")
	public boolean selected;

	public SelectableSchema(String schemaName, boolean selected) {
		this.schemaName = schemaName;
		this.selected = selected;
	}

	public SelectableSchema() {
	}
}