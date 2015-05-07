package com.manydesigns.portofino.shiro;

import org.apache.shiro.authc.AuthenticationToken;

public class SignUpToken implements AuthenticationToken {
	private static final long serialVersionUID = 3805617406572200869L;
	protected final String token;

	public SignUpToken(String token) {
		this.token = token;
	}

	@Override
	public Object getPrincipal() {
		return token;
	}

	@Override
	public String getCredentials() {
		return token;
	}

	public String getToken() {
		return token;
	}
}