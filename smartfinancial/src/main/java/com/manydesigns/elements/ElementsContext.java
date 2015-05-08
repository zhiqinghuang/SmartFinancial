package com.manydesigns.elements;

import com.manydesigns.elements.i18n.TextProvider;
import ognl.OgnlContext;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ElementsContext {
	protected TextProvider textProvider;
	protected HttpServletRequest httpServletRequest;
	protected HttpServletResponse httpServletResponse;
	protected ServletContext servletContext;
	protected OgnlContext ognlContext;

	public ElementsContext() {
	}

	public TextProvider getTextProvider() {
		return textProvider;
	}

	public void setTextProvider(TextProvider textProvider) {
		this.textProvider = textProvider;
	}

	public HttpServletRequest getHttpServletRequest() {
		return httpServletRequest;
	}

	public void setHttpServletRequest(HttpServletRequest httpServletRequest) {
		this.httpServletRequest = httpServletRequest;
	}

	public HttpServletResponse getHttpServletResponse() {
		return httpServletResponse;
	}

	public void setHttpServletResponse(HttpServletResponse httpServletResponse) {
		this.httpServletResponse = httpServletResponse;
	}

	public ServletContext getServletContext() {
		return servletContext;
	}

	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	public OgnlContext getOgnlContext() {
		return ognlContext;
	}

	public void setOgnlContext(OgnlContext ognlContext) {
		this.ognlContext = ognlContext;
	}
}