package com.manydesigns.portofino.navigation;

import com.manydesigns.portofino.dispatcher.Dispatch;
import com.manydesigns.portofino.dispatcher.DispatcherLogic;
import com.manydesigns.portofino.dispatcher.PageInstance;
import com.manydesigns.portofino.logic.SecurityLogic;
import com.manydesigns.portofino.pages.*;
import com.manydesigns.portofino.security.AccessLevel;
import org.apache.commons.configuration.Configuration;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Navigation {

	protected final Configuration configuration;
	protected final Dispatch dispatch;
	protected final Subject subject;
	protected final boolean skipPermissions;
	protected NavigationItem rootNavigationItem;

	public static final Logger logger = LoggerFactory.getLogger(Navigation.class);

	public Navigation(Configuration configuration, Dispatch dispatch, Subject subject, boolean skipPermissions) {
		this.configuration = configuration;
		this.dispatch = dispatch;
		this.subject = subject;
		this.skipPermissions = skipPermissions;

		buildTree();
	}

	private void buildTree() {
		int rootPageIndex = dispatch.getClosestSubtreeRootIndex();
		PageInstance[] pageInstances = dispatch.getPageInstancePath(rootPageIndex);
		if (pageInstances == null || pageInstances.length == 0) {
			return;
		}
		PageInstance rootPageInstance = pageInstances[0];
		String prefix = "";
		if (rootPageIndex > 0) {
			prefix += rootPageInstance.getParent().getPath() + "/" + rootPageInstance.getName();
		}
		boolean rootSelected = pageInstances.length == 1;
		Page rootPage = rootPageInstance.getPage();
		boolean rootGhost = rootPage.getActualNavigationRoot() == NavigationRoot.GHOST_ROOT;
		rootNavigationItem = new NavigationItem(rootPageInstance.getTitle(), rootPageInstance.getDescription(), prefix, true, rootSelected, rootGhost);
		LinkedList<Page> pages = new LinkedList<Page>();
		PageInstance[] allInstances = dispatch.getPageInstancePath();
		for (int i = 0; i <= rootPageIndex; i++) {
			pages.add(allInstances[i].getPage());
		}
		Permissions basePermissions = SecurityLogic.calculateActualPermissions(new Permissions(), pages);
		pages.clear();
		List<ChildPage> childPages;
		NavigationItem currentNavigationItem = rootNavigationItem;
		for (int i = 0, pageInstancesLength = pageInstances.length; i < pageInstancesLength; i++) {
			PageInstance current = pageInstances[i];
			PageInstance next;
			if (i < pageInstancesLength - 1) {
				next = pageInstances[i + 1];
			} else {
				next = null;
			}

			Layout layout = current.getLayout();
			if (layout != null) {
				childPages = layout.getChildPages();
			} else {
				childPages = new ArrayList<ChildPage>();
			}

			List<NavigationItem> currentChildNavigationItems = currentNavigationItem.getChildNavigationItems();
			prefix = currentNavigationItem.getPath() + "/";
			for (String param : current.getParameters()) {
				prefix += param + "/";
			}
			currentNavigationItem = null;
			for (ChildPage childPage : childPages) {
				File pageDir = current.getChildPageDirectory(childPage.getName());
				Page page;
				try {
					page = DispatcherLogic.getPage(pageDir);
				} catch (Exception e) {
					logger.warn("Nonexisting child page: " + pageDir);
					logger.debug("Detailed explanation", e);
					continue;
				}
				String path = prefix + childPage.getName();
				boolean inPath = false;
				boolean selected = false;
				String description = page.getDescription();
				if (next != null) {
					if (next.getName().equals(childPage.getName())) {
						inPath = true;
						selected = (i == pageInstancesLength - 2);
						description = next.getDescription();
					}
				}
				pages.add(page);
				if (!skipPermissions) {
					Permissions permissions = SecurityLogic.calculateActualPermissions(basePermissions, pages);
					if (!SecurityLogic.hasPermissions(configuration, permissions, subject, AccessLevel.VIEW)) {
						pages.removeLast();
						continue;
					}
				}
				pages.removeLast();
				if (!childPage.isShowInNavigation() && !inPath) {
					continue;
				}
				NavigationItem childNavigationItem = new NavigationItem(page.getTitle(), description, path, inPath, selected, false);
				currentChildNavigationItems.add(childNavigationItem);
				if (inPath) {
					currentNavigationItem = childNavigationItem;
				}
			}
			if (currentNavigationItem == null && next != null) {
				boolean selected = (i == pageInstancesLength - 2);
				String path = prefix + next.getName();
				currentNavigationItem = new NavigationItem(next.getTitle(), next.getDescription(), path, true, selected, false);
				currentChildNavigationItems.add(currentNavigationItem);
			}

			if (next != null) {
				pages.add(next.getPage());
			}
		}
	}

	protected boolean isSelected(PageInstance pageInstance) {
		return pageInstance == dispatch.getLastPageInstance();
	}

	public Dispatch getDispatch() {
		return dispatch;
	}

	public Subject getSubject() {
		return subject;
	}

	public boolean isSkipPermissions() {
		return skipPermissions;
	}

	public NavigationItem getRootNavigationItem() {
		return rootNavigationItem;
	}
}