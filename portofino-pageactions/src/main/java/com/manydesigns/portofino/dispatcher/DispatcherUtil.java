package com.manydesigns.portofino.dispatcher;

import com.manydesigns.elements.ElementsThreadLocals;
import com.manydesigns.elements.stripes.ElementsActionBeanContext;
import com.manydesigns.portofino.RequestAttributes;
import com.manydesigns.portofino.modules.BaseModule;
import com.manydesigns.portofino.modules.PageactionsModule;
import com.manydesigns.portofino.stripes.AbstractActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import org.apache.commons.configuration.Configuration;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.File;

public class DispatcherUtil {

	public static Dispatcher get(HttpServletRequest request) {
		if (request == null) {
			return null;
		}
		return (Dispatcher) request.getAttribute(RequestAttributes.DISPATCHER);
	}

	public static Dispatcher install(HttpServletRequest request) {
		ServletContext servletContext = ElementsThreadLocals.getServletContext();
		Configuration configuration = (Configuration) servletContext.getAttribute(BaseModule.PORTOFINO_CONFIGURATION);
		File pagesDir = (File) servletContext.getAttribute(PageactionsModule.PAGES_DIRECTORY);

		Dispatcher dispatcher = new Dispatcher(configuration, pagesDir);
		request.setAttribute(RequestAttributes.DISPATCHER, dispatcher);
		return dispatcher;
	}

	public static Dispatch getDispatch(HttpServletRequest request) {
		Dispatcher dispatcher = get(request);
		return getDispatch(dispatcher, request);
	}

	public static Dispatch getDispatch(HttpServletRequest request, Object actionBean) {
		Dispatcher dispatcher = get(request);
		if (actionBean instanceof AbstractActionBean) {
			String actionPath = ((AbstractActionBean) actionBean).getContext().getActionPath();
			return dispatcher.getDispatch(actionPath);
		} else {
			return getDispatch(request);
		}
	}

	public static Dispatch getDispatch(ActionBeanContext context) {
		HttpServletRequest request = context.getRequest();
		Dispatcher dispatcher = get(request);
		if (context instanceof ElementsActionBeanContext) {
			String actionPath = ((ElementsActionBeanContext) context).getActionPath();
			return dispatcher.getDispatch(actionPath);
		} else {
			return getDispatch(request);
		}
	}

	public static Dispatch getDispatch(Dispatcher dispatcher, HttpServletRequest request) {
		// TODO ElementsActionBeanContext
		ElementsActionBeanContext context = new ElementsActionBeanContext();
		context.setRequest(request);
		String originalPath = context.getActionPath();
		return dispatcher.getDispatch(originalPath);
	}
}