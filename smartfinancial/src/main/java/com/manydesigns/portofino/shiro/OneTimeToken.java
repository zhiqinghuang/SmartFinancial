package com.manydesigns.portofino.shiro;

import org.apache.shiro.authc.AuthenticationToken;

public class OneTimeToken implements AuthenticationToken {

	private static final long serialVersionUID = 3944968614303141586L;
	final String token;

	public OneTimeToken(String token) {
		this.token = token;
	}

	@Override
	public Object getPrincipal() {
		return token;
	}

	@Override
	public Object getCredentials() {
		return token;
	}
}