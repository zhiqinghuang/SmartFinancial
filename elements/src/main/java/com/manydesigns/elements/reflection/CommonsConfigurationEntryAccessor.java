package com.manydesigns.elements.reflection;

import com.manydesigns.elements.annotations.Label;
import com.manydesigns.elements.annotations.impl.LabelImpl;
import org.apache.commons.configuration.Configuration;

import java.lang.annotation.Annotation;

public class CommonsConfigurationEntryAccessor implements PropertyAccessor {
	public static final String copyright = "Copyright (c) 2005-2015, ManyDesigns srl";

	protected final String name;
	protected final Label labelAnnotation;

	public CommonsConfigurationEntryAccessor(String name) {
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
		return ((Configuration) obj).getString(name);
	}

	public void set(Object obj, Object value) {
		((Configuration) obj).setProperty(name, value);
	}
}