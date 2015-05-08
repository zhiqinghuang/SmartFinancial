package com.manydesigns.portofino.menu;

public class MenuLink extends MenuItem {
	public final String link;

	public MenuLink(String id, String icon, String title, String link, double order) {
		super(id, icon, title, order);
		this.link = link;
	}

	public String getLink() {
		return link;
	}
}