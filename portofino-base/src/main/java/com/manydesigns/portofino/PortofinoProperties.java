package com.manydesigns.portofino;

public final class PortofinoProperties {
	// App properties
	public static final String BLOBS_DIR_PATH = "blobs.dir.path";
	public static final String APP_NAME = "app.name";
	public static final String LANDING_PAGE = "landing.page";

	// Server config
	public static final String HOSTNAMES = "portofino.hostnames";
	public static final String URL_ENCODING = "url.encoding";
	public static final String URL_ENCODING_DEFAULT = "UTF-8";
	public static final String TEMP_FILE_SERVICE_CLASS = "temp.file.service.class";

	// Login
	public static final String LOGIN_PAGE = "login.page";

	// The mail address used as the from: field in email messages sent by the
	// application (e.g. password change)
	public static final String MAIL_FROM = "mail.from";

	// Groovy
	public static final String GROOVY_PRELOAD_PAGES = "groovy.preloadPages";
	public static final String GROOVY_PRELOAD_CLASSES = "groovy.preloadClasses";

	private PortofinoProperties() {
	}
}