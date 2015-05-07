package com.manydesigns.portofino.menu;

import java.util.ArrayList;
import java.util.List;

public class MenuGroup extends MenuItem {
	public final List<MenuLink> menuLinks = new ArrayList<MenuLink>();

	public MenuGroup(String id, String icon, String title, double order) {
		super(id, icon, title, order);
	}

	public List<MenuLink> getMenuLinks() {
		return menuLinks;
	}
}