package com.manydesigns.elements.fields.search;

import com.manydesigns.elements.Element;
import com.manydesigns.elements.reflection.PropertyAccessor;

public interface SearchField extends Element {
	PropertyAccessor getPropertyAccessor();

	void toSearchString(StringBuilder sb, String encoding);

	void configureCriteria(Criteria criteria);
}