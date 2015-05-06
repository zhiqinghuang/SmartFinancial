package com.manydesigns.elements.annotations;

import com.manydesigns.elements.ElementsProperties;
import com.manydesigns.elements.util.InstanceBuilder;
import com.manydesigns.elements.util.ReflectionUtil;
import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class AnnotationsManager {
	protected static final Configuration elementsConfiguration;
	protected static final AnnotationsManager manager;

	public static final Logger logger = LoggerFactory.getLogger(AnnotationsManager.class);

	protected final Map<Class, Class> annotationClassMap;

	static {
		elementsConfiguration = ElementsProperties.getConfiguration();
		String managerClassName = elementsConfiguration.getString(ElementsProperties.ANNOTATIONS_MANAGER);
		InstanceBuilder<AnnotationsManager> builder = new InstanceBuilder<AnnotationsManager>(AnnotationsManager.class, AnnotationsManager.class, logger);
		manager = builder.createInstance(managerClassName);
	}

	public static AnnotationsManager getManager() {
		return manager;
	}

	public AnnotationsManager() {
		annotationClassMap = new HashMap<Class, Class>();
		Properties mappings = elementsConfiguration.getProperties(ElementsProperties.ANNOTATIONS_IMPLEMENTATION_LIST);
		if (mappings == null) {
			logger.debug("Empty list");
			return;
		}

		for (Map.Entry<Object, Object> mapping : mappings.entrySet()) {
			String annotationName = (String) mapping.getKey();
			String annotationImplName = (String) mapping.getValue();

			addAnnotationMapping(annotationName, annotationImplName);
		}
	}

	public void addAnnotationMapping(String annotationName, String annotationImplName) {
		logger.debug("Mapping annotation {} to implemetation {}", annotationName, annotationImplName);
		Class annotationClass = ReflectionUtil.loadClass(annotationName);
		if (annotationClass == null) {
			logger.warn("Failed to load annotation class: {}", annotationName);
			return;
		}
		if (!annotationClass.isAnnotation()) {
			logger.warn("Not an annotation: {}", annotationName);
			return;
		}
		Class annotationImplClass = ReflectionUtil.loadClass(annotationImplName);
		if (annotationImplClass == null) {
			logger.warn("Failed to load annotation implementation class: {}", annotationImplName);
			return;
		}
		if (!Arrays.asList(annotationImplClass.getInterfaces()).contains(annotationClass)) {
			logger.warn("Class {} not an implementation of {}", annotationImplName, annotationName);
			return;
		}

		annotationClassMap.put(annotationClass, annotationImplClass);
		logger.debug("Mapped annotation {} to implementation {}", annotationName, annotationImplName);

	}

	public Set<Class> getManagedAnnotationClasses() {
		return annotationClassMap.keySet();
	}

	public Class getAnnotationImplementationClass(Class annotationClass) {
		return annotationClassMap.get(annotationClass);
	}
}