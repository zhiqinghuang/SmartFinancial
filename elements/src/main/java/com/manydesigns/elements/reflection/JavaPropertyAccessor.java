package com.manydesigns.elements.reflection;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class JavaPropertyAccessor implements PropertyAccessor {
	private final PropertyDescriptor propertyDescriptor;
	private final Method getter;
	private final Method setter;

	public final static Logger logger = LoggerFactory.getLogger(JavaPropertyAccessor.class);

	public JavaPropertyAccessor(PropertyDescriptor propertyDescriptor) {
		this.propertyDescriptor = propertyDescriptor;
		getter = propertyDescriptor.getReadMethod();
		setter = propertyDescriptor.getWriteMethod();
		if (setter == null) {
			logger.debug("Setter not available for: {}", propertyDescriptor.getName());
		}
	}

	public String getName() {
		return propertyDescriptor.getName();
	}

	public Class getType() {
		return getter.getReturnType();
	}

	public int getModifiers() {
		return getter.getModifiers();
	}

	public Object get(Object obj) {
		try {
			return getter.invoke(obj);
		} catch (IllegalAccessException e) {
			throw new ReflectionException(String.format("Cannot get property: %s", getName()), e);
		} catch (InvocationTargetException e) {
			throw new ReflectionException(String.format("Cannot get property: %s", getName()), e);
		}
	}

	public void set(Object obj, Object value) {
		if (setter == null) {
			throw new ReflectionException(String.format("Setter not available for property: %s", getName()));
		} else {
			try {
				setter.invoke(obj, value);
			} catch (IllegalAccessException e) {
				throw new ReflectionException(String.format("Cannot set property: %s", getName()), e);

			} catch (InvocationTargetException e) {
				throw new ReflectionException(String.format("Cannot set property: %s", getName()), e);
			}
		}
	}

	public boolean isAnnotationPresent(Class<? extends Annotation> annotationClass) {
		return getter.isAnnotationPresent(annotationClass);
	}

	public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
		return getter.getAnnotation(annotationClass);
	}

	public Annotation[] getAnnotations() {
		return getter.getAnnotations();
	}

	public Annotation[] getDeclaredAnnotations() {
		return getter.getDeclaredAnnotations();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("name", getName()).toString();
	}
}