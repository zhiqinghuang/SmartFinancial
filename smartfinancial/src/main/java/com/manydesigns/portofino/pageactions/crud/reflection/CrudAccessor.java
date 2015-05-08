package com.manydesigns.portofino.pageactions.crud.reflection;

import com.manydesigns.elements.reflection.ClassAccessor;
import com.manydesigns.elements.reflection.PropertyAccessor;
import com.manydesigns.portofino.pageactions.crud.configuration.CrudConfiguration;
import com.manydesigns.portofino.pageactions.crud.configuration.CrudProperty;
import com.manydesigns.portofino.reflection.AbstractAnnotatedAccessor;
import org.apache.commons.lang.ArrayUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;

public class CrudAccessor extends AbstractAnnotatedAccessor implements ClassAccessor {

	protected final CrudConfiguration crudConfiguration;
	protected final ClassAccessor tableAccessor;
	protected final CrudPropertyAccessor[] propertyAccessors;
	protected final CrudPropertyAccessor[] keyPropertyAccessors;

	public final static Logger logger = LoggerFactory.getLogger(CrudAccessor.class);

	public CrudAccessor(@NotNull final CrudConfiguration crudConfiguration, @NotNull ClassAccessor tableAccessor) {
		super(null);
		this.crudConfiguration = crudConfiguration;
		this.tableAccessor = tableAccessor;
		PropertyAccessor[] columnAccessors = tableAccessor.getProperties();
		PropertyAccessor[] keyColumnAccessors = tableAccessor.getKeyProperties();

		propertyAccessors = new CrudPropertyAccessor[columnAccessors.length];
		keyPropertyAccessors = new CrudPropertyAccessor[keyColumnAccessors.length];

		int i = 0;
		for (PropertyAccessor columnAccessor : columnAccessors) {
			CrudProperty crudProperty = findCrudPropertyByName(crudConfiguration, columnAccessor.getName());
			boolean inKey = ArrayUtils.contains(keyColumnAccessors, columnAccessor);
			CrudPropertyAccessor propertyAccessor = new CrudPropertyAccessor(crudProperty, columnAccessor, inKey);
			propertyAccessors[i] = propertyAccessor;
			i++;
		}

		i = 0;
		for (PropertyAccessor keyColumnAccessor : keyColumnAccessors) {
			String propertyName = keyColumnAccessor.getName();
			try {
				CrudPropertyAccessor keyPropertyAccessor = getProperty(keyColumnAccessor.getName());
				keyPropertyAccessors[i] = keyPropertyAccessor;
			} catch (NoSuchFieldException e) {
				logger.error("Could not find key property: " + propertyName, e);
			}
			i++;
		}
	}

	public static CrudProperty findCrudPropertyByName(CrudConfiguration crudConfiguration, String propertyName) {
		for (CrudProperty current : crudConfiguration.getProperties()) {
			if (current.getName().equalsIgnoreCase(propertyName)) {
				return current;
			}
		}
		return null;
	}

	public String getName() {
		return crudConfiguration.getName();
	}

	public CrudPropertyAccessor getProperty(String propertyName) throws NoSuchFieldException {
		for (CrudPropertyAccessor current : propertyAccessors) {
			// XXX Alessio verificare
			if (current.getName().equalsIgnoreCase(propertyName)) {
				return current;
			}
		}

		throw new NoSuchFieldException(propertyName + " (of use case " + getName() + ")");
	}

	public PropertyAccessor[] getProperties() {
		return propertyAccessors.clone();
	}

	public PropertyAccessor[] getKeyProperties() {
		return keyPropertyAccessors.clone();
	}

	public Object newInstance() {
		return tableAccessor.newInstance();
	}

	@Override
	public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
		T annotation = super.getAnnotation(annotationClass);
		if (annotation != null) {
			return annotation;
		}
		return tableAccessor.getAnnotation(annotationClass);
	}

	public CrudConfiguration getCrudConfiguration() {
		return crudConfiguration;
	}
}