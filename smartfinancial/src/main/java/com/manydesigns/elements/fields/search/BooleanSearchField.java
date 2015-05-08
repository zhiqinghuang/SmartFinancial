package com.manydesigns.elements.fields.search;

import com.manydesigns.elements.fields.BooleanSearchValue;
import com.manydesigns.elements.reflection.PropertyAccessor;
import com.manydesigns.elements.xml.XhtmlBuffer;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

import javax.servlet.http.HttpServletRequest;

public class BooleanSearchField extends AbstractSearchField {
	protected BooleanSearchValue value = BooleanSearchValue.ANY;

	public BooleanSearchField(PropertyAccessor accessor) {
		this(accessor, null);
	}

	public BooleanSearchField(PropertyAccessor accessor, String prefix) {
		super(accessor, prefix);
	}

	public void toSearchString(StringBuilder sb, String encoding) {
		if (value != null && value != BooleanSearchValue.ANY) {
			appendToSearchString(sb, inputName, value.getStringValue(), encoding);
		}
	}

	public void configureCriteria(Criteria criteria) {
		if (value == null) {
			return;
		}

		switch (value) {
		case ANY:
			break;
		case NULL:
			criteria.isNull(accessor);
			break;
		case FALSE:
			criteria.eq(accessor, Boolean.FALSE);
			break;
		case TRUE:
			criteria.eq(accessor, Boolean.TRUE);
			break;
		default:
			logger.error("Unknown BooleanSearchValue: {}", value.name());
		}
	}

	public void readFromRequest(HttpServletRequest req) {
		String stringValue = req.getParameter(inputName);
		value = BooleanSearchValue.ANY; // default
		for (BooleanSearchValue current : BooleanSearchValue.values()) {
			if (current.getStringValue().equals(stringValue)) {
				value = current;
			}
		}
	}

	public boolean validate() {
		return true;
	}

	public void toXhtml(@NotNull XhtmlBuffer xb) {
		xb.openElement("div");
		xb.addAttribute("class", "form-group boolean-search-field ");
		xb.openElement("label");
		xb.addAttribute("class", ATTR_NAME_HTML_CLASS);
		xb.write(StringUtils.capitalize(label));
		xb.closeElement("label");
		xb.openElement("div");
		xb.addAttribute("class", FORM_CONTROL_CSS_CLASS);

		for (BooleanSearchValue current : BooleanSearchValue.values()) {
			// don't print null if the attribute is required
			if (required && current == BooleanSearchValue.NULL) {
				continue;
			}
			String idStr = id + "_" + current.name();
			String stringValue = current.getStringValue();
			boolean checked = (value == current);
			xb.openElement("label");
			xb.addAttribute("class", "checkbox");
			xb.addAttribute("for", idStr);
			String label = getText(current.getLabelI18N());
			xb.write(label);
			xb.writeNbsp();
			xb.writeInputRadio(idStr, inputName, stringValue, checked);
			xb.closeElement("label");
			xb.write(" ");
		}
		xb.closeElement("div");
		xb.closeElement("div");
	}

	public BooleanSearchValue getValue() {
		return value;
	}

	public void setValue(BooleanSearchValue value) {
		this.value = value;
	}
}
