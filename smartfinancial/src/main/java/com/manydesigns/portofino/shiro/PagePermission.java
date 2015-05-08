package com.manydesigns.portofino.shiro;

import com.manydesigns.portofino.pages.Permissions;
import com.manydesigns.portofino.security.AccessLevel;
import org.apache.shiro.authz.Permission;

public class PagePermission implements Permission {
	protected final Permissions calculatedPermissions;
	protected final AccessLevel accessLevel;
	protected final String[] permissions;

	public PagePermission(Permissions calculatedPermissions, AccessLevel accessLevel, String... permissions) {
		this.calculatedPermissions = calculatedPermissions;
		this.accessLevel = accessLevel;
		this.permissions = permissions;
	}

	public boolean implies(Permission p) {
		return false;
	}

	public Permissions getCalculatedPermissions() {
		return calculatedPermissions;
	}

	public AccessLevel getAccessLevel() {
		return accessLevel;
	}

	public String[] getPermissions() {
		return permissions;
	}
}