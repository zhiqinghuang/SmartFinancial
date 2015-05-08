package com.manydesigns.portofino.interceptors;

import com.manydesigns.elements.ElementsThreadLocals;
import com.manydesigns.portofino.shiro.SecurityUtilsBean;
import com.manydesigns.portofino.shiro.ShiroUtils;
import com.manydesigns.portofino.stripes.AuthenticationRequiredResolution;
import com.manydesigns.portofino.stripes.ForbiddenAccessResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.controller.ExecutionContext;
import net.sourceforge.stripes.controller.Interceptor;
import net.sourceforge.stripes.controller.Intercepts;
import net.sourceforge.stripes.controller.LifecycleStage;
import ognl.OgnlContext;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.aop.MethodInvocation;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.aop.AnnotationsAuthorizingMethodInterceptor;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.io.Serializable;
import java.lang.reflect.Method;

@Intercepts(LifecycleStage.BindingAndValidation)
public class ShiroInterceptor implements Interceptor {
	public final static Logger logger = LoggerFactory.getLogger(ShiroInterceptor.class);

	public Resolution intercept(final ExecutionContext context) throws Exception {
		logger.debug("Retrieving user");
		Serializable userId = null;
		Subject subject = SecurityUtils.getSubject();
		Object principal = subject.getPrincipal();
		if (principal == null) {
			logger.debug("No user found");
		} else {
			userId = ShiroUtils.getUserId(subject);
			logger.debug("Retrieved userId={}", userId);
		}

		logger.debug("Publishing securityUtils in OGNL context");
		OgnlContext ognlContext = ElementsThreadLocals.getOgnlContext();
		ognlContext.put("securityUtils", new SecurityUtilsBean());

		logger.debug("Setting up logging MDC");
		MDC.clear();
		if (userId != null) { // Issue #755
			MDC.put("userId", userId.toString());
		}

		try {
			AUTH_CHECKER.assertAuthorized(context);
			logger.debug("Security check passed.");
			return context.proceed();
		} catch (UnauthenticatedException e) {
			logger.debug("Method required authentication", e);
			return new AuthenticationRequiredResolution();
		} catch (AuthorizationException e) {
			logger.warn("Method invocation not authorized", e);
			return new ForbiddenAccessResolution(e.getMessage());
		}
	}

	public static final class AuthChecker extends AnnotationsAuthorizingMethodInterceptor {

		public void assertAuthorized(final ExecutionContext context) throws AuthorizationException {
			super.assertAuthorized(new MethodInvocation() {
				@Override
				public Object proceed() throws Throwable {
					return null;
				}

				@Override
				public Method getMethod() {
					return context.getHandler();
				}

				@Override
				public Object[] getArguments() {
					return new Object[0];
				}

				@Override
				public Object getThis() {
					return context.getActionBean();
				}
			});
		}
	}

	protected static final AuthChecker AUTH_CHECKER = new AuthChecker();

}