package com.manydesigns.portofino.breadcrumbs;

import com.manydesigns.portofino.dispatcher.Dispatch;
import com.manydesigns.portofino.dispatcher.PageInstance;
import com.manydesigns.portofino.pages.NavigationRoot;
import com.manydesigns.portofino.pages.Page;

import java.util.ArrayList;
import java.util.List;

public class Breadcrumbs {

	protected final Dispatch dispatch;
	protected final List<BreadcrumbItem> items;

	public Breadcrumbs(Dispatch dispatch) {
		this(dispatch, dispatch.getPageInstancePath().length);
	}

	public Breadcrumbs(Dispatch dispatch, int upto) {
		this.dispatch = dispatch;
		items = new ArrayList<BreadcrumbItem>();

		StringBuilder sb = new StringBuilder();
		int start = dispatch.getClosestSubtreeRootIndex();
		PageInstance pageInstance = dispatch.getPageInstance(start);
		PageInstance parentPageInstance = pageInstance.getParent();
		if (parentPageInstance == null) {
			start++;
		} else {
			sb.append(parentPageInstance.getPath());
		}
		for (int i = start; i < upto; i++) {
			PageInstance current = dispatch.getPageInstancePath()[i];
			sb.append("/");
			Page page = current.getPage();
			sb.append(current.getName());
			BreadcrumbItem item = new BreadcrumbItem(sb.toString(), current.getPage().getTitle(), current.getPage().getDescription());
			if (page.getActualNavigationRoot() != NavigationRoot.GHOST_ROOT) {
				items.add(item);
			}
			if (!current.getParameters().isEmpty()) {
				for (String param : current.getParameters()) {
					sb.append("/").append(param);
				}
				if (page.getActualNavigationRoot() != NavigationRoot.GHOST_ROOT) {
					String title = current.getTitle();
					String description = current.getDescription();
					BreadcrumbItem item2 = new BreadcrumbItem(sb.toString(), title, description);
					items.add(item2);
				}
			}
		}
	}

	public List<BreadcrumbItem> getItems() {
		return items;
	}
}