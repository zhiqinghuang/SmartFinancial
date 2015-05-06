package com.manydesigns.portofino.modules;

import com.manydesigns.portofino.PortofinoProperties;
import com.manydesigns.portofino.cache.CacheResetListenerRegistry;
import com.manydesigns.portofino.di.Inject;
import com.manydesigns.portofino.files.TempFileService;
import ognl.OgnlRuntime;
import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;

public class BaseModule implements Module {

	public static final Logger logger = LoggerFactory.getLogger(BaseModule.class);
	public static final String GROOVY_SCRIPT_ENGINE = "GROOVY_SCRIPT_ENGINE";
	public static final String GROOVY_CLASS_PATH = "GROOVY_CLASS_PATH";

	protected ModuleStatus status = ModuleStatus.CREATED;

	public final static String SERVLET_CONTEXT = "com.manydesigns.portofino.servletContext";
	public final static String PORTOFINO_CONFIGURATION = "portofinoConfiguration";
	public final static String APPLICATION_DIRECTORY = "com.manydesigns.portofino.application.directory";
	public final static String RESOURCE_BUNDLE_MANAGER = "com.manydesigns.portofino.resourceBundleManager";
	public final static String ELEMENTS_CONFIGURATION = "com.manydesigns.portofino.elementsConfiguration";
	public final static String SERVER_INFO = "com.manydesigns.portofino.serverInfo";
	public final static String CLASS_LOADER = "com.manydesigns.portofino.application.classLoader";
	public final static String MODULE_REGISTRY = "com.manydesigns.portofino.modules.ModuleRegistry";
	public final static String CACHE_RESET_LISTENER_REGISTRY = "com.manydesigns.portofino.cache.CacheResetListenerRegistry";
	public static final String DEFAULT_BLOB_MANAGER = "com.manydesigns.portofino.blobs.DefaultBlobManager";
	public static final String TEMPORARY_BLOB_MANAGER = "com.manydesigns.portofino.blobs.TemporaryBlobManager";

	@Inject(PORTOFINO_CONFIGURATION)
	public Configuration configuration;

	@Inject(SERVLET_CONTEXT)
	public ServletContext servletContext;

	@Override
	public String getModuleVersion() {
		return ModuleRegistry.getPortofinoVersion();
	}

	@Override
	public int getMigrationVersion() {
		return 1;
	}

	@Override
	public double getPriority() {
		return 0;
	}

	@Override
	public String getId() {
		return "base";
	}

	@Override
	public String getName() {
		return "Base";
	}

	@Override
	public int install() {
		return getMigrationVersion();
	}

	@Override
	public void init() {
		logger.debug("Setting up temporary file service");
		String tempFileServiceClass = configuration.getString(PortofinoProperties.TEMP_FILE_SERVICE_CLASS);
		if (tempFileServiceClass != null) {
			try {
				TempFileService.setInstance((TempFileService) Class.forName(tempFileServiceClass).newInstance());
			} catch (Exception e) {
				logger.error("Could not set up temp file service", e);
				throw new Error(e);
			}
		}

		// Disabilitazione security manager per funzionare su GAE. Il security
		// manager permette di valutare
		// in sicurezza espressioni OGNL provenienti da fonti non sicure,
		// configurando i necessari permessi
		// (invoke.<declaring-class>.<method-name>). In Portofino non
		// permettiamo agli utenti finali di valutare
		// espressioni OGNL arbitrarie, pertanto il security manager può essere
		// disabilitato in sicurezza.
		logger.info("Disabling OGNL security manager");
		OgnlRuntime.setSecurityManager(null);

		logger.debug("Installing cache reset listener registry");
		CacheResetListenerRegistry cacheResetListenerRegistry = new CacheResetListenerRegistry();
		servletContext.setAttribute(CACHE_RESET_LISTENER_REGISTRY, cacheResetListenerRegistry);

		status = ModuleStatus.ACTIVE;
	}

	@Override
	public void start() {
		status = ModuleStatus.STARTED;
	}

	@Override
	public void stop() {
		status = ModuleStatus.STOPPED;
	}

	@Override
	public void destroy() {
		status = ModuleStatus.DESTROYED;
	}

	@Override
	public ModuleStatus getStatus() {
		return status;
	}
}