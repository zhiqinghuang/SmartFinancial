package com.manydesigns.portofino.actions.admin.page.forms;

import com.manydesigns.elements.annotations.Label;
import com.manydesigns.elements.annotations.RegExp;
import com.manydesigns.elements.annotations.Required;
import com.manydesigns.elements.annotations.Select;
import com.manydesigns.elements.options.DisplayMode;
import com.manydesigns.portofino.pages.Page;

public class NewPage extends Page {
	protected String actionClassName;
	protected String insertPositionName;
	protected String fragment;

	@Label("Page type")
	@Required
	public String getActionClassName() {
		return actionClassName;
	}

	public void setActionClassName(String actionClassName) {
		this.actionClassName = actionClassName;
	}

	@Label("Where")
	@Select(displayMode = DisplayMode.RADIO)
	@Required
	public String getInsertPositionName() {
		return insertPositionName;
	}

	public void setInsertPositionName(String insertPositionName) {
		this.insertPositionName = insertPositionName;
	}

	@RegExp(value = "[a-zA-Z0-9][a-zA-Z0-9_\\-]*", errorMessage = "invalid.fragment.only.letters.numbers.etc.are.allowed")
	@Required
	public String getFragment() {
		return fragment;
	}

	public void setFragment(String fragment) {
		this.fragment = fragment;
	}
}