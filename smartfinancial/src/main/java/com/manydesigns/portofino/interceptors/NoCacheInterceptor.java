package com.manydesigns.portofino.interceptors;

import com.manydesigns.elements.servlet.ServletConstants;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.controller.ExecutionContext;
import net.sourceforge.stripes.controller.Interceptor;
import net.sourceforge.stripes.controller.Intercepts;
import net.sourceforge.stripes.controller.LifecycleStage;

import javax.servlet.http.HttpServletResponse;

@Intercepts(LifecycleStage.RequestInit)
public class NoCacheInterceptor implements Interceptor {
	public Resolution intercept(ExecutionContext context) throws Exception {
		HttpServletResponse response = context.getActionBeanContext().getResponse();
		// Avoid caching of dynamic pages
		// HTTP 1.0
		response.setHeader(ServletConstants.HTTP_PRAGMA, ServletConstants.HTTP_PRAGMA_NO_CACHE);
		response.setDateHeader(ServletConstants.HTTP_EXPIRES, 0);

		// HTTP 1.1
		response.addHeader(ServletConstants.HTTP_CACHE_CONTROL, ServletConstants.HTTP_CACHE_CONTROL_NO_CACHE);
		response.addHeader(ServletConstants.HTTP_CACHE_CONTROL, ServletConstants.HTTP_CACHE_CONTROL_NO_STORE);
		// response.addHeader(ServletConstants.HTTP_CACHE_CONTROL,
		// ServletConstants.HTTP_CACHE_CONTROL_MUST_REVALIDATE);
		// response.addHeader(ServletConstants.HTTP_CACHE_CONTROL,
		// ServletConstants.HTTP_CACHE_CONTROL_MAX_AGE + 0);

		return context.proceed();
	}
}