package com.manydesigns.elements.forms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.manydesigns.elements.Mode;
import com.manydesigns.elements.annotations.Enabled;
import com.manydesigns.elements.fields.Field;
import com.manydesigns.elements.fields.SelectField;
import com.manydesigns.elements.fields.helpers.FieldsManager;
import com.manydesigns.elements.options.SelectionProvider;
import com.manydesigns.elements.reflection.ClassAccessor;
import com.manydesigns.elements.reflection.PropertyAccessor;

public class AbstractFormBuilder {
	protected final FieldsManager manager;
	protected final ClassAccessor classAccessor;
	protected final Map<String[], SelectionProvider> selectionProviders;

	protected String prefix;
	protected Mode mode = Mode.EDIT;

	public static final Logger logger = LoggerFactory.getLogger(AbstractFormBuilder.class);

	public AbstractFormBuilder(ClassAccessor classAccessor) {
		logger.debug("Entering AbstractBuilder constructor");

		manager = FieldsManager.getManager();
		this.classAccessor = classAccessor;
		selectionProviders = new HashMap<String[], SelectionProvider>();

		logger.debug("Exiting AbstractBuilder constructor");
	}

	protected void removeUnusedSelectionProviders(Collection<PropertyAccessor> propertyAccessors) {
		List<String> propertyNames = new ArrayList<String>();
		for (PropertyAccessor propertyAccessor : propertyAccessors) {
			propertyNames.add(propertyAccessor.getName());
		}
		List<String[]> removeList = new ArrayList<String[]>();
		for (String[] current : selectionProviders.keySet()) {
			List<String> currentNames = Arrays.asList(current);
			if (!propertyNames.containsAll(currentNames)) {
				removeList.add(current);
			}
		}
		for (String[] current : removeList) {
			selectionProviders.remove(current);
		}
	}

	protected boolean isPropertyEnabled(PropertyAccessor propertyAccessor) {
		// check if field is enabled
		Enabled enabled = propertyAccessor.getAnnotation(Enabled.class);
		if (enabled != null && !enabled.value()) {
			logger.debug("Skipping non-enabled field: {}", propertyAccessor.getName());
			return false;
		}
		return true;
	}

	protected Field buildField(PropertyAccessor propertyAccessor, Field field, String prefix) {
		if (field == null) {
			field = manager.tryToInstantiateField(classAccessor, propertyAccessor, mode, prefix);
		}
		if (field == null) {
			logger.warn("Cannot instantiate field for property {}", propertyAccessor);
		}
		return field;
	}

	protected SelectField buildSelectField(PropertyAccessor propertyAccessor, SelectionProvider selectionProvider, String prefix) {
		return new SelectField(propertyAccessor, selectionProvider, mode, prefix);
	}
}