package com.manydesigns.portofino.interceptors;

import com.manydesigns.portofino.dispatcher.Dispatch;
import com.manydesigns.portofino.dispatcher.DispatcherUtil;
import com.manydesigns.portofino.logic.SecurityLogic;
import com.manydesigns.portofino.stripes.ForbiddenAccessResolution;
import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.controller.ExecutionContext;
import net.sourceforge.stripes.controller.Interceptor;
import net.sourceforge.stripes.controller.Intercepts;
import net.sourceforge.stripes.controller.LifecycleStage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

@Intercepts(LifecycleStage.BindingAndValidation)
public class SecurityInterceptor implements Interceptor {
	public final static Logger logger = LoggerFactory.getLogger(SecurityInterceptor.class);

	public Resolution intercept(ExecutionContext context) throws Exception {
		logger.debug("Retrieving Stripes objects");
		ActionBeanContext actionContext = context.getActionBeanContext();
		ActionBean actionBean = context.getActionBean();
		Method handler = context.getHandler();

		logger.debug("Retrieving Servlet API objects");
		HttpServletRequest request = actionContext.getRequest();

		Dispatch dispatch = DispatcherUtil.getDispatch(request);

		if (SecurityLogic.isAllowed(request, dispatch, actionBean, handler)) {
			logger.debug("Security check passed.");
			return context.proceed();
		} else {
			return new ForbiddenAccessResolution();
		}
	}
}