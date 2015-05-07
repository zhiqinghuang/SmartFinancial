package com.manydesigns.portofino.di;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Injections {
	public static final Logger logger = LoggerFactory.getLogger(Injections.class);

	protected static final Map<Class, Field[]> cache = new HashMap<Class, Field[]>();

	public static void inject(Object obj, @Nullable ServletContext servletContext, @Nullable HttpServletRequest request) {
		if (obj == null) {
			logger.debug("Object is null");
			return;
		}
		Field[] fields = findAnnotatedFields(obj.getClass());
		for (Field field : fields) {
			logger.debug("Found annotated field: " + field);
			Inject annotation = field.getAnnotation(Inject.class);
			assert annotation != null;
			String key = annotation.value();

			logger.debug("Searching in request");
			if (request != null) {
				Object value = request.getAttribute(key);
				if (value != null) {
					logger.debug("Found '{}' in request: {}", key, value);
					setFieldQueitly(obj, field, value);
					continue;
				}
			}

			logger.debug("Searching in session");
			HttpSession session = (request == null) ? null : request.getSession(false);
			if (session != null) {
				Object value = session.getAttribute(key);
				if (value != null) {
					logger.debug("Found '{}' in session: {}", key, value);
					setFieldQueitly(obj, field, value);
					continue;
				}
			}

			logger.debug("Searching in servlet context");
			if (servletContext != null) {
				Object value = servletContext.getAttribute(key);
				if (value != null) {
					logger.debug("Found '{}' in servlet context: {}", key, value);
					setFieldQueitly(obj, field, value);
					continue;
				}
			}

			logger.debug("Value not found. Setting field to null.");
			setFieldQueitly(obj, field, null);
		}
	}

	public static void setFieldQueitly(@NotNull Object obj, @NotNull Field field, @Nullable Object value) {
		try {
			field.set(obj, value);
		} catch (Throwable e) {
			logger.warn("Cannot set field", e);
		}
	}

	public static Field[] findAnnotatedFields(@NotNull Class clazz) {
		Field[] result;
		synchronized (cache) {
			result = cache.get(clazz);
		}

		if (result != null) {
			return result;
		}

		List<Field> foundFields = new ArrayList<Field>();
		Class current = clazz;
		while (current != null) {
			for (Field field : current.getDeclaredFields()) {
				if (field.isAnnotationPresent(Inject.class)) {
					field.setAccessible(true);
					foundFields.add(field);
				}
			}
			current = current.getSuperclass();
		}
		result = new Field[foundFields.size()];
		foundFields.toArray(result);

		synchronized (cache) {
			cache.put(clazz, result);
		}

		return result;
	}
}