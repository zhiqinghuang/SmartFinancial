package com.manydesigns.elements.options;

import java.util.Map;

public interface SelectionModel {
	SelectionProvider getSelectionProvider();

	String getName();

	Object getValue(int index);

	void setValue(int index, Object value);

	String getLabelSearch(int index);

	void setLabelSearch(int index, String labelSearch);

	Map<Object, Option> getOptions(int index);

	String getOption(int index, Object value, boolean includeInactive);

	public static class Option {
		public final Object value;
		public final String label;
		public final boolean active;

		public Option(Object value, String label, boolean active) {
			this.value = value;
			this.label = label;
			this.active = active;
		}
	}
}