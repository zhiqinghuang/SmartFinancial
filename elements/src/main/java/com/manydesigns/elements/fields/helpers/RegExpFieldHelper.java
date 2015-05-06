package com.manydesigns.elements.fields.helpers;

import com.manydesigns.elements.Mode;
import com.manydesigns.elements.annotations.RegExp;
import com.manydesigns.elements.fields.Field;
import com.manydesigns.elements.fields.RegExpTextField;
import com.manydesigns.elements.fields.search.SearchField;
import com.manydesigns.elements.fields.search.TextMatchMode;
import com.manydesigns.elements.fields.search.TextSearchField;
import com.manydesigns.elements.reflection.ClassAccessor;
import com.manydesigns.elements.reflection.PropertyAccessor;

public class RegExpFieldHelper implements FieldHelper {
	public Field tryToInstantiateField(ClassAccessor classAccessor, PropertyAccessor propertyAccessor, Mode mode, String prefix) {
		RegExp regExp = propertyAccessor.getAnnotation(RegExp.class);
		if (regExp != null && String.class.isAssignableFrom(propertyAccessor.getType())) {
			RegExpTextField field = new RegExpTextField(propertyAccessor, mode, prefix, regExp.value());
			field.setErrorString(field.getText(regExp.errorMessage(), regExp.value()));
			return field;
		}
		return null;
	}

	public SearchField tryToInstantiateSearchField(ClassAccessor classAccessor, PropertyAccessor propertyAccessor, String prefix) {
		if (String.class.isAssignableFrom(propertyAccessor.getType()) && propertyAccessor.isAnnotationPresent(RegExp.class)) {
			TextSearchField textSearchField = new TextSearchField(propertyAccessor, prefix);
			textSearchField.setShowMatchMode(false);
			textSearchField.setMatchMode(TextMatchMode.EQUALS);
			return textSearchField;
		}
		return null;
	}
}