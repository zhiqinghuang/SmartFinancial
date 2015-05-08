package com.manydesigns.elements.fields.helpers;

import com.manydesigns.elements.Mode;
import com.manydesigns.elements.fields.BooleanField;
import com.manydesigns.elements.fields.Field;
import com.manydesigns.elements.fields.search.BooleanSearchField;
import com.manydesigns.elements.fields.search.SearchField;
import com.manydesigns.elements.reflection.ClassAccessor;
import com.manydesigns.elements.reflection.PropertyAccessor;

public class BooleanFieldHelper implements FieldHelper {
	public Field tryToInstantiateField(ClassAccessor classAccessor, PropertyAccessor propertyAccessor, Mode mode, String prefix) {
		Field result;
		Class type = propertyAccessor.getType();
		if (type == Boolean.class || type == Boolean.TYPE) {
			result = new BooleanField(propertyAccessor, mode, prefix);
		} else {
			result = null;
		}
		return result;
	}

	public SearchField tryToInstantiateSearchField(ClassAccessor classAccessor, PropertyAccessor propertyAccessor, String prefix) {
		SearchField result;
		Class type = propertyAccessor.getType();
		if (type == Boolean.class || type == Boolean.TYPE) {
			result = new BooleanSearchField(propertyAccessor, prefix);
		} else {
			result = null;
		}
		return result;
	}
}