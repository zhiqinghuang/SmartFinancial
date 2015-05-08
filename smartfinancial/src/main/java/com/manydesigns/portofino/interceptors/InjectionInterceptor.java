package com.manydesigns.portofino.interceptors;

import com.manydesigns.portofino.di.Injections;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.controller.ExecutionContext;
import net.sourceforge.stripes.controller.Interceptor;
import net.sourceforge.stripes.controller.Intercepts;
import net.sourceforge.stripes.controller.LifecycleStage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

@Intercepts(LifecycleStage.BindingAndValidation)
public class InjectionInterceptor implements Interceptor {
	public final static Logger logger = LoggerFactory.getLogger(InjectionInterceptor.class);

	public Resolution intercept(ExecutionContext context) throws Exception {
		logger.debug("Retrieving Stripes objects");
		Object action = context.getActionBean();
		ActionBeanContext actionContext = context.getActionBeanContext();

		logger.debug("Retrieving Servlet API objects");
		HttpServletRequest request = actionContext.getRequest();
		ServletContext servletContext = actionContext.getServletContext();

		Injections.inject(action, servletContext, request);

		return context.proceed();
	}
}