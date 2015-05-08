package com.manydesigns.elements.reflection;

import java.lang.reflect.AnnotatedElement;

public interface ClassAccessor extends AnnotatedElement {
	public String getName();

	public PropertyAccessor getProperty(String propertyName) throws NoSuchFieldException;

	public PropertyAccessor[] getProperties();

	public PropertyAccessor[] getKeyProperties();

	public Object newInstance();

}