package com.manydesigns.portofino.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import java.io.Serializable;

public class SecurityUtilsBean {
	public Subject getSubject() {
		return SecurityUtils.getSubject();
	}

	public Serializable getUserId() {
		return ShiroUtils.getUserId(getSubject());
	}

	public org.apache.shiro.mgt.SecurityManager getSecurityManager() {
		return SecurityUtils.getSecurityManager();
	}

	public Object getPrimaryPrincipal() {
		return ShiroUtils.getPrimaryPrincipal(getSubject());
	}

	public Object getPrincipal(int index) {
		return ShiroUtils.getPrincipal(getSubject(), index);
	}
}