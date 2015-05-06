package com.manydesigns.portofino.shiro;

import org.apache.shiro.authc.AuthenticationToken;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

public class ServletContainerToken implements AuthenticationToken {

	private static final long serialVersionUID = -4271289567810361230L;

	protected final Principal userPrincipal;

	public ServletContainerToken(HttpServletRequest request) {
		userPrincipal = request.getUserPrincipal();
		if (userPrincipal == null) {
			throw new NullPointerException("UserPrincipal can not be null");
		}
	}

	public Principal getPrincipal() {
		return userPrincipal;
	}

	public Object getCredentials() {
		return "";
	}
}