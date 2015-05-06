package com.manydesigns.elements.fields.helpers;

import com.manydesigns.elements.Mode;
import com.manydesigns.elements.fields.Field;
import com.manydesigns.elements.fields.search.SearchField;
import com.manydesigns.elements.reflection.ClassAccessor;
import com.manydesigns.elements.reflection.PropertyAccessor;

public interface FieldHelper {

	Field tryToInstantiateField(ClassAccessor classAccessor, PropertyAccessor propertyAccessor, Mode mode, String prefix);

	SearchField tryToInstantiateSearchField(ClassAccessor classAccessor, PropertyAccessor propertyAccessor, String prefix);
}