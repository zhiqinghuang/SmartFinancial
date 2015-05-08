package com.manydesigns.elements.reflection;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class JavaFieldAccessor implements PropertyAccessor {
	private Field field;

	public JavaFieldAccessor(Field field) {
		assert field != null;
		this.field = field;
	}

	public String getName() {
		return field.getName();
	}

	public Class getType() {
		return field.getType();
	}

	public int getModifiers() {
		return field.getModifiers();
	}

	public Object get(Object obj) {
		try {
			return field.get(obj);
		} catch (IllegalAccessException e) {
			throw new ReflectionException(String.format("Cannot get property: %s", getName()), e);
		}
	}

	public void set(Object obj, Object value) {
		try {
			field.set(obj, value);
		} catch (IllegalAccessException e) {
			throw new ReflectionException(String.format("Cannot set property: %s", getName()), e);
		}
	}

	public boolean isAnnotationPresent(Class<? extends Annotation> annotationClass) {
		return field.isAnnotationPresent(annotationClass);
	}

	public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
		return field.getAnnotation(annotationClass);
	}

	public Annotation[] getAnnotations() {
		return field.getAnnotations();
	}

	public Annotation[] getDeclaredAnnotations() {
		return field.getDeclaredAnnotations();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("name", field.getName()).toString();
	}
}