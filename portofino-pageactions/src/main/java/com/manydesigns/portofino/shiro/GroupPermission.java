package com.manydesigns.portofino.shiro;

import com.manydesigns.portofino.pages.Permissions;
import com.manydesigns.portofino.security.AccessLevel;
import org.apache.shiro.authz.Permission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class GroupPermission implements Permission {

	protected final Collection<String> groups;
	public static final Logger logger = LoggerFactory.getLogger(GroupPermission.class);

	public GroupPermission(Collection<String> groups) {
		this.groups = groups;
	}

	public boolean implies(Permission p) {
		if (p instanceof GroupPermission) {
			GroupPermission gp = (GroupPermission) p;
			return gp.groups.containsAll(groups) && groups.containsAll(gp.groups);
		} else if (p instanceof PagePermission) {
			PagePermission pp = (PagePermission) p;
			return hasPermissions(pp.getCalculatedPermissions(), groups, pp.getAccessLevel(), pp.getPermissions());
		}
		return false;
	}

	public static boolean hasPermissions(Permissions configuration, Collection<String> groups, AccessLevel level, String... permissions) {
		boolean hasLevel = level == null;
		boolean hasPermissions = true;
		Map<String, Boolean> permMap = new HashMap<String, Boolean>(permissions.length);
		for (String groupId : groups) {
			AccessLevel actualLevel = configuration.getActualLevels().get(groupId);
			if (actualLevel == AccessLevel.DENY) {
				return false;
			} else if (!hasLevel && actualLevel != null && actualLevel.isGreaterThanOrEqual(level)) {
				hasLevel = true;
			}

			Set<String> perms = configuration.getActualPermissions().get(groupId);
			if (perms != null) {
				for (String permission : permissions) {
					if (perms.contains(permission)) {
						permMap.put(permission, true);
					}
				}
			}
		}

		for (String permission : permissions) {
			hasPermissions &= permMap.containsKey(permission);
		}

		hasPermissions = hasLevel && hasPermissions;
		if (!hasPermissions) {
			logger.debug("User does not have permissions. User's groups: {}", groups);
		}
		return hasPermissions;
	}
}