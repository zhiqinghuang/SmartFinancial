package com.manydesigns.elements.fields.search;

import com.manydesigns.elements.ognl.OgnlUtils;
import com.manydesigns.elements.reflection.PropertyAccessor;
import com.manydesigns.elements.xml.XhtmlBuffer;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

import javax.servlet.http.HttpServletRequest;

public class RangeSearchField extends AbstractSearchField {

	public final static String MIN_SUFFIX = "_min";
	public final static String MAX_SUFFIX = "_max";

	public final static String NULL_VALUE = "-";

	protected String minId;
	protected String minInputName;
	protected String minStringValue;
	protected Object minValue;

	protected String maxId;
	protected String maxInputName;
	protected String maxStringValue;
	protected Object maxValue;

	protected Integer size = 4;
	protected Integer maxLength = null;

	protected boolean searchNullValue;

	public RangeSearchField(PropertyAccessor accessor) {
		this(accessor, null);
	}

	public RangeSearchField(PropertyAccessor accessor, String prefix) {
		super(accessor, prefix);

		minId = id + MIN_SUFFIX;
		minInputName = inputName + MIN_SUFFIX;

		maxId = id + MAX_SUFFIX;
		maxInputName = inputName + MAX_SUFFIX;
	}

	public void toXhtml(@NotNull XhtmlBuffer xb) {
		xb.openElement("div");
		xb.addAttribute("class", "form-group");
		xb.openElement("div");
		xb.addAttribute("class", "input-group");
		xb.openElement("label");
		xb.addAttribute("for", minId);
		xb.addAttribute("class", ATTR_NAME_HTML_CLASS + " input-group-addon");
		xb.write(StringUtils.capitalize(label));
		xb.closeElement("label");
		rangeEndToXhtml(xb, minId, minInputName, minStringValue, getText("elements.search.range.from"));
		rangeEndToXhtml(xb, maxId, maxInputName, maxStringValue, getText("elements.search.range.to"));
		xb.closeElement("div");
		xb.closeElement("div");
	}

	public void rangeEndToXhtml(XhtmlBuffer xb, String id, String inputName, String stringValue, String label) {
		xb.openElement("label");
		xb.addAttribute("class", "input-group-addon");
		xb.addAttribute("for", id);
		xb.write(label);
		xb.closeElement("label");
		xb.writeInputText(id, inputName, stringValue, FORM_CONTROL_CSS_CLASS, size, maxLength);
		xb.write(" ");
	}

	public void readFromRequest(HttpServletRequest req) {
		Class type = accessor.getType();

		minStringValue = StringUtils.trimToNull(req.getParameter(minInputName));
		try {
			minValue = OgnlUtils.convertValue(minStringValue, type);
		} catch (Throwable e) {
			minValue = null;
		}

		maxStringValue = StringUtils.trimToNull(req.getParameter(maxInputName));
		try {
			maxValue = OgnlUtils.convertValue(maxStringValue, type);
		} catch (Throwable e) {
			maxValue = null;
		}

		searchNullValue = (NULL_VALUE.equals(minStringValue) || NULL_VALUE.equals(maxStringValue));
	}

	public boolean validate() {
		return true;
	}

	public void toSearchString(StringBuilder sb, String encoding) {
		if (minStringValue != null) {
			appendToSearchString(sb, minInputName, minStringValue, encoding);
		}
		if (maxStringValue != null) {
			appendToSearchString(sb, maxInputName, maxStringValue, encoding);
		}
	}

	public void configureCriteria(Criteria criteria) {
		if (searchNullValue) {
			criteria.isNull(accessor);
		} else if (minValue != null && maxValue != null) {
			criteria.between(accessor, minValue, maxValue);
		} else if (minValue != null) {
			criteria.ge(accessor, minValue);
		} else if (maxValue != null) {
			criteria.le(accessor, maxValue);
		}
	}

	public String getMinId() {
		return minId;
	}

	public void setMinId(String minId) {
		this.minId = minId;
	}

	public String getMinInputName() {
		return minInputName;
	}

	public void setMinInputName(String minInputName) {
		this.minInputName = minInputName;
	}

	public String getMinStringValue() {
		return minStringValue;
	}

	public void setMinStringValue(String minStringValue) {
		this.minStringValue = minStringValue;
	}

	public Object getMinValue() {
		return minValue;
	}

	public void setMinValue(Object minValue) {
		this.minValue = minValue;
	}

	public String getMaxId() {
		return maxId;
	}

	public void setMaxId(String maxId) {
		this.maxId = maxId;
	}

	public String getMaxInputName() {
		return maxInputName;
	}

	public void setMaxInputName(String maxInputName) {
		this.maxInputName = maxInputName;
	}

	public String getMaxStringValue() {
		return maxStringValue;
	}

	public void setMaxStringValue(String maxStringValue) {
		this.maxStringValue = maxStringValue;
	}

	public Object getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(Object maxValue) {
		this.maxValue = maxValue;
	}

	public boolean isSearchNullValue() {
		return searchNullValue;
	}

	public void setSearchNullValue(boolean searchNullValue) {
		this.searchNullValue = searchNullValue;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public Integer getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(Integer maxLength) {
		this.maxLength = maxLength;
	}
}