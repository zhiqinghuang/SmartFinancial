package com.manydesigns.portofino.dispatcher;

import com.manydesigns.elements.util.Util;
import com.manydesigns.portofino.pages.NavigationRoot;
import net.sourceforge.stripes.action.ActionBean;

public class Dispatch {

	protected final PageInstance[] pageInstancePath;

	public Dispatch(PageInstance... pageInstancePath) {
		this.pageInstancePath = pageInstancePath;
	}

	public PageInstance[] getPageInstancePath() {
		return pageInstancePath;
	}

	public PageInstance[] getPageInstancePath(int startIndex) {
		return Util.copyOfRange(pageInstancePath, startIndex, pageInstancePath.length);
	}

	public PageInstance getRootPageInstance() {
		return pageInstancePath[0];
	}

	public PageInstance getLastPageInstance() {
		return pageInstancePath[pageInstancePath.length - 1];
	}

	public PageInstance getPageInstance(int index) {
		if (index >= 0) {
			return getPageInstancePath()[index];
		} else {
			return getPageInstancePath()[getPageInstancePath().length + index];
		}
	}

	public int getClosestSubtreeRootIndex() {
		PageInstance[] path = getPageInstancePath();
		for (int i = path.length - 1; i > 0; i--) {
			if (path[i].getPage().getActualNavigationRoot() != NavigationRoot.INHERIT) {
				return i;
			}
		}
		return 0;
	}

	public Class<? extends ActionBean> getActionBeanClass() {
		return getLastPageInstance().getActionClass();
	}
}