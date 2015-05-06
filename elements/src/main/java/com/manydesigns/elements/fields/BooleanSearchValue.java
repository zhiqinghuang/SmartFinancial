package com.manydesigns.elements.fields;

public enum BooleanSearchValue {
	TRUE("true", "elements.Yes"), FALSE("false", "elements.No"), ANY("", "elements.Any"), NULL("-", "elements.Undefined");

	private final String stringValue;

	private final String labelI18N;

	BooleanSearchValue(String stringValue, String labelI18N) {
		this.stringValue = stringValue;
		this.labelI18N = labelI18N;
	}

	public String getStringValue() {
		return stringValue;
	}

	public String getLabelI18N() {
		return labelI18N;
	}
}