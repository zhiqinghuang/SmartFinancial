package com.manydesigns.elements.forms;

import com.manydesigns.elements.annotations.Searchable;
import com.manydesigns.elements.fields.helpers.FieldsManager;
import com.manydesigns.elements.fields.search.SearchField;
import com.manydesigns.elements.fields.search.SelectSearchField;
import com.manydesigns.elements.options.SelectionModel;
import com.manydesigns.elements.options.SelectionProvider;
import com.manydesigns.elements.reflection.ClassAccessor;
import com.manydesigns.elements.reflection.JavaClassAccessor;
import com.manydesigns.elements.reflection.PropertyAccessor;
import org.apache.commons.lang.ArrayUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchFormBuilder extends AbstractFormBuilder {
	protected List<PropertyAccessor> propertyAccessors;

	public SearchFormBuilder(Class aClass) {
		this(JavaClassAccessor.getClassAccessor(aClass));
	}

	public SearchFormBuilder(ClassAccessor classAccessor) {
		super(classAccessor);
	}

	public SearchFormBuilder configFields(String... fieldNames) {
		propertyAccessors = new ArrayList<PropertyAccessor>();
		for (String current : fieldNames) {
			try {
				PropertyAccessor accessor = classAccessor.getProperty(current);
				propertyAccessors.add(accessor);
			} catch (NoSuchFieldException e) {
				logger.warn("Field not found: " + current, e);
			}
		}

		return this;
	}

	public SearchFormBuilder configPrefix(String prefix) {
		logger.debug("prefix = {}", prefix);

		this.prefix = prefix;
		return this;
	}

	public SearchFormBuilder configSelectionProvider(SelectionProvider selectionProvider, String... fieldNames) {
		selectionProviders.put(fieldNames, selectionProvider);
		return this;
	}

	public SearchFormBuilder configReflectiveFields() {
		logger.debug("configReflectiveFields");

		propertyAccessors = new ArrayList<PropertyAccessor>();

		for (PropertyAccessor current : classAccessor.getProperties()) {
			if (!isPropertyEnabled(current)) {
				continue;
			}

			// check if field is searchable
			Searchable searchableAnnotation = current.getAnnotation(Searchable.class);
			if (searchableAnnotation != null && !searchableAnnotation.value()) {
				logger.debug("Skipping non-searchable field: {}", current.getName());
				continue;
			}

			propertyAccessors.add(current);
		}

		return this;
	}

	public SearchForm build() {
		logger.debug("build");

		SearchForm searchForm = new SearchForm();
		FieldsManager manager = FieldsManager.getManager();

		if (propertyAccessors == null) {
			configReflectiveFields();
		}

		Map<String, SearchField> fieldMap = new HashMap<String, SearchField>();
		for (PropertyAccessor propertyAccessor : propertyAccessors) {
			SearchField field = null;
			String fieldName = propertyAccessor.getName();

			for (Map.Entry<String[], SelectionProvider> current : selectionProviders.entrySet()) {

				String[] fieldNames = current.getKey();
				int index = ArrayUtils.indexOf(fieldNames, fieldName);
				if (index >= 0) {
					field = new SelectSearchField(propertyAccessor, current.getValue(), prefix);
					break;
				}
			}

			if (field == null) {
				field = manager.tryToInstantiateSearchField(classAccessor, propertyAccessor, prefix);
			}

			if (field == null) {
				logger.warn("Cannot instanciate field for property {}", propertyAccessor);
				continue;
			}
			fieldMap.put(fieldName, field);
			searchForm.add(field);
		}

		// handle cascaded select fields
		for (Map.Entry<String[], SelectionProvider> current : selectionProviders.entrySet()) {
			String[] fieldNames = current.getKey();
			SelectionProvider selectionProvider = current.getValue();
			SelectionModel selectionModel = selectionProvider.createSelectionModel();

			SelectSearchField previousField = null;
			for (int i = 0; i < fieldNames.length; i++) {
				SelectSearchField selectSearchField = (SelectSearchField) fieldMap.get(fieldNames[i]);
				if (selectSearchField == null) {
					previousField = null;
					continue;
				}
				selectSearchField.setSelectionModel(selectionModel);
				selectSearchField.setSelectionModelIndex(i);
				if (previousField != null) {
					selectSearchField.setPreviousSelectField(previousField);
					previousField.setNextSelectField(selectSearchField);
				}
				previousField = selectSearchField;
			}
		}

		return searchForm;
	}
}