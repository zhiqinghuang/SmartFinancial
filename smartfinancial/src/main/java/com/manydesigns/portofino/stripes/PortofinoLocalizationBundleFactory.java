package com.manydesigns.portofino.stripes;

import com.manydesigns.portofino.i18n.ResourceBundleManager;
import com.manydesigns.portofino.modules.BaseModule;
import net.sourceforge.stripes.config.Configuration;
import net.sourceforge.stripes.localization.LocalizationBundleFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class PortofinoLocalizationBundleFactory implements LocalizationBundleFactory {

	public static final Logger logger = LoggerFactory.getLogger(PortofinoLocalizationBundleFactory.class);

	protected ResourceBundleManager resourceBundleManager;

	public ResourceBundle getErrorMessageBundle(Locale locale) throws MissingResourceException {
		return resourceBundleManager.getBundle(locale);
	}

	public ResourceBundle getFormFieldBundle(Locale locale) throws MissingResourceException {
		return resourceBundleManager.getBundle(locale);
	}

	public void init(Configuration configuration) throws Exception {
		ServletContext servletContext = configuration.getServletContext();
		resourceBundleManager = (ResourceBundleManager) servletContext.getAttribute(BaseModule.RESOURCE_BUNDLE_MANAGER);
	}
}