package com.manydesigns.portofino.shiro;

import com.manydesigns.elements.reflection.ClassAccessor;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authz.Authorizer;
import org.apache.shiro.cache.CacheManagerAware;
import org.apache.shiro.realm.Realm;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

public interface PortofinoRealm extends Realm, Authorizer, CacheManagerAware {
	void verifyUser(Serializable user);

	void changePassword(Serializable user, String oldPassword, String newPassword) throws IncorrectCredentialsException;

	String generateOneTimeToken(Serializable user);

	ClassAccessor getSelfRegisteredUserClassAccessor();

	String saveSelfRegisteredUser(Object user) throws RegistrationException;

	Map<Serializable, String> getUsers();

	Serializable getUserById(String encodedUserId);

	Serializable getUserByEmail(String email);

	String getUserPrettyName(Serializable user);

	Serializable getUserId(Serializable user);

	Set<String> getGroups();
}