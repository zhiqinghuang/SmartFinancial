package com.manydesigns.portofino.shiro;

import org.apache.shiro.authc.AuthenticationToken;

public class PasswordResetToken implements AuthenticationToken {

	private static final long serialVersionUID = 1768475502547231894L;
	protected final String token;
	protected final String newPassword;

	public PasswordResetToken(String token, String newPassword) {
		this.token = token;
		this.newPassword = newPassword;
	}

	@Override
	public Object getPrincipal() {
		return token;
	}

	@Override
	public String getCredentials() {
		return newPassword;
	}

	public String getToken() {
		return token;
	}

	public String getNewPassword() {
		return newPassword;
	}
}