package com.manydesigns.portofino.actions.admin.page.forms;

import com.manydesigns.elements.annotations.Label;
import com.manydesigns.elements.annotations.RegExp;
import com.manydesigns.elements.annotations.Required;

public class CopyPage {
	@RegExp(value = "[a-zA-Z0-9][a-zA-Z0-9_\\-]*", errorMessage = "invalid.fragment.only.letters.numbers.etc.are.allowed")
	@Required
	@Label("Fragment")
	public String fragment;

	@Required
	@Label("Copy to")
	public String destinationPagePath;

}