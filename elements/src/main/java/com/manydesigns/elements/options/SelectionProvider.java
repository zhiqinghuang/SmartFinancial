package com.manydesigns.elements.options;

public interface SelectionProvider {
	String getName();

	int getFieldCount();

	DisplayMode getDisplayMode();

	SearchDisplayMode getSearchDisplayMode();

	SelectionModel createSelectionModel();

	void ensureActive(Object... values);

	String getCreateNewValueHref();

	String getCreateNewValueText();
}