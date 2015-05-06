package com.manydesigns.portofino.servlets;

import javax.servlet.*;
import java.io.IOException;

public class UTF8ResponseFilter implements Filter {
	public static final String copyright = "Copyright (c) 2005-2015, ManyDesigns srl";

	public void init(FilterConfig filterConfig) throws ServletException {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		response.setCharacterEncoding("UTF-8");
		chain.doFilter(request, response);
	}

	public void destroy() {
	}
}