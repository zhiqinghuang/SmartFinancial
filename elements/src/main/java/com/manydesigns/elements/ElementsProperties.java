package com.manydesigns.elements;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ElementsProperties {

	public final static String PROPERTIES_RESOURCE = "elements.properties";
	public final static String CUSTOM_PROPERTIES_RESOURCE = "elements-custom.properties";

	public static final String FIELDS_MANAGER = "fields.manager";
	public static final String FIELDS_LIST = "fields.list";

	public static final String ANNOTATIONS_MANAGER = "annotations.manager";
	public static final String ANNOTATIONS_IMPLEMENTATION_LIST = "annotations.implementation.list";

	public static final String FIELDS_LABEL_CAPITALIZE = "fields.label.capitalize";
	public static final String FIELDS_DATE_FORMAT = "fields.date.format";

	private static final CompositeConfiguration configuration;

	public static final Logger logger = LoggerFactory.getLogger(ElementsProperties.class);

	static {
		configuration = new CompositeConfiguration();
		addConfiguration(CUSTOM_PROPERTIES_RESOURCE);
		addConfiguration(PROPERTIES_RESOURCE);
	}

	public static void addConfiguration(String resource) {
		try {
			configuration.addConfiguration(new PropertiesConfiguration(resource));
		} catch (Throwable e) {
			logger.warn(String.format("Error loading properties from: %s", resource), e);
		}
	}

	public static Configuration getConfiguration() {
		return configuration;
	}

	private ElementsProperties() {
	}

}