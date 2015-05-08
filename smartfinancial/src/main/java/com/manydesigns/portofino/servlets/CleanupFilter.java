package com.manydesigns.portofino.servlets;

import com.manydesigns.elements.ElementsThreadLocals;
import com.manydesigns.portofino.modules.DatabaseModule;
import com.manydesigns.portofino.persistence.Persistence;
import org.slf4j.MDC;

import javax.servlet.*;
import java.io.IOException;

public class CleanupFilter implements Filter {
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		try {
			chain.doFilter(request, response);
		} finally {
			ServletContext servletContext = ElementsThreadLocals.getServletContext();
			Persistence persistence = (Persistence) servletContext.getAttribute(DatabaseModule.PERSISTENCE);
			MDC.clear();
			if (persistence != null && persistence.getModel() != null) {
				persistence.closeSessions();
			}
		}
	}

	public void destroy() {
	}
}