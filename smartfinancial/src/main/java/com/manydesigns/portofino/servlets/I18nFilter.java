package com.manydesigns.portofino.servlets;

import com.manydesigns.portofino.i18n.I18nUtils;

import javax.servlet.*;
import java.io.IOException;

public class I18nFilter implements Filter {
	protected ServletContext servletContext;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		servletContext = filterConfig.getServletContext();
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		// I18n
		I18nUtils.setupTextProvider(servletContext, request);
		filterChain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		servletContext = null;
	}
}