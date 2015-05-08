package com.manydesigns.elements.servlet;

import com.manydesigns.elements.ElementsThreadLocals;
import ognl.OgnlContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class ElementsFilter implements Filter {
	public static final String REQUEST_OGNL_ATTRIBUTE = "request";
	public static final String SESSION_OGNL_ATTRIBUTE = "session";
	public static final String SERVLET_CONTEXT_OGNL_ATTRIBUTE = "servletContext";

	protected FilterConfig filterConfig;
	protected ServletContext servletContext;

	public final static Logger logger = LoggerFactory.getLogger(ElementsFilter.class);

	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
		this.servletContext = filterConfig.getServletContext();
		logger.info("ElementsFilter initialized");
	}

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException {
		if (req instanceof HttpServletRequest && res instanceof HttpServletResponse) {
			doHttpFilter((HttpServletRequest) req, (HttpServletResponse) res, filterChain);
		} else {
			filterChain.doFilter(req, res);
		}
	}

	public void destroy() {
		logger.info("ElementsFilter destroyed");
	}

	protected void doHttpFilter(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws IOException, ServletException {
		ServletContext context = filterConfig.getServletContext();
		try {
			logger.debug("Setting up default OGNL context");
			ElementsThreadLocals.setupDefaultElementsContext();
			OgnlContext ognlContext = ElementsThreadLocals.getOgnlContext();

			logger.debug("Creating request attribute mapper");
			AttributeMap requestAttributeMap = AttributeMap.createAttributeMap(req);
			ognlContext.put(REQUEST_OGNL_ATTRIBUTE, requestAttributeMap);

			logger.debug("Creating session attribute mapper");
			HttpSession session = req.getSession(false);
			AttributeMap sessionAttributeMap = AttributeMap.createAttributeMap(session);
			ognlContext.put(SESSION_OGNL_ATTRIBUTE, sessionAttributeMap);

			logger.debug("Creating servlet context attribute mapper");
			AttributeMap servletContextAttributeMap = AttributeMap.createAttributeMap(servletContext);
			ognlContext.put(SERVLET_CONTEXT_OGNL_ATTRIBUTE, servletContextAttributeMap);

			ElementsThreadLocals.setHttpServletRequest(req);
			ElementsThreadLocals.setHttpServletResponse(res);
			ElementsThreadLocals.setServletContext(context);

			filterChain.doFilter(req, res);
		} finally {
			ElementsThreadLocals.removeElementsContext();
		}
	}
}