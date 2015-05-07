package com.manydesigns.portofino.stripes;

import com.manydesigns.elements.ElementsThreadLocals;
import com.manydesigns.elements.stripes.ElementsActionBeanContext;
import com.manydesigns.portofino.PortofinoProperties;
import com.manydesigns.portofino.modules.BaseModule;
import net.sourceforge.stripes.action.ErrorResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.util.UrlBuilder;
import org.apache.commons.configuration.Configuration;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;
import java.util.Map;

public class ForbiddenAccessResolution implements Resolution {
	public final static Logger logger = LoggerFactory.getLogger(ForbiddenAccessResolution.class);

	public static final int STATUS = 403;

	private String errorMessage;

	public ForbiddenAccessResolution() {
	}

	public ForbiddenAccessResolution(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (request.getParameter("__portofino_quiet_auth_failure") != null) {
			return;
		}
		Subject subject = SecurityUtils.getSubject();
		ElementsActionBeanContext context = new ElementsActionBeanContext();
		context.setRequest(request);
		String originalPath = context.getActionPath();
		UrlBuilder urlBuilder = new UrlBuilder(Locale.getDefault(), originalPath, false);
		Map<?, ?> parameters = request.getParameterMap();
		urlBuilder.addParameters(parameters);
		String returnUrl = urlBuilder.toString();
		boolean ajax = "true".equals(request.getParameter("ajax"));
		ServletContext servletContext = ElementsThreadLocals.getServletContext();
		Configuration configuration = (Configuration) servletContext.getAttribute(BaseModule.PORTOFINO_CONFIGURATION);
		if (!subject.isAuthenticated() && !ajax) {
			logger.info("Unauthenticated user not allowed to see {}. Redirecting to login.", originalPath);
			String loginPage = configuration.getString(PortofinoProperties.LOGIN_PAGE);
			RedirectResolution redirectResolution = new RedirectResolution(loginPage, true);
			redirectResolution.addParameter("returnUrl", returnUrl);
			redirectResolution.execute(request, response);
		} else {
			if (ajax) {
				logger.debug("AJAX call while user disconnected");
				// TODO where to redirect?
				String loginPage = configuration.getString(PortofinoProperties.LOGIN_PAGE);
				UrlBuilder loginUrlBuilder = new UrlBuilder(Locale.getDefault(), loginPage, false);
				response.setStatus(STATUS);
				new StreamingResolution("text/plain", loginUrlBuilder.toString()).execute(request, response);
			} else {
				logger.warn("User not authorized for url {}.", originalPath);
				new ErrorResolution(STATUS, errorMessage).execute(request, response);
			}
		}
	}
}