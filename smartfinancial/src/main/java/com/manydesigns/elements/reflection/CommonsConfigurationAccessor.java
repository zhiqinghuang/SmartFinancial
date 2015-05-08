package com.manydesigns.elements.reflection;

import org.apache.commons.configuration.Configuration;

import java.lang.annotation.Annotation;
import java.util.*;

public class CommonsConfigurationAccessor implements ClassAccessor {
	protected final List<CommonsConfigurationEntryAccessor> accessors;

	public CommonsConfigurationAccessor(Configuration configuration) {
		accessors = new ArrayList<CommonsConfigurationEntryAccessor>();
		int i = 0;
		Iterator keys = configuration.getKeys();
		while (keys.hasNext()) {
			String name = (String) keys.next();
			accessors.add(new CommonsConfigurationEntryAccessor(name));
			i++;
		}

		// sort alphabetically
		Collections.sort(accessors, new Comparator<CommonsConfigurationEntryAccessor>() {
			public int compare(CommonsConfigurationEntryAccessor o1, CommonsConfigurationEntryAccessor o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
	}

	public String getName() {
		return null;
	}

	public PropertyAccessor getProperty(String propertyName) throws NoSuchFieldException {
		for (CommonsConfigurationEntryAccessor current : accessors) {
			if (current.getName().equals(propertyName)) {
				return current;
			}
		}
		throw new NoSuchFieldException(propertyName);
	}

	public PropertyAccessor[] getProperties() {
		PropertyAccessor[] result = new PropertyAccessor[accessors.size()];
		return accessors.toArray(result);
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