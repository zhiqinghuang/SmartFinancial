package com.manydesigns.elements.fields.helpers;

import com.manydesigns.elements.Mode;
import com.manydesigns.elements.annotations.PartitaIva;
import com.manydesigns.elements.fields.Field;
import com.manydesigns.elements.fields.PartitaIvaField;
import com.manydesigns.elements.fields.search.SearchField;
import com.manydesigns.elements.fields.search.TextMatchMode;
import com.manydesigns.elements.fields.search.TextSearchField;
import com.manydesigns.elements.reflection.ClassAccessor;
import com.manydesigns.elements.reflection.PropertyAccessor;

public class PartitaIvaFieldHelper implements FieldHelper {
	public Field tryToInstantiateField(ClassAccessor classAccessor, PropertyAccessor propertyAccessor, Mode mode, String prefix) {
		if (String.class.isAssignableFrom(propertyAccessor.getType()) && propertyAccessor.isAnnotationPresent(PartitaIva.class)) {
			return new PartitaIvaField(propertyAccessor, mode, prefix);
		}
		return null;
	}

	public SearchField tryToInstantiateSearchField(ClassAccessor classAccessor, PropertyAccessor propertyAccessor, String prefix) {
		if (String.class.isAssignableFrom(propertyAccessor.getType()) && propertyAccessor.isAnnotationPresent(PartitaIva.class)) {
			TextSearchField textSearchField = new TextSearchField(propertyAccessor, prefix);
			textSearchField.setShowMatchMode(false);
			textSearchField.setMatchMode(TextMatchMode.EQUALS);
			return textSearchField;
		}
		return null;
	}
}