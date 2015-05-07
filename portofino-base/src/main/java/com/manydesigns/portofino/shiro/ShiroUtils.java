package com.manydesigns.portofino.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;

import java.io.Serializable;
import java.util.List;

public class ShiroUtils {
	public static Object getPrimaryPrincipal(Subject s) {
		return getPrincipal(s, 0);
	}

	public static Object getPrincipal(Subject s, int i) {
		Object principal = s.getPrincipal();
		if (principal instanceof PrincipalCollection) {
			List principals = ((PrincipalCollection) principal).asList();
			return principals.get(i);
		} else {
			if (i == 0) {
				return principal;
			} else {
				throw new IndexOutOfBoundsException("The subject has only 1 principal, index " + i + " is not valid");
			}
		}
	}

	public static PortofinoRealm getPortofinoRealm() {
		RealmSecurityManager realmSecurityManager = (RealmSecurityManager) SecurityUtils.getSecurityManager();
		PortofinoRealm portofinoRealm = (PortofinoRealm) realmSecurityManager.getRealms().iterator().next();
		return portofinoRealm;
	}

	public static Serializable getUserId(Subject subject) {
		PortofinoRealm portofinoRealm = getPortofinoRealm();
		Serializable principal = (Serializable) getPrimaryPrincipal(subject);
		if (portofinoRealm != null) {
			return portofinoRealm.getUserId(principal);
		} else {
			return principal;
		}
	}
}