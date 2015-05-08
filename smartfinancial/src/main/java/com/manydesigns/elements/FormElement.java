package com.manydesigns.elements;

import java.util.List;

public interface FormElement extends Element {
	Mode getMode();

	String getLabel();

	void setLabel(String label);

	boolean isForceNewRow();

	void setForceNewRow(boolean forceNewRow);

	int getColSpan();

	void setColSpan(int colSpan);

	String getHelp();

	void setHelp(String help);

	List<String> getErrors();

	boolean hasRequiredFields();
}