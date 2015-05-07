package com.manydesigns.portofino.i18n;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ResourceBundleManager {

	protected LinkedList<String> searchPaths = new LinkedList<String>();
	protected final ConcurrentMap<Locale, ConfigurationResourceBundle> resourceBundles = new ConcurrentHashMap<Locale, ConfigurationResourceBundle>();

	public static final Logger logger = LoggerFactory.getLogger(ResourceBundleManager.class);

	protected String getBundleFileName(String baseName, Locale locale) {
		return getBundleName(baseName, locale) + ".properties";
	}

	protected String getBundleName(String baseName, Locale locale) {
		String language = locale.getLanguage();
		String country = locale.getCountry();
		String variant = locale.getVariant();

		if (StringUtils.isBlank(language) && StringUtils.isBlank(country) && StringUtils.isBlank(variant)) {
			return baseName;
		}

		String name = baseName + "_";
		if (!StringUtils.isBlank(variant)) {
			name += language + "_" + country + "_" + variant;
		} else if (!StringUtils.isBlank(country)) {
			name += language + "_" + country;
		} else {
			name += language;
		}
		return name;
	}

	public ResourceBundle getBundle(Locale locale) {
		ConfigurationResourceBundle bundle = resourceBundles.get(locale);
		if (bundle == null) {
			CompositeConfiguration configuration = new CompositeConfiguration();
			Iterator<String> iterator = searchPaths.descendingIterator();
			while (iterator.hasNext()) {
				String path = iterator.next();
				int index = path.lastIndexOf('/') + 1;
				String basePath = path.substring(0, index);
				int suffixIndex = path.length() - ".properties".length();
				String resourceBundleBaseName = path.substring(index, suffixIndex);
				String bundleName = getBundleFileName(resourceBundleBaseName, locale);
				PropertiesConfiguration conf;
				try {
					conf = new PropertiesConfiguration();
					conf.setFileName(basePath + bundleName);
					conf.setDelimiterParsingDisabled(true);
					conf.load();
				} catch (ConfigurationException e) {
					logger.debug("Couldn't load resource bundle for locale " + locale + " from " + basePath, e);
					// Fall back to default .properties without _locale
					try {
						String defaultBundleName = basePath + resourceBundleBaseName + ".properties";
						conf = new PropertiesConfiguration();
						conf.setFileName(defaultBundleName);
						conf.setDelimiterParsingDisabled(true);
						conf.load();
					} catch (ConfigurationException e1) {
						logger.debug("Couldn't load default resource bundle from " + basePath, e1);
						conf = null;
					}
				}
				if (conf != null) {
					FileChangedReloadingStrategy reloadingStrategy = new FileChangedReloadingStrategy();
					conf.setReloadingStrategy(reloadingStrategy);
					configuration.addConfiguration(conf);
				}
			}
			bundle = new ConfigurationResourceBundle(configuration, locale);
			// TODO setParent?
			resourceBundles.put(locale, bundle);
		}
		return bundle;
	}

	public void addSearchPath(String searchPath) {
		if (searchPaths.contains(searchPath)) {
			logger.debug("Not adding search path: {}", searchPath);
			return;
		}
		logger.info("Adding search path: {}", searchPath);
		LinkedList<String> newSearchPaths = new LinkedList<String>(searchPaths);
		newSearchPaths.add(searchPath);
		searchPaths = newSearchPaths;
		resourceBundles.clear(); // Clear cache
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(super.toString() + " search paths: \n");
		for (String s : searchPaths) {
			sb.append(s + "\n");
		}
		return sb.toString();
	}
}