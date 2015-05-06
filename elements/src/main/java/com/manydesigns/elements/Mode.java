package com.manydesigns.elements;

import org.apache.commons.lang.builder.ToStringBuilder;

public enum Mode {
	CREATE(8, "CREATE"),

	EDIT(0, "EDIT"),

	BULK_EDIT(16, "BULK_EDIT"),

	CREATE_PREVIEW(9, "CREATE_PREVIEW"),

	PREVIEW(1, "PREVIEW"),

	VIEW(2, "VIEW"),

	CREATE_HIDDEN(11, "CREATE_HIDDEN"),

	HIDDEN(3, "HIDDEN");

	private final static int BASE_MODE_MASK = 3;
	private final static int CREATE_MASK = 8;
	private final static int BULK_MASK = 16;

	private final boolean create;
	private final boolean bulk;

	private final boolean edit;
	private final boolean preview;
	private final boolean view;
	private final boolean hidden;

	private final String name;

	Mode(int value, String name) {
		int baseMode = value & BASE_MODE_MASK;
		switch (baseMode) {
		case 0:
			edit = true;
			preview = false;
			view = false;
			hidden = false;
			break;
		case 1:
			edit = false;
			preview = true;
			view = false;
			hidden = false;
			break;
		case 2:
			edit = false;
			preview = false;
			view = true;
			hidden = false;
			break;
		case 3:
			edit = false;
			preview = false;
			view = false;
			hidden = true;
			break;
		default:
			throw new InternalError("Unrecognized mode: " + value);
		}
		create = (value & CREATE_MASK) != 0;
		bulk = (value & BULK_MASK) != 0;
		this.name = name;
	}

	public boolean isCreate() {
		return create;
	}

	public boolean isBulk() {
		return bulk;
	}

	public boolean isEdit() {
		return edit;
	}

	public boolean isPreview() {
		return preview;
	}

	public boolean isView() {
		return view;
	}

	public boolean isHidden() {
		return hidden;
	}

	public boolean isView(boolean insertable, boolean updatable) {
		return view || (!hidden && !create && !updatable) || (!hidden && create && !insertable);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("name", name).append("create", create).append("edit", edit).append("preview", preview).append("view", view).append("hidden", hidden).toString();
	}
}