package com.manydesigns.elements.forms;

import com.manydesigns.elements.composites.AbstractCompositeElement;
import com.manydesigns.elements.fields.search.Criteria;
import com.manydesigns.elements.fields.search.SearchField;
import com.manydesigns.elements.reflection.PropertyAccessor;
import com.manydesigns.elements.xml.XhtmlBuffer;
import org.jetbrains.annotations.NotNull;

public class SearchForm extends AbstractCompositeElement<SearchField> {
	public static final String copyright = "Copyright (c) 2005-2015, ManyDesigns srl";

	public void toXhtml(@NotNull XhtmlBuffer xb) {
		xb.openElement("div");
		xb.addAttribute("class", "searchform");

		for (SearchField current : this) {
			current.toXhtml(xb);
		}
		xb.closeElement("div");
	}

	public String toSearchString(String encoding) {
		StringBuilder sb = new StringBuilder();
		for (SearchField current : this) {
			current.toSearchString(sb, encoding);
		}
		return sb.toString();
	}

	public void configureCriteria(Criteria criteria) {
		for (SearchField current : this) {
			current.configureCriteria(criteria);
		}
	}

	public SearchField findSearchFieldByPropertyName(String propertyName) {
		for (SearchField current : this) {
			PropertyAccessor accessor = current.getPropertyAccessor();
			if (accessor.getName().equals(propertyName)) {
				return current;
			}
		}
		return null;
	}
}