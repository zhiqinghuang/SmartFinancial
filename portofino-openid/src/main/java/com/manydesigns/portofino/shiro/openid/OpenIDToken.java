package com.manydesigns.portofino.shiro.openid;

import org.apache.shiro.authc.AuthenticationToken;
import org.openid4java.discovery.Identifier;

import java.io.Serializable;

public class OpenIDToken implements AuthenticationToken, Serializable {
	protected final Identifier identifier;
	protected final String firstLoginToken;

	public OpenIDToken(Identifier identifier, String firstLoginToken) {
		this.identifier = identifier;
		this.firstLoginToken = firstLoginToken;
	}

	public Identifier getPrincipal() {
		return identifier;
	}

	public Identifier getCredentials() {
		return identifier;
	}

	public Identifier getIdentifier() {
		return identifier;
	}

	public String getFirstLoginToken() {
		return firstLoginToken;
	}
}