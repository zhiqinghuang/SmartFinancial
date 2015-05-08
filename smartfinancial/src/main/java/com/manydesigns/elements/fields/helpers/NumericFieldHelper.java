package com.manydesigns.elements.fields.helpers;

import com.manydesigns.elements.Mode;
import com.manydesigns.elements.fields.Field;
import com.manydesigns.elements.fields.NumericField;
import com.manydesigns.elements.fields.search.RangeSearchField;
import com.manydesigns.elements.fields.search.SearchField;
import com.manydesigns.elements.reflection.ClassAccessor;
import com.manydesigns.elements.reflection.PropertyAccessor;
import com.manydesigns.elements.util.Util;

public class NumericFieldHelper implements FieldHelper {
	public Field tryToInstantiateField(ClassAccessor classAccessor, PropertyAccessor propertyAccessor, Mode mode, String prefix) {
		Class type = propertyAccessor.getType();
		if (Util.isNumericType(type)) {
			return new NumericField(propertyAccessor, mode, prefix);
		}

		return null;
	}

	public SearchField tryToInstantiateSearchField(ClassAccessor classAccessor, PropertyAccessor propertyAccessor, String prefix) {
		Class type = propertyAccessor.getType();
		if (Util.isNumericType(type)) {
			return new RangeSearchField(propertyAccessor, prefix);
		}

		return null;
	}
}