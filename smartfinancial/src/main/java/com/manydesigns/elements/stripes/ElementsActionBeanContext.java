package com.manydesigns.elements.stripes;

import com.manydesigns.elements.servlet.ServletUtils;
import net.sourceforge.stripes.action.ActionBeanContext;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

public class ElementsActionBeanContext extends ActionBeanContext {
	protected String actionPath;

	@Override
	public void setRequest(HttpServletRequest request) {
		super.setRequest(request);
		String actionPath = (String) request.getAttribute(RequestDispatcher.INCLUDE_SERVLET_PATH);
		if (actionPath == null) {
			actionPath = ServletUtils.getPath(request);
		}
		this.actionPath = actionPath;
	}

	public String getActionPath() {
		return actionPath;
	}

	public void setActionPath(String actionPath) {
		this.actionPath = actionPath;
	}
}