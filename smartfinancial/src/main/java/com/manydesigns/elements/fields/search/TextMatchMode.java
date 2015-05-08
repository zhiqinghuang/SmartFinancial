package com.manydesigns.elements.fields.search;

public enum TextMatchMode {
	CONTAINS("", "elements.search.text.match.mode.contains"), EQUALS("equals", "elements.search.text.match.mode.equals"), STARTS_WITH("starts", "elements.search.text.match.mode.starts.with"), ENDS_WITH("ends", "elements.search.text.match.mode.ends.with");

	private final String stringValue;
	private final String i18nKey;

	TextMatchMode(String stringValue, String i18nKey) {
		this.stringValue = stringValue;
		this.i18nKey = i18nKey;
	}

	public String getStringValue() {
		return stringValue;
	}

	public String getI18nKey() {
		return i18nKey;
	}
}
