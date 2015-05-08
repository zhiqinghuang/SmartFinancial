package com.manydesigns.portofino.actions.admin.database.forms;

import com.manydesigns.elements.annotations.Status;
import com.manydesigns.portofino.model.database.ConnectionProvider;

public class ConnectionProviderTableForm {
	public String databaseName;
	@Status(red = { ConnectionProvider.STATUS_ERROR }, amber = { ConnectionProvider.STATUS_DISCONNECTED }, green = { ConnectionProvider.STATUS_CONNECTED })
	public String status;
	public String description;

	public ConnectionProviderTableForm() {
	}

	public ConnectionProviderTableForm(String databaseName, String description, String status) {
		this.databaseName = databaseName;
		this.status = status;
		this.description = description;
	}
}