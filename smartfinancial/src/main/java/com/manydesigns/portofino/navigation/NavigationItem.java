package com.manydesigns.portofino.navigation;

import java.util.ArrayList;
import java.util.List;

public class NavigationItem {

	protected final String title;
	protected final String description;
	protected final String path;
	protected final boolean inPath;
	protected final boolean selected;
	protected final boolean ghost;

	protected final List<NavigationItem> childNavigationItems;

	public NavigationItem(String title, String description, String path, boolean inPath, boolean selected, boolean ghost) {
		this.title = title;
		this.description = description;
		this.path = path;
		this.inPath = inPath;
		this.selected = selected;
		this.ghost = ghost;
		childNavigationItems = new ArrayList<NavigationItem>();
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public String getPath() {
		return path;
	}

	public boolean isInPath() {
		return inPath;
	}

	public boolean isSelected() {
		return selected;
	}

	public boolean isGhost() {
		return ghost;
	}

	public List<NavigationItem> getChildNavigationItems() {
		return childNavigationItems;
	}
}