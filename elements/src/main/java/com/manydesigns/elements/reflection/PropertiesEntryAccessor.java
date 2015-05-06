package com.manydesigns.elements.reflection;

import com.manydesigns.elements.annotations.Label;
import com.manydesigns.elements.annotations.impl.LabelImpl;

import java.lang.annotation.Annotation;
import java.util.Properties;

public class PropertiesEntryAccessor implements PropertyAccessor {
	protected final String name;
	protected final Label labelAnnotation;

	public PropertiesEntryAccessor(String name) {
		this.name = name;
		labelAnnotation = new LabelImpl(name);
	}

	public String getName() {
		return name;
	}

	public Class getType() {
		return String.class;
	}

	public int getModifiers() {
		return 0;
	}

	public boolean isAnnotationPresent(Class<? extends Annotation> annotationClass) {
		return getAnnotation(annotationClass) != null;
	}

	public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
		if (annotationClass == Label.class) {
			// noinspection unchecked
			return (T) labelAnnotation;
		}
		return null;
	}

	public Annotation[] getAnnotations() {
		Annotation[] result = new Annotation[1];
		result[0] = labelAnnotation;
		return result;
	}

	public Annotation[] getDeclaredAnnotations() {
		return getAnnotations();
	}

	public String get(Object obj) {
		return ((Properties) obj).getProperty(name);
	}

	public void set(Object obj, Object value) {
		((Properties) obj).setProperty(name, (String) value);
	}
}