package com.manydesigns.portofino.menu;

public abstract class MenuItem {
	public final String id;
	public final String icon;
	public final String label;
	public final double order;

	public MenuItem(String id, String icon, String label, double order) {
		this.id = id;
		this.icon = icon;
		this.label = label;
		this.order = order;
	}

	public String getIcon() {
		return icon;
	}

	public String getLabel() {
		return label;
	}
}