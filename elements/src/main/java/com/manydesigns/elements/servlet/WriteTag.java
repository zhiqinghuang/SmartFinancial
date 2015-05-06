package com.manydesigns.elements.servlet;

import com.manydesigns.elements.ElementsThreadLocals;
import com.manydesigns.elements.ognl.OgnlUtils;
import com.manydesigns.elements.xml.XhtmlBuffer;
import com.manydesigns.elements.xml.XhtmlFragment;
import ognl.OgnlContext;
import ognl.OgnlException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

public class WriteTag extends TagSupport {
	private static final long serialVersionUID = -4797501527473521285L;
	public static final String APPLICATION_SCOPE = "APPLICATION";
	public static final String SESSION_SCOPE = "SESSION";
	public static final String REQUEST_SCOPE = "REQUEST";
	public static final String PAGE_SCOPE = "PAGE";

	protected String name;
	protected String property;
	protected String scope;

	public final static Logger logger = LoggerFactory.getLogger(WriteTag.class);

	@Override
	public int doStartTag() {
		JspWriter out = pageContext.getOut();
		try {
			doTag(out);
		} catch (Throwable e) {
			throw new Error(e);
		}
		return SKIP_BODY;
	}

	private void doTag(JspWriter out) throws OgnlException {
		Integer scopeCode;
		if (scope == null) {
			scopeCode = null;
		} else if (APPLICATION_SCOPE.equals(scope)) {
			scopeCode = PageContext.APPLICATION_SCOPE;
		} else if (SESSION_SCOPE.equals(scope)) {
			scopeCode = PageContext.SESSION_SCOPE;
		} else if (REQUEST_SCOPE.equals(scope)) {
			scopeCode = PageContext.REQUEST_SCOPE;
		} else if (PAGE_SCOPE.equals(scope)) {
			scopeCode = PageContext.PAGE_SCOPE;
		} else {
			logger.warn("Unknown scope: {}", scope);
			return;
		}

		Object bean;
		if (scopeCode != null) {
			bean = pageContext.getAttribute(name, scopeCode);
		} else {
			bean = pageContext.findAttribute(name);
		}

		if (bean == null) {
			logger.warn("Bean {} not found in scope {}", name, scope);
			return;
		}

		if (property != null) {
			// use property as Ognl expression
			OgnlContext ognlContext = ElementsThreadLocals.getOgnlContext();
			bean = OgnlUtils.getValueQuietly(property, ognlContext, bean);
		}

		if (bean instanceof XhtmlFragment) {
			XhtmlFragment xhtmlFragment = (XhtmlFragment) bean;
			XhtmlBuffer xb = new XhtmlBuffer(out);
			xhtmlFragment.toXhtml(xb);
		} else {
			logger.warn("Bean {} scope {} property {} not of type XhtmlFragment", new String[] { name, scope, property });
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}
}