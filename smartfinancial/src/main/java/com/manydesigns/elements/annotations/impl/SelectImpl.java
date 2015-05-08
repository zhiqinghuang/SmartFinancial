package com.manydesigns.elements.annotations.impl;

import com.manydesigns.elements.annotations.Select;
import com.manydesigns.elements.options.DisplayMode;
import com.manydesigns.elements.options.SearchDisplayMode;

import java.lang.annotation.Annotation;

public class SelectImpl implements Select {
	private DisplayMode displayMode;
	private SearchDisplayMode searchDisplayMode;
	private String[] values;
	private String[] labels;
	private boolean nullOption;

	public SelectImpl(DisplayMode displayMode, SearchDisplayMode searchDisplayMode, String[] values, String[] labels, boolean nullOption) {
		this.displayMode = displayMode;
		this.searchDisplayMode = searchDisplayMode;
		this.values = values;
		this.labels = labels;
		this.nullOption = nullOption;
	}

	public DisplayMode displayMode() {
		return displayMode;
	}

	public SearchDisplayMode searchDisplayMode() {
		return searchDisplayMode;
	}

	public String[] values() {
		return values;
	}

	public String[] labels() {
		return labels;
	}

	public boolean nullOption() {
		return nullOption;
	}

	public Class<? extends Annotation> annotationType() {
		return Select.class;
	}
}