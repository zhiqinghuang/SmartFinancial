package com.manydesigns.elements.fields.helpers;

import com.manydesigns.elements.Mode;
import com.manydesigns.elements.annotations.Select;
import com.manydesigns.elements.fields.Field;
import com.manydesigns.elements.fields.SelectField;
import com.manydesigns.elements.fields.search.SearchField;
import com.manydesigns.elements.fields.search.SelectSearchField;
import com.manydesigns.elements.reflection.ClassAccessor;
import com.manydesigns.elements.reflection.PropertyAccessor;

public class SelectFieldHelper implements FieldHelper {
	public Field tryToInstantiateField(ClassAccessor classAccessor, PropertyAccessor propertyAccessor, Mode mode, String prefix) {
		if (String.class.isAssignableFrom(propertyAccessor.getType()) && propertyAccessor.isAnnotationPresent(Select.class)) {
			return new SelectField(propertyAccessor, mode, prefix);
		} else if (propertyAccessor.getType().isEnum()) {
			return new SelectField(propertyAccessor, mode, prefix);
		}
		return null;
	}

	public SearchField tryToInstantiateSearchField(ClassAccessor classAccessor, PropertyAccessor propertyAccessor, String prefix) {
		if (String.class.isAssignableFrom(propertyAccessor.getType()) && propertyAccessor.isAnnotationPresent(Select.class)) {
			return new SelectSearchField(propertyAccessor, prefix);
		}
		return null;
	}
}