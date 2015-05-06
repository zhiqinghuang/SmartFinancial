package com.manydesigns.elements.reflection;

import java.lang.reflect.AnnotatedElement;

public interface PropertyAccessor extends AnnotatedElement {
	public String getName();

	public Class getType();

	public int getModifiers();

	public Object get(Object obj);

	public void set(Object obj, Object value);
}