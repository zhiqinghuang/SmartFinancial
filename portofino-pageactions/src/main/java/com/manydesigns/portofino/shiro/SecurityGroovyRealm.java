package com.manydesigns.portofino.shiro;

import com.manydesigns.elements.reflection.ClassAccessor;
import com.manydesigns.portofino.di.Injections;
import groovy.util.GroovyScriptEngine;
import groovy.util.ResourceException;
import groovy.util.ScriptException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.Destroyable;
import org.apache.shiro.util.LifecycleUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import java.io.Serializable;
import java.util.*;

public class SecurityGroovyRealm implements PortofinoRealm, Destroyable {
	public static final Logger logger = LoggerFactory.getLogger(SecurityGroovyRealm.class);

	protected final GroovyScriptEngine groovyScriptEngine;
	protected final String scriptUrl;
	protected final ServletContext servletContext;
	protected volatile PortofinoRealm security;
	protected volatile boolean destroyed = false;

	protected CacheManager cacheManager;

	public SecurityGroovyRealm(GroovyScriptEngine groovyScriptEngine, String scriptUrl, ServletContext servletContext) throws ScriptException, ResourceException, InstantiationException, IllegalAccessException {
		this.groovyScriptEngine = groovyScriptEngine;
		this.scriptUrl = scriptUrl;
		this.servletContext = servletContext;
		doEnsureDelegate();
	}

	private synchronized PortofinoRealm ensureDelegate() {
		if (destroyed) {
			throw new IllegalStateException("This realm has been destroyed.");
		}
		try {
			return doEnsureDelegate();
		} catch (Exception e) {
			throw new Error("Security.groovy not found or not loadable", e);
		}
	}

	private PortofinoRealm doEnsureDelegate() throws ScriptException, ResourceException, IllegalAccessException, InstantiationException {
		Class<?> scriptClass = groovyScriptEngine.loadScriptByName(scriptUrl);
		if (scriptClass.isInstance(security)) { // Class did not change
			return security;
		} else {
			logger.info("Refreshing Portofino Realm Delegate instance (Security.groovy)");
			if (security != null) {
				logger.debug("Script class changed: from " + security.getClass() + " to " + scriptClass);
			}
			Object securityTemp = scriptClass.newInstance();
			if (securityTemp instanceof PortofinoRealm) {
				PortofinoRealm realm = (PortofinoRealm) securityTemp;
				configureDelegate(realm);
				PortofinoRealm oldSecurity = security;
				security = realm;
				LifecycleUtils.destroy(oldSecurity);
				return realm;
			} else {
				throw new ClassCastException("Security object is not an instance of " + PortofinoRealm.class + ": " + securityTemp + " (" + securityTemp.getClass().getSuperclass() + " " + Arrays.asList(securityTemp.getClass().getInterfaces()) + ")");
			}
		}
	}

	protected void configureDelegate(PortofinoRealm security) {
		Injections.inject(security, servletContext, null);
		security.setCacheManager(cacheManager);
		LifecycleUtils.init(security);
	}

	@Override
	public void verifyUser(Serializable user) {
		ensureDelegate().verifyUser(user);
	}

	@Override
	public void changePassword(Serializable user, String oldPassword, String newPassword) {
		ensureDelegate().changePassword(user, oldPassword, newPassword);
	}

	@Override
	public String generateOneTimeToken(Serializable user) {
		return ensureDelegate().generateOneTimeToken(user);
	}

	@Override
	public Map<Serializable, String> getUsers() {
		return ensureDelegate().getUsers();
	}

	@Override
	public Serializable getUserById(String encodedUserId) {
		return ensureDelegate().getUserById(encodedUserId);
	}

	@Override
	public Serializable getUserByEmail(String email) {
		return ensureDelegate().getUserByEmail(email);
	}

	@Override
	public ClassAccessor getSelfRegisteredUserClassAccessor() {
		return ensureDelegate().getSelfRegisteredUserClassAccessor();
	}

	@Override
	public String saveSelfRegisteredUser(Object user) {
		return ensureDelegate().saveSelfRegisteredUser(user);
	}

	@Override
	public String getUserPrettyName(Serializable user) {
		return ensureDelegate().getUserPrettyName(user);
	}

