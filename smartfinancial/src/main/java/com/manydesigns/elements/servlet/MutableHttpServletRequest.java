package com.manydesigns.elements.servlet;

import net.sourceforge.stripes.mock.MockHttpSession;
import net.sourceforge.stripes.mock.MockServletContext;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang.ArrayUtils;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.*;

public class MutableHttpServletRequest implements HttpServletRequest {
	public final Map<String, Object> attributeMap;
	public final Map<String, String[]> parameterMap;
	public final Map<String, String[]> headerMap;
	public final Map<String, FileItem[]> fileItemMap;
	public final List<Locale> locales;

	private String method;
	private String contextPath;
	private String servletPath;
	private String requestURI;
	private String queryString;
	private String scheme;
	private String serverName;
	private int serverPort;

	private String contentType;
	private String characterEncoding;

	private MockServletContext servletContext;
	private MockHttpSession session;

	public MutableHttpServletRequest() {
		attributeMap = new HashMap<String, Object>();
		headerMap = new HashMap<String, String[]>();
		parameterMap = new HashMap<String, String[]>();
		fileItemMap = new HashMap<String, FileItem[]>();
		locales = new ArrayList<Locale>();
		locales.add(Locale.getDefault());
	}

	public MutableHttpServletRequest(MockServletContext servletContext) {
		this();
		this.servletContext = servletContext;
	}

	public void addParameter(String name, String value) {
		String[] oldValues = parameterMap.get(name);
		String[] newValues = (String[]) ArrayUtils.add(oldValues, value);
		parameterMap.put(name, newValues);
	}

	public void setParameter(String key, String... values) {
		parameterMap.put(key, values);
	}

	public void addFileItem(String name, FileItem item) {
		FileItem[] oldValues = fileItemMap.get(name);
		FileItem[] newValues = (FileItem[]) ArrayUtils.add(oldValues, item);
		fileItemMap.put(name, newValues);
	}

	public FileItem getFileItem(String name) {
		FileItem[] values = fileItemMap.get(name);
		if (values == null) {
			return null;
		} else {
			return values[0];
		}
	}

