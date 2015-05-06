package com.manydesigns.elements.util;

import com.manydesigns.elements.fields.helpers.FieldsManager;
import org.slf4j.Logger;

public class InstanceBuilder<T> {
	private Class<T> clazz;
	private Class defaultImplClass;
	private Logger logger;

	public InstanceBuilder(Class<T> iface, Class<? extends T> defaultImplClass, Logger logger) {
		this.clazz = iface;
		this.defaultImplClass = defaultImplClass;
		this.logger = logger;
	}

	@SuppressWarnings({ "unchecked" })
	public T createInstance(String managerClassName) {
		Class managerClass = ReflectionUtil.loadClass(managerClassName);
		if (managerClass == null) {
			logger.warn("Cannot load class: {}", managerClassName);
			managerClass = defaultImplClass;
		}

		if (!clazz.isAssignableFrom(managerClass)) {
			logger.warn("Cannot use as {}: {}", clazz.getName(), managerClassName);
			managerClass = defaultImplClass;
		}
		logger.debug("Using class: {}", managerClass.getName());

		T instance = (T) ReflectionUtil.newInstance(managerClass);
		if (instance == null) {
			logger.warn("Cannot instanciate: {}. Fall back to default: {}.", managerClass.getName(), FieldsManager.class.getName());
			instance = (T) ReflectionUtil.newInstance(defaultImplClass);
			if (instance == null) {
				logger.error("Cannot instanciate: {}", defaultImplClass.getName());
			}
		}

		logger.debug("Installed {0}: {1}", clazz.getName(), instance);
		return instance;
	}
}