	@Override
	public Serializable getUserId(Serializable user) {
		return ensureDelegate().getUserId(user);
	}

	@Override
	public Set<String> getGroups() {
		return ensureDelegate().getGroups();
	}

	@Override
	public String getName() {
		return ensureDelegate().getName();
	}

	@Override
	public boolean supports(AuthenticationToken token) {
		return ensureDelegate().supports(token);
	}

	@Override
	public AuthenticationInfo getAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		return ensureDelegate().getAuthenticationInfo(token);
	}

	@Override
	public boolean isPermitted(PrincipalCollection principals, String permission) {
		return ensureDelegate().isPermitted(principals, permission);
	}

	@Override
	public boolean isPermitted(PrincipalCollection subjectPrincipal, Permission permission) {
		return ensureDelegate().isPermitted(subjectPrincipal, permission);
	}

	@Override
	public boolean[] isPermitted(PrincipalCollection subjectPrincipal, String... permissions) {
		return ensureDelegate().isPermitted(subjectPrincipal, permissions);
	}

	@Override
	public boolean[] isPermitted(PrincipalCollection subjectPrincipal, List<Permission> permissions) {
		return ensureDelegate().isPermitted(subjectPrincipal, permissions);
	}

	@Override
	public boolean isPermittedAll(PrincipalCollection subjectPrincipal, String... permissions) {
		return ensureDelegate().isPermittedAll(subjectPrincipal, permissions);
	}

	@Override
	public boolean isPermittedAll(PrincipalCollection subjectPrincipal, Collection<Permission> permissions) {
		return ensureDelegate().isPermittedAll(subjectPrincipal, permissions);
	}

	@Override
	public void checkPermission(PrincipalCollection subjectPrincipal, String permission) throws AuthorizationException {
		ensureDelegate().checkPermission(subjectPrincipal, permission);
	}

	@Override
	public void checkPermission(PrincipalCollection subjectPrincipal, Permission permission) throws AuthorizationException {
		ensureDelegate().checkPermission(subjectPrincipal, permission);
	}

	@Override
	public void checkPermissions(PrincipalCollection subjectPrincipal, String... permissions) throws AuthorizationException {
		ensureDelegate().checkPermissions(subjectPrincipal, permissions);
	}

	@Override
	public void checkPermissions(PrincipalCollection subjectPrincipal, Collection<Permission> permissions) throws AuthorizationException {
		ensureDelegate().checkPermissions(subjectPrincipal, permissions);
	}

	@Override
	public boolean hasRole(PrincipalCollection subjectPrincipal, String roleIdentifier) {
		return ensureDelegate().hasRole(subjectPrincipal, roleIdentifier);
	}

	@Override
	public boolean[] hasRoles(PrincipalCollection subjectPrincipal, List<String> roleIdentifiers) {
		return ensureDelegate().hasRoles(subjectPrincipal, roleIdentifiers);
	}

	@Override
	public boolean hasAllRoles(PrincipalCollection subjectPrincipal, Collection<String> roleIdentifiers) {
		return ensureDelegate().hasAllRoles(subjectPrincipal, roleIdentifiers);
	}

	@Override
	public void checkRole(PrincipalCollection subjectPrincipal, String roleIdentifier) throws AuthorizationException {
		ensureDelegate().checkRole(subjectPrincipal, roleIdentifier);
	}

	@Override
	public void checkRoles(PrincipalCollection subjectPrincipal, Collection<String> roleIdentifiers) throws AuthorizationException {
		ensureDelegate().checkRoles(subjectPrincipal, roleIdentifiers);
	}

	@Override
	public void checkRoles(PrincipalCollection subjectPrincipal, String... roleIdentifiers) throws AuthorizationException {
		ensureDelegate().checkRoles(subjectPrincipal, roleIdentifiers);
	}

	@Override
	public void setCacheManager(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
		if (security != null) {
			security.setCacheManager(cacheManager);
		}
	}

	@Override
	public void destroy() {
		boolean wasDestroyed = destroyed;
		destroyed = true;
		if (!wasDestroyed) {
			logger.info("Destroying realm delegate");
			LifecycleUtils.destroy(security);
		}
	}
}