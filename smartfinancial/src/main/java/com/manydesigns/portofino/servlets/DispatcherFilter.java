package com.manydesigns.portofino.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.manydesigns.portofino.dispatcher.DispatcherUtil;

public class DispatcherFilter implements Filter {

	public final static Logger logger = LoggerFactory.getLogger(DispatcherFilter.class);

	protected FilterConfig filterConfig;
	protected ServletContext servletContext;

	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
		servletContext = filterConfig.getServletContext();
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// cast to http type
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		logger.debug("Installing the dispatcher in the http current request");
		DispatcherUtil.install(httpRequest);
		chain.doFilter(request, response);
	}

	private void restoreRequestAttributes(ServletRequest request, Map<String, Object> savedAttributes) {
		List<String> attrNamesToRemove = new ArrayList<String>();
		Enumeration attrNames = request.getAttributeNames();
		while (attrNames.hasMoreElements()) {
			attrNamesToRemove.add((String) attrNames.nextElement());
		}
		for (String attrName : attrNamesToRemove) {
			request.removeAttribute(attrName);
		}
		for (Map.Entry<String, Object> entry : savedAttributes.entrySet()) {
			request.setAttribute(entry.getKey(), entry.getValue());
		}
	}

	private Map<String, Object> saveAndResetRequestAttributes(ServletRequest request) {
		Map<String, Object> savedAttributes = new HashMap<String, Object>();
		logger.debug("--- start req dump ---");
		Enumeration attrNames = request.getAttributeNames();
		while (attrNames.hasMoreElements()) {
			String attrName = (String) attrNames.nextElement();
			Object attrValue = request.getAttribute(attrName);
			logger.debug("{} = {}", attrName, attrValue);
			savedAttributes.put(attrName, attrValue);
		}
		for (String attrName : savedAttributes.keySet()) {
			if (attrName.startsWith("javax.servlet")) {
				continue;
			}
			if (attrName.equals("returnUrl")) {
				continue;
			}
			request.removeAttribute(attrName);
		}
		logger.debug("--- end req dump ---");
		return savedAttributes;
	}

	public void destroy() {
		filterConfig = null;
		servletContext = null;
	}
}