	public void setFileItem(String key, FileItem value) {
		FileItem[] values = { value };
		fileItemMap.put(key, values);
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public void setContextPath(String context) {
		this.contextPath = context;
	}

	public void setServletPath(String servletPath) {
		this.servletPath = servletPath;
	}

	public void makeMultipart() {
		setMethod("POST");
		setContentType("multipart/form-data");
	}

	public void setScheme(String scheme) {
		this.scheme = scheme;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	public String getParameter(String name) {
		String[] values = parameterMap.get(name);
		if (values == null || values.length == 0) {
			return null;
		} else {
			return values[0];
		}
	}

	public Enumeration getParameterNames() {
		return Collections.enumeration(parameterMap.keySet());
	}

	public String[] getParameterValues(String name) {
		return parameterMap.get(name);
	}

	public Map getParameterMap() {
		return parameterMap;
	}

	public String getMethod() {
		return method;
	}

	public String getContextPath() {
		return contextPath;
	}

	public String getServletPath() {
		return servletPath;
	}

	public Object getAttribute(String s) {
		return attributeMap.get(s);
	}

	public void setAttribute(String s, Object o) {
		attributeMap.put(s, o);
	}

	public void removeAttribute(String s) {
		attributeMap.remove(s);
	}

	public String getAuthType() {
		throw new UnsupportedOperationException();
	}

	public Cookie[] getCookies() {
		throw new UnsupportedOperationException();
	}

	public long getDateHeader(String s) {
		throw new UnsupportedOperationException();
	}

	public String getHeader(String s) {
		String[] values = headerMap.get(s);
		if (values == null || values.length == 0) {
			return null;
		}
		return values[0];
	}

	public Enumeration<String> getHeaders(String s) {
		String[] values = headerMap.get(s);
		if (values == null) {
			return null;
		}
		return Collections.enumeration(Arrays.asList(values));
	}

	public Enumeration<String> getHeaderNames() {
		return Collections.enumeration(headerMap.keySet());
	}

	public int getIntHeader(String s) {
		throw new UnsupportedOperationException();
	}

	public String getPathInfo() {
		return null; // TODO
	}

	public String getPathTranslated() {
		throw new UnsupportedOperationException();
	}

	public String getQueryString() {
		return queryString;
	}

	public String getRemoteUser() {
		throw new UnsupportedOperationException();
	}

	public boolean isUserInRole(String s) {
		throw new UnsupportedOperationException();
	}

	public Principal getUserPrincipal() {
		throw new UnsupportedOperationException();
	}

	public String getRequestedSessionId() {
		throw new UnsupportedOperationException();
	}

	public String getRequestURI() {
		return requestURI;
	}

	public void setRequestURI(String requestURI) {
		this.requestURI = requestURI;
	}

	public StringBuffer getRequestURL() {
		throw new UnsupportedOperationException();
	}

	public HttpSession getSession(boolean create) {
		if (create) {
			synchronized (this) {
				if (session == null) {
					session = new MockHttpSession(servletContext);
				}
			}
		}
		return session;
	}

	public HttpSession getSession() {
		return getSession(true);
	}

	public boolean isRequestedSessionIdValid() {
		throw new UnsupportedOperationException();
	}

	public boolean isRequestedSessionIdFromCookie() {
		throw new UnsupportedOperationException();
	}

	public boolean isRequestedSessionIdFromURL() {
		throw new UnsupportedOperationException();
	}

	@Deprecated
	public boolean isRequestedSessionIdFromUrl() {
		throw new UnsupportedOperationException();
	}

	public Enumeration getAttributeNames() {
		return Collections.enumeration(attributeMap.keySet());
	}

	public String getCharacterEncoding() {
		return characterEncoding;
	}

	public void setCharacterEncoding(String s) throws UnsupportedEncodingException {
		characterEncoding = s;
	}

	public int getContentLength() {
		throw new UnsupportedOperationException();
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public ServletInputStream getInputStream() throws IOException {
		throw new UnsupportedOperationException();
	}

	public String getProtocol() {
		throw new UnsupportedOperationException();
	}

	public String getScheme() {
		return scheme;
	}

	public String getServerName() {
		return serverName;
	}

	public int getServerPort() {
		return serverPort;
	}

	public BufferedReader getReader() throws IOException {
		throw new UnsupportedOperationException();
	}

	public String getRemoteAddr() {
		throw new UnsupportedOperationException();
	}

	public String getRemoteHost() {
		throw new UnsupportedOperationException();
	}

	public Locale getLocale() {
		return locales.get(0);
	}

	public Enumeration<Locale> getLocales() {
		return Collections.enumeration(locales);
	}

	public boolean isSecure() {
		throw new UnsupportedOperationException();
	}

	public RequestDispatcher getRequestDispatcher(String s) {
		throw new UnsupportedOperationException();
	}

	@Deprecated
	public String getRealPath(String s) {
		throw new UnsupportedOperationException();
	}

	public int getRemotePort() {
		throw new UnsupportedOperationException();
	}

	public String getLocalName() {
		throw new UnsupportedOperationException();
	}

	public String getLocalAddr() {
		throw new UnsupportedOperationException();
	}

	public int getLocalPort() {
		throw new UnsupportedOperationException();
	}

	// Servlet API 3.0 methods
	@Override
	public boolean authenticate(HttpServletResponse response) throws IOException, ServletException {
		return false;
	}

	@Override
	public void login(String s, String s1) throws ServletException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void logout() throws ServletException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Collection<Part> getParts() throws IOException, ServletException {
		return null;
	}

	@Override
	public Part getPart(String s) throws IOException, ServletException {
		return null;
	}

	@Override
	public ServletContext getServletContext() {
		return servletContext;
	}

	@Override
	public AsyncContext startAsync() throws IllegalStateException {
		return null;
	}

	@Override
	public AsyncContext startAsync(ServletRequest request, ServletResponse response) throws IllegalStateException {
		return null;
	}

	@Override
	public boolean isAsyncStarted() {
		return false;
	}

	@Override
	public boolean isAsyncSupported() {
		return false;
	}

	@Override
	public AsyncContext getAsyncContext() {
		return null;
	}

	@Override
	public DispatcherType getDispatcherType() {
		return null;
	}
}