package com.manydesigns.elements.reflection;

import com.manydesigns.elements.annotations.Key;
import com.manydesigns.elements.util.ReflectionUtil;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

public class JavaClassAccessor implements ClassAccessor {
	public final static String[] PROPERTY_NAME_BLACKLIST = { "class" };

	protected final Class javaClass;
	protected final PropertyAccessor[] propertyAccessors;
	protected final PropertyAccessor[] keyPropertyAccessors;

	protected static final Map<Class, JavaClassAccessor> classAccessorCache;
	public static final Logger logger = LoggerFactory.getLogger(JavaClassAccessor.class);

	static {
		classAccessorCache = new HashMap<Class, JavaClassAccessor>();
	}

	public static JavaClassAccessor getClassAccessor(Class javaClass) {
		JavaClassAccessor cachedResult = classAccessorCache.get(javaClass);
		if (cachedResult == null) {
			logger.debug("Cache miss for: {}", javaClass);
			cachedResult = new JavaClassAccessor(javaClass);
			logger.debug("Caching key: {} - Value: {}", javaClass, cachedResult);
			classAccessorCache.put(javaClass, cachedResult);
		} else {
			logger.debug("Cache hit for: {} - Value: {}", javaClass, cachedResult);
		}
		return cachedResult;
	}

	protected JavaClassAccessor(Class javaClass) {
		this.javaClass = javaClass;

		List<PropertyAccessor> accessorList = setupPropertyAccessors();
		propertyAccessors = new PropertyAccessor[accessorList.size()];
		accessorList.toArray(propertyAccessors);

		List<PropertyAccessor> keyAccessors = setupKeyPropertyAccessors();
		keyPropertyAccessors = new PropertyAccessor[keyAccessors.size()];
		keyAccessors.toArray(keyPropertyAccessors);
	}

	protected List<PropertyAccessor> setupPropertyAccessors() {
		List<PropertyAccessor> accessorList = new ArrayList<PropertyAccessor>();

		// handle properties through introspection
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(javaClass);
			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
			for (PropertyDescriptor current : propertyDescriptors) {
				JavaPropertyAccessor accessor = new JavaPropertyAccessor(current);
				if (isValidProperty(accessor)) {
					accessorList.add(accessor);
				}
			}
		} catch (IntrospectionException e) {
			logger.error(e.getMessage(), e);
		}

		// handle public fields
		for (Field field : javaClass.getFields()) {
			if (isPropertyPresent(accessorList, field.getName())) {
				continue;
			}
			JavaFieldAccessor accessor = new JavaFieldAccessor(field);
			if (isValidProperty(accessor)) {
				accessorList.add(accessor);
			}
		}

		return accessorList;
	}

	protected boolean isValidProperty(PropertyAccessor propertyAccessor) {
		// static field?
		if (Modifier.isStatic(propertyAccessor.getModifiers())) {
			return false;
		}
		// blacklisted?
		if (ArrayUtils.contains(PROPERTY_NAME_BLACKLIST, propertyAccessor.getName())) {
			return false;
		}
		return true;
	}

	protected List<PropertyAccessor> setupKeyPropertyAccessors() {
		Map<String, List<PropertyAccessor>> keys = new HashMap<String, List<PropertyAccessor>>();
		for (PropertyAccessor propertyAccessor : propertyAccessors) {
			Key key = propertyAccessor.getAnnotation(Key.class);
			if (key != null) {
				List<PropertyAccessor> keyProperties = keys.get(key.name());
				if (keyProperties == null) {
					keyProperties = new ArrayList<PropertyAccessor>();
					keys.put(key.name(), keyProperties);
				}
				keyProperties.add(propertyAccessor);
			}
		}

		if (keys.isEmpty()) {
			logger.debug("No primary key configured for {}", javaClass);
			return Collections.emptyList();
		}

		String primaryKeyName;
		List<PropertyAccessor> keyAccessors;
		Key key = getAnnotation(Key.class);
		if (key != null) {
			primaryKeyName = key.name();
		} else {
			primaryKeyName = Key.DEFAULT_NAME;
		}
		keyAccessors = keys.get(primaryKeyName);
		if (keyAccessors == null) {
			keyAccessors = keys.get(Key.DEFAULT_NAME);
		}
		if (keyAccessors == null) {
			logger.debug("Primary key \"" + primaryKeyName + "\" not found in " + javaClass + "; using the first available key.");
			keyAccessors = keys.values().iterator().next();
		}

		Collections.sort(keyAccessors, new Comparator<PropertyAccessor>() {
			public int compare(PropertyAccessor o1, PropertyAccessor o2) {
				Integer ord1 = o1.getAnnotation(Key.class).order();
				Integer ord2 = o2.getAnnotation(Key.class).order();
				return ord1.compareTo(ord2);
			}
		});

		return keyAccessors;
	}

	private boolean isPropertyPresent(List<PropertyAccessor> accessorList, String name) {
		for (PropertyAccessor current : accessorList) {
			if (current.getName().equals(name)) {
				return true;
			}
		}
		return false;
	}

	public String getName() {
		return javaClass.getName();
	}

	public PropertyAccessor getProperty(String propertyName) throws NoSuchFieldException {
		for (PropertyAccessor current : propertyAccessors) {
			if (current.getName().equals(propertyName)) {
				return current;
			}
		}
		throw new NoSuchFieldException(propertyName);
	}

	public PropertyAccessor[] getProperties() {
		return propertyAccessors.clone();
	}

	public PropertyAccessor[] getKeyProperties() {
		return keyPropertyAccessors.clone();
	}

	public Object newInstance() {
		return ReflectionUtil.newInstance(javaClass);
	}

	public boolean isAnnotationPresent(Class<? extends Annotation> annotationClass) {
		return javaClass.isAnnotationPresent(annotationClass);
	}

	public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
		// noinspection unchecked
		return (T) javaClass.getAnnotation(annotationClass);
	}

	public Annotation[] getAnnotations() {
		return javaClass.getAnnotations();
	}

	public Annotation[] getDeclaredAnnotations() {
		return javaClass.getDeclaredAnnotations();
	}

	public Class getJavaClass() {
		return javaClass;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("javaClass", javaClass).toString();
	}
}