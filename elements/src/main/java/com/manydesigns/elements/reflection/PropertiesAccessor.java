package com.manydesigns.elements.reflection;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Properties;

public class PropertiesAccessor implements ClassAccessor {

	protected final PropertiesEntryAccessor[] accessors;

	public PropertiesAccessor(Properties properties) {
		accessors = new PropertiesEntryAccessor[properties.size()];
		int i = 0;
		for (Object current : properties.keySet()) {
			String name = (String) current;
			accessors[i] = new PropertiesEntryAccessor(name);
			i++;
		}

		// sort alphabetically
		Arrays.sort(accessors, new Comparator<PropertiesEntryAccessor>() {

			public int compare(PropertiesEntryAccessor o1, PropertiesEntryAccessor o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
	}

	public String getName() {
		return null;
	}

	public PropertyAccessor getProperty(String propertyName) throws NoSuchFieldException {
		for (PropertiesEntryAccessor current : accessors) {
			if (current.getName().equals(propertyName)) {
				return current;
			}
		}
		throw new NoSuchFieldException(propertyName);
	}

	public PropertyAccessor[] getProperties() {
		return accessors.clone();
	}

	public PropertyAccessor[] getKeyProperties() {
		return new PropertyAccessor[0];
	}

	public Object newInstance() {
		return null;
	}

	public boolean isAnnotationPresent(Class<? extends Annotation> annotationClass) {
		return false;
	}

	public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
		return null;
	}

	public Annotation[] getAnnotations() {
		return new Annotation[0];
	}

	public Annotation[] getDeclaredAnnotations() {
		return new Annotation[0];
	}
}