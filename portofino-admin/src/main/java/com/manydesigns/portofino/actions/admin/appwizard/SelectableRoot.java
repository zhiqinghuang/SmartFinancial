package com.manydesigns.portofino.actions.admin.appwizard;

import com.manydesigns.elements.annotations.Label;
import com.manydesigns.elements.annotations.Updatable;

public class SelectableRoot {
	@Updatable(false)
	public String tableName;
	@Label("")
	public boolean selected;

	public SelectableRoot(String tableName, boolean selected) {
		this.tableName = tableName;
		this.selected = selected;
	}

	public SelectableRoot() {
	}
}