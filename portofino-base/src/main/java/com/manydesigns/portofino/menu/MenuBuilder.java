package com.manydesigns.portofino.menu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MenuBuilder {
	public final List<MenuAppender> menuAppenders = new ArrayList<MenuAppender>();

	public Menu build() {
		Menu menu = new Menu();
		for (MenuAppender appender : menuAppenders) {
			appender.append(menu);
		}
		Collections.sort(menu.items, new MenuItemComparator());
		for (MenuItem menuItem : menu.items) {
			if (menuItem instanceof MenuGroup) {
				Collections.sort(((MenuGroup) menuItem).menuLinks, new MenuItemComparator());
			}
		}
		return menu;
	}

	public List<MenuAppender> getMenuAppenders() {
		return menuAppenders;
	}

	public static class MenuItemComparator implements Comparator<MenuItem> {
		@Override
		public int compare(MenuItem mi1, MenuItem mi2) {
			return Double.compare(mi1.order, mi2.order);
		}
	}
}