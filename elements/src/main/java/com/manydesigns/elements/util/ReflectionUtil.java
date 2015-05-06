package com.manydesigns.elements.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.lang.reflect.Constructor;

public class ReflectionUtil {
	public final static Logger logger = LoggerFactory.getLogger(ReflectionUtil.class);

	public static Class loadClass(String className) {
		try {
			Class<?> aClass = Class.forName(className);
			logger.debug("Loaded class: {}", aClass);
			return aClass;
		} catch (Throwable e) {
			logger.debug("Could not load class: {}", className);
			return null;
		}
	}

	public static Constructor getConstructor(String className, Class... argClasses) {
		return getConstructor(loadClass(className), argClasses);
	}

	public static Constructor getConstructor(Class aClass, Class... argClasses) {
		try {
			Constructor constructor = aClass.getConstructor(argClasses);
			logger.debug("Found constructor: {}", constructor);
			return constructor;
		} catch (Throwable e) {
			logger.debug("Could not find construtor for class: {}", aClass);
			return null;
		}
	}

	public static Constructor getBestMatchConstructor(Class aClass, Class... argClasses) {
		for (Constructor current : aClass.getConstructors()) {
			Class[] parameterTypes = current.getParameterTypes();
			if (parameterTypes.length != argClasses.length) {
				continue;
			}
			boolean matches = true;
			for (int i = 0; i < argClasses.length; i++) {
				Class paramaterType = parameterTypes[i];
				Class argClass = argClasses[i];
				matches = matches && paramaterType.isAssignableFrom(argClass);
			}
			if (matches) {
				return current;
			}
		}
		logger.debug("Could not find best match construtor for class: {}", aClass);
		return null;
	}

	public static Object newInstance(String className) {
		return newInstance(loadClass(className));
	}

	public static Object newInstance(Class aClass) {
		Constructor constructor = getConstructor(aClass);
		return newInstance(constructor);
	}

	public static Object newInstance(Constructor constructor, Object... args) {
		try {
			return constructor.newInstance(args);
		} catch (Throwable e) {
			logger.debug("Could not instanciate class constructor: {}", constructor);
			return null;
		}
	}

	public static InputStream getResourceAsStream(String resourceName) {
		return ReflectionUtil.class.getClassLoader().getResourceAsStream(resourceName);
	}
}