package com.manydesigns.portofino.shiro;

import java.io.Serializable;
import java.security.Principal;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.PathMatchingFilter;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServletContainerSecurityFilter extends PathMatchingFilter {

	public static final Logger logger = LoggerFactory.getLogger(ServletContainerSecurityFilter.class);

	@Override
	protected boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
		Subject subject = SecurityUtils.getSubject();
		HttpServletRequest req = (HttpServletRequest) request;
		boolean shiroAuthenticated = subject.isAuthenticated();

		// Returns: a java.security.Principal containing the name of the user
		// making this request;
		// null if the user has not been authenticated
		Principal containerPrincipal = getContainerPrincipal(req);
		boolean containerAuthenticated = containerPrincipal != null;
		logger.debug("User authenticated by Shiro? {} User authenticated by the container? {}", shiroAuthenticated, containerAuthenticated);
		if (!shiroAuthenticated && containerAuthenticated) {
			logger.debug("User is known to the servlet container, but not to Shiro, attempting programmatic login");
			try {
				subject.login(new ServletContainerToken(req));
				Serializable userId = ShiroUtils.getUserId(SecurityUtils.getSubject());
				logger.info("User {} login", userId);
			} catch (AuthenticationException e) {
				HttpSession session = req.getSession(false);
				String attrName = ServletContainerSecurityFilter.class.getName() + ".shiroLoginFailedErrorLogged";
				String msg = "User " + containerPrincipal + " is known to the servlet container, " + "but not to Shiro, and programmatic login failed!";
				if (session == null || session.getAttribute(attrName) == null) {
					logger.error(msg, e);
				} else {
					logger.debug(msg, e);
				}
				if (session != null) {
					session.setAttribute(attrName, true);
				}
			}
		} else if (shiroAuthenticated && !containerAuthenticated) {
			logger.debug("User is authenticated to Shiro, but not to the servlet container; logging out of Shiro.");
			Serializable userId = ShiroUtils.getUserId(SecurityUtils.getSubject());
			subject.logout();
			logger.info("User {} logout", userId);
		}
		return true;
	}

	protected Principal getContainerPrincipal(HttpServletRequest req) {
		if (req instanceof ShiroHttpServletRequest) {

			ServletRequest request = ((ShiroHttpServletRequest) req).getRequest();
			if (request instanceof HttpServletRequest) {
				HttpServletRequest httpRequest = (HttpServletRequest) request;
				return httpRequest.getUserPrincipal();
			}
		}
		return req.getUserPrincipal();
	}
}