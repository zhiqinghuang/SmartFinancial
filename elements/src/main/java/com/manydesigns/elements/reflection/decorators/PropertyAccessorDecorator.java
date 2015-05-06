package com.manydesigns.elements.reflection.decorators;

import com.manydesigns.elements.reflection.AbstractAnnotatedAccessor;
import com.manydesigns.elements.reflection.PropertyAccessor;

import java.lang.annotation.Annotation;

public class PropertyAccessorDecorator extends AbstractAnnotatedAccessor implements PropertyAccessor {

	private final PropertyAccessor delegate;

	public PropertyAccessorDecorator(PropertyAccessor delegate, PropertyAccessor decorator) {
		this.delegate = delegate;
		for (Annotation annotation : delegate.getAnnotations()) {
			this.annotations.put(annotation.getClass().getInterfaces()[0], annotation);
		}
		for (Annotation annotation : decorator.getAnnotations()) {
			this.annotations.put(annotation.getClass().getInterfaces()[0], annotation);
		}
	}

	@Override
	public String getName() {
		return delegate.getName();
	}

	@Override
	public Class getType() {
		return delegate.getType();
	}

	@Override
	public int getModifiers() {
		return delegate.getModifiers();
	}

	@Override
	public Object get(Object obj) {
		return delegate.get(obj);
	}

	@Override
	public void set(Object obj, Object value) {
		delegate.set(obj, value);
	}
}