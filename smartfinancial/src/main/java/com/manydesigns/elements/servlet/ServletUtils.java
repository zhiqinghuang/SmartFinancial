package com.manydesigns.elements.servlet;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Enumeration;

public class ServletUtils {
	public final static Logger logger = LoggerFactory.getLogger(ServletUtils.class);

	public static void dumpRequestAttributes(HttpServletRequest request) {
		Enumeration<String> attNames = request.getAttributeNames();
		while (attNames.hasMoreElements()) {
			String attrName = (String) attNames.nextElement();
			Object attrValue = request.getAttribute(attrName);
			logger.info("{} = {}", attrName, attrValue);
		}
	}

	/**
	 * Returns the requested path, without the context path. E.g. webapp
	 * deployed under /foo, GET /foo/bar/baz?q=1&k=2, getPath() returns
	 * /bar/baz.
	 * 
	 * @param request
	 *            the HTTP request
	 * @return the path of the requested resource as a path internal to the
	 *         webapp.
	 */
	public static String getPath(HttpServletRequest request) {
		String path = request.getRequestURI();
		String contextPath = request.getContextPath();
		if (path.startsWith(contextPath)) {
			path = path.substring(contextPath.length());
		}
		return path;
	}

	public static String getApplicationBaseUrl(HttpServletRequest req) {
		String scheme = req.getScheme();
		int port = req.getServerPort();
		String portString;
		if ((scheme.equals("http") && port == 80) || (scheme.equals("https") && port == 443)) {
			portString = "";
		} else {
			portString = ":" + port;
		}
		return scheme + "://" + req.getServerName() + portString + req.getContextPath();
	}

	public final static long ONE_YEAR_MILLIS = 365l * 24l * 60l * 60l * 1000l;

	/**
	 * Marks the resource returned to a web client to be stored in cache for a
	 * very long time. The resource is marked to be cached privately, i.e. on
	 * the client only, not in intermediate caches like proxies.
	 * 
	 * @param response
	 *            the HTTP response whose headers are set.
	 */
	public static void markCacheableForever(HttpServletResponse response) {
		long expiresAfterMillis = System.currentTimeMillis() + ONE_YEAR_MILLIS;
		response.setHeader(ServletConstants.HTTP_PRAGMA, "");
		response.setDateHeader(ServletConstants.HTTP_EXPIRES, expiresAfterMillis);
		// Private - only authorized users can cache the content
		response.setHeader(ServletConstants.HTTP_CACHE_CONTROL, ServletConstants.HTTP_CACHE_CONTROL_PRIVATE);
		response.addHeader(ServletConstants.HTTP_CACHE_CONTROL, ServletConstants.HTTP_CACHE_CONTROL_MAX_AGE + ONE_YEAR_MILLIS);
	}

	/**
	 * See <a href=
	 * "http://tomcat.10.n6.nabble.com/Path-parameters-and-getRequestURI-td4377159.html"
	 * >this</a> and <a
	 * href="http://tomcat.markmail.org/thread/ykx72wcuzcmiyujz">this</a>.
	 */
	public static String removePathParameters(String originalPath) {
		String[] tokens = originalPath.split("/");
		for (int i = 0; i < tokens.length; i++) {
			int index = tokens[i].indexOf(";");
			if (index >= 0) {
				tokens[i] = tokens[i].substring(0, index);
			}
		}
		return StringUtils.join(tokens, "/");
	}

	public static String removeRedundantTrailingSlashes(String path) {
		int trimPosition = path.length() - 1;
		while (trimPosition >= 0 && path.charAt(trimPosition) == '/') {
			trimPosition--;
		}
		String withoutTrailingSlashes = path.substring(0, trimPosition + 1);
		while (withoutTrailingSlashes.contains("//")) {
			withoutTrailingSlashes = withoutTrailingSlashes.replace("//", "/");
		}
		return withoutTrailingSlashes;
	}
}