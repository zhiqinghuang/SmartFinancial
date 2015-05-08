package com.manydesigns.elements.fields.helpers;

import com.manydesigns.elements.ElementsProperties;
import com.manydesigns.elements.Mode;
import com.manydesigns.elements.fields.Field;
import com.manydesigns.elements.fields.search.SearchField;
import com.manydesigns.elements.reflection.ClassAccessor;
import com.manydesigns.elements.reflection.PropertyAccessor;
import com.manydesigns.elements.util.InstanceBuilder;
import com.manydesigns.elements.util.ReflectionUtil;
import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class FieldsManager implements FieldHelper {
	protected static final Configuration elementsConfiguration;
	protected static final FieldsManager manager;

	public static final Logger logger = LoggerFactory.getLogger(FieldsManager.class);

	protected final ArrayList<FieldHelper> helperList;

	static {
		elementsConfiguration = ElementsProperties.getConfiguration();
		String managerClassName = elementsConfiguration.getString(ElementsProperties.FIELDS_MANAGER);
		InstanceBuilder<FieldsManager> builder = new InstanceBuilder<FieldsManager>(FieldsManager.class, FieldsManager.class, logger);
		manager = builder.createInstance(managerClassName);
	}

	public static FieldsManager getManager() {
		return manager;
	}

	public FieldsManager() {
		helperList = new ArrayList<FieldHelper>();
		String[] fields = elementsConfiguration.getStringArray(ElementsProperties.FIELDS_LIST);
		if (fields == null) {
			logger.debug("Empty list");
			return;
		}

		for (String current : fields) {
			addFieldHelper(current);
		}
	}

	public void addFieldHelper(String fieldHelperClassName) {
		String helperClassName = fieldHelperClassName.trim();
		logger.debug("Adding field helper: {}", helperClassName);
		FieldHelper helper = (FieldHelper) ReflectionUtil.newInstance(helperClassName);
		if (helper == null) {
			logger.debug("Failed to add field helper: {}", helperClassName);
		} else {
			helperList.add(helper);
			logger.debug("Added field helper: {}", helper);
		}
	}

	public Field tryToInstantiateField(ClassAccessor classAccessor, PropertyAccessor propertyAccessor, Mode mode, String prefix) {
		for (FieldHelper current : helperList) {
			Field result = current.tryToInstantiateField(classAccessor, propertyAccessor, mode, prefix);
			if (result != null) {
				return result;
			}
		}
		return null;
	}

	public SearchField tryToInstantiateSearchField(ClassAccessor classAccessor, PropertyAccessor propertyAccessor, String prefix) {
		for (FieldHelper current : helperList) {
			SearchField result = current.tryToInstantiateSearchField(classAccessor, propertyAccessor, prefix);
			if (result != null) {
				return result;
			}
		}
		return null;
	}

	public ArrayList<FieldHelper> getHelperList() {
		return helperList;
	}
}