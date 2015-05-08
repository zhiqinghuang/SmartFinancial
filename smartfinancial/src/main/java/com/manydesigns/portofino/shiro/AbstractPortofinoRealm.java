package com.manydesigns.portofino.shiro;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.configuration.Configuration;
import org.apache.shiro.authc.credential.PasswordMatcher;
import org.apache.shiro.authc.credential.PasswordService;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.HashService;
import org.apache.shiro.crypto.hash.format.HashFormat;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import com.manydesigns.elements.reflection.ClassAccessor;
import com.manydesigns.elements.reflection.JavaClassAccessor;
import com.manydesigns.portofino.di.Inject;
import com.manydesigns.portofino.logic.SecurityLogic;
import com.manydesigns.portofino.modules.BaseModule;

public abstract class AbstractPortofinoRealm extends AuthorizingRealm implements PortofinoRealm {

	@Inject(BaseModule.PORTOFINO_CONFIGURATION)
	protected Configuration portofinoConfiguration;

	protected PasswordService passwordService;

	protected AbstractPortofinoRealm() {
		// Legacy - let the actual implementation handle hashing
		setup(new PlaintextHashService(), new PlaintextHashFormat());
	}

	public AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		Object principal = principals.getPrimaryPrincipal();
		Set<String> groups = new HashSet<String>();
		groups.add(SecurityLogic.getAllGroup(portofinoConfiguration));
		if (principal == null) {
			groups.add(SecurityLogic.getAnonymousGroup(portofinoConfiguration));
		} else if (principal instanceof Serializable) {
			groups.add(SecurityLogic.getRegisteredGroup(portofinoConfiguration));
			groups.addAll(loadAuthorizationInfo((Serializable) principal));
		} else {
			throw new AuthorizationException("Invalid principal: " + principal);
		}

		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo(groups);
		if (groups.contains(SecurityLogic.getAdministratorsGroup(portofinoConfiguration))) {
			info.addStringPermission("*");
		}
		Permission permission = new GroupPermission(groups);
		info.setObjectPermissions(Collections.singleton(permission));
		return info;
	}

	protected Collection<String> loadAuthorizationInfo(Serializable principal) {
		return Collections.emptySet();
	}

	public Set<String> getGroups() {
		Set<String> groups = new LinkedHashSet<String>();
		groups.add(SecurityLogic.getAllGroup(portofinoConfiguration));
		groups.add(SecurityLogic.getAnonymousGroup(portofinoConfiguration));
		groups.add(SecurityLogic.getRegisteredGroup(portofinoConfiguration));
		groups.add(SecurityLogic.getAdministratorsGroup(portofinoConfiguration));
		return groups;
	}

	@Override
	public Serializable getUserById(String encodedUserId) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Serializable getUserByEmail(String email) {
		throw new UnsupportedOperationException();
	}

	@Override
	public ClassAccessor getSelfRegisteredUserClassAccessor() {
		return JavaClassAccessor.getClassAccessor(User.class);
	}

	@Override
	public String getUserPrettyName(Serializable user) {
		return user.toString();
	}

	@Override
	public void verifyUser(Serializable user) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void changePassword(Serializable user, String oldPassword, String newPassword) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String generateOneTimeToken(Serializable user) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String saveSelfRegisteredUser(Object user) {
		throw new UnsupportedOperationException();
	}

	protected void setup(HashService hashService, HashFormat hashFormat) {
		PortofinoPasswordService passwordService = new PortofinoPasswordService();
		passwordService.setHashService(hashService);
		passwordService.setHashFormat(hashFormat);
		PasswordMatcher passwordMatcher = new PasswordMatcher();
		passwordMatcher.setPasswordService(passwordService);
		setCredentialsMatcher(passwordMatcher);
		this.passwordService = passwordService;
	}
}