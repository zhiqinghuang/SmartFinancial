package com.manydesigns.portofino.shiro;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.manydesigns.portofino.logic.SecurityLogic;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.pam.UnsupportedTokenException;

import java.io.Serializable;
import java.util.*;

public class GAEPortofinoRealm extends AbstractPortofinoRealm {

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) {
		if (!(token instanceof ServletContainerToken)) {
			throw new UnsupportedTokenException("Token not supported: " + token);
		}
		// On GAE, if the user was logged by the container, it is also known to
		// the UserService
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		if (user == null) {
			throw new UnknownAccountException("User is authenticated to the container, but is not known to the UserService");
		}
		// TODO verifica utilizzo User come principal direttamente
		return new SimpleAuthenticationInfo(user, token.getCredentials(), getName());
	}

	@Override
	protected Collection<String> loadAuthorizationInfo(Serializable principal) {
		Set<String> authz = new HashSet<String>(super.loadAuthorizationInfo(principal));
		User user = (User) principal;
		UserService userService = UserServiceFactory.getUserService();
		if (user != null && userService.isUserAdmin() && StringUtils.equals(userService.getCurrentUser().getUserId(), user.getUserId())) {
			authz.add(SecurityLogic.getAdministratorsGroup(portofinoConfiguration));
		}
		return authz;
	}

	public Map<Serializable, String> getUsers() {
		return new HashMap<Serializable, String>();
	}

	@Override
	public boolean supports(AuthenticationToken token) {
		return token instanceof ServletContainerToken;
	}

	@Override
	public Serializable getUserByEmail(String email) {
		throw new UnsupportedOperationException(); // TODO verificare
	}

	@Override
	public Serializable getUserId(Serializable user) {
		return ((User) user).getUserId();
	}

	@Override
	public String getUserPrettyName(Serializable user) {
		return ((User) user).getNickname();
	}
}