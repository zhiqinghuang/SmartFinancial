package com.manydesigns.portofino.interceptors;

import com.manydesigns.portofino.buttons.ButtonsLogic;
import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ErrorResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.controller.ExecutionContext;
import net.sourceforge.stripes.controller.Interceptor;
import net.sourceforge.stripes.controller.Intercepts;
import net.sourceforge.stripes.controller.LifecycleStage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

@Intercepts(LifecycleStage.EventHandling)
public class GuardsInterceptor implements Interceptor {
	public final static Logger logger = LoggerFactory.getLogger(GuardsInterceptor.class);

	private static final int CONFLICT = 409;

	public Resolution intercept(ExecutionContext context) throws Exception {
		logger.debug("Retrieving Stripes objects");
		ActionBean actionBean = context.getActionBean();
		Method handler = context.getHandler();

		logger.debug("Checking guards on {}", handler);
		if (ButtonsLogic.doGuardsPass(actionBean, handler)) {
			return context.proceed();
		} else {
			logger.warn("Operation not permitted.");
			return new ErrorResolution(CONFLICT);
		}
	}
}