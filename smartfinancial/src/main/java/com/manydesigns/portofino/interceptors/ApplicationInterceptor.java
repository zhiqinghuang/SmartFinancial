package com.manydesigns.portofino.interceptors;

import com.manydesigns.portofino.RequestAttributes;
import com.manydesigns.portofino.dispatcher.*;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.controller.ExecutionContext;
import net.sourceforge.stripes.controller.Interceptor;
import net.sourceforge.stripes.controller.Intercepts;
import net.sourceforge.stripes.controller.LifecycleStage;
import org.apache.commons.lang.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.DispatcherType;
import javax.servlet.http.HttpServletRequest;

@Intercepts(LifecycleStage.CustomValidation)
public class ApplicationInterceptor implements Interceptor {

	public final static Logger logger = LoggerFactory.getLogger(ApplicationInterceptor.class);

	public Resolution intercept(ExecutionContext context) throws Exception {
		logger.debug("Retrieving Stripes objects");
		ActionBeanContext actionContext = context.getActionBeanContext();

		logger.debug("Retrieving Servlet API objects");
		HttpServletRequest request = actionContext.getRequest();

		if (request.getDispatcherType() == DispatcherType.REQUEST) {
			logger.debug("Starting page response timer");
			StopWatch stopWatch = new StopWatch();
			// There is no need to stop this timer.
			stopWatch.start();
			request.setAttribute(RequestAttributes.STOP_WATCH, stopWatch);
		}

		Resolution resolution = DispatcherLogic.dispatch(actionContext);
		return resolution != null ? resolution : context.proceed();
	}
}