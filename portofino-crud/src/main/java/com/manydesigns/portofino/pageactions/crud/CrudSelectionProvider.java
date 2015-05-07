package com.manydesigns.portofino.pageactions.crud;

import com.manydesigns.elements.options.SelectionProvider;

public class CrudSelectionProvider {
	protected final SelectionProvider selectionProvider;
	protected final String[] fieldNames;

	protected final String createNewValueHref;
	protected final String createNewValueText;

	public CrudSelectionProvider(SelectionProvider selectionProvider, String[] fieldNames, String createNewValueHref, String createNewValueText) {
		this.selectionProvider = selectionProvider;
		this.fieldNames = fieldNames;
		this.createNewValueHref = createNewValueHref;
		this.createNewValueText = createNewValueText;
	}

	public SelectionProvider getSelectionProvider() {
		return selectionProvider;
	}

	public String[] getFieldNames() {
		return fieldNames;
	}

	public String getCreateNewValueHref() {
		return createNewValueHref;
	}

	public String getCreateNewValueText() {
		return createNewValueText;
	}
}