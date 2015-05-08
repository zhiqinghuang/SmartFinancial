package com.manydesigns.elements.configuration;

import com.manydesigns.elements.reflection.ClassAccessor;
import com.manydesigns.elements.reflection.JavaClassAccessor;
import com.manydesigns.elements.reflection.PropertyAccessor;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.text.StrLookup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BeanLookup extends StrLookup {
	protected final Object bean;

	protected final ClassAccessor accessor;

	public static final Logger logger = LoggerFactory.getLogger(BeanLookup.class);

	public BeanLookup(Object bean) {
		this.bean = bean;
		Class clazz = bean.getClass();
		accessor = JavaClassAccessor.getClassAccessor(clazz);
	}

	@Override
	public String lookup(String key) {
		try {
			PropertyAccessor property = accessor.getProperty(key);
			return ObjectUtils.toString(property.get(bean));
		} catch (NoSuchFieldException e) {
			logger.warn("Cannot access property '{}' on class '{}'", key, accessor.getName());
			return null;
		}
	}
}
