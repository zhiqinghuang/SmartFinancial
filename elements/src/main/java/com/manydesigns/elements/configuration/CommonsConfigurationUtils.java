package com.manydesigns.elements.configuration;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.FileConfiguration;

public class CommonsConfigurationUtils {

	public static void save(Configuration configuration) throws ConfigurationException {
		FileConfiguration fileConfiguration = getWritableFileConfiguration(configuration);
		if (fileConfiguration == null) {
			throw new ConfigurationException("Cannot save configuration");
		} else {
			fileConfiguration.save();
		}
	}

	public static FileConfiguration getWritableFileConfiguration(Configuration configuration) {
		if (configuration instanceof FileConfiguration) {
			return (FileConfiguration) configuration;
		} else if (configuration instanceof CompositeConfiguration) {
			CompositeConfiguration compositeConfiguration = (CompositeConfiguration) configuration;
			Configuration inMemoryConfigutation = compositeConfiguration.getInMemoryConfiguration();
			return getWritableFileConfiguration(inMemoryConfigutation);
		} else {
			return null;
		}
	}
}