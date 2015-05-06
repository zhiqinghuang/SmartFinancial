package com.manydesigns.elements.fields;

import com.manydesigns.elements.Mode;
import com.manydesigns.elements.annotations.FieldSize;
import com.manydesigns.elements.annotations.MaxLength;
import com.manydesigns.elements.reflection.PropertyAccessor;
import com.manydesigns.elements.xml.XhtmlBuffer;
import org.apache.commons.lang.StringUtils;

public abstract class AbstractTextField extends AbstractField {
	protected String stringValue;
	protected boolean autoCapitalize = false;
	protected boolean replaceBadUnicodeCharacters = true;
	protected Integer maxLength = null;
	protected boolean labelPlaceholder;

	protected Integer size;

	public AbstractTextField(PropertyAccessor accessor, Mode mode) {
		this(accessor, mode, null);
	}

	public AbstractTextField(PropertyAccessor accessor, Mode mode, String prefix) {
		super(accessor, mode, prefix);
		if (accessor.isAnnotationPresent(MaxLength.class)) {
			maxLength = accessor.getAnnotation(MaxLength.class).value();
		}
		if (accessor.isAnnotationPresent(FieldSize.class)) {
			size = accessor.getAnnotation(FieldSize.class).value();
		}
	}

	public boolean validate() {
		if (mode.isView(insertable, updatable) || (mode.isBulk() && !bulkChecked)) {
			return true;
		}

		boolean result = true;
		if (required && StringUtils.isBlank(stringValue)) {
			errors.add(getText("elements.error.field.required"));
			result = false;
		}
		if (maxLength != null && StringUtils.length(stringValue) > maxLength) {
			errors.add(getText("elements.error.field.length.exceeded", maxLength));
			result = false;
		}
		return result;
	}

	@Override
	public void labelToXhtml(XhtmlBuffer xb) {
		if (mode.isBulk() || !labelPlaceholder) {
			super.labelToXhtml(xb);
		}
	}

	public void valueToXhtml(XhtmlBuffer xb) {
		if (mode.isView(insertable, updatable)) {
			valueToXhtmlView(xb);
		} else if (mode.isEdit()) {
			valueToXhtmlEdit(xb);
		} else if (mode.isPreview()) {
			valueToXhtmlPreview(xb);
		} else if (mode.isHidden()) {
			xb.writeInputHidden(id, inputName, stringValue);
		} else {
			throw new IllegalStateException("Unknown mode: " + mode);
		}
	}

	protected void valueToXhtmlEdit(XhtmlBuffer xb) {
		xb.writeInputText(id, inputName, stringValue, labelPlaceholder ? label : null, EDITABLE_FIELD_CSS_CLASS, size, maxLength);
		if (mode.isBulk()) {
			xb.writeJavaScript("$(function() { " + "configureBulkEditTextField('" + id + "', '" + bulkCheckboxName + "'); " + "});");
		}
	}

	protected void valueToXhtmlPreview(XhtmlBuffer xb) {
		valueToXhtmlView(xb);
		xb.writeInputHidden(inputName, stringValue);
	}

	protected void valueToXhtmlView(XhtmlBuffer xb) {
		xb.openElement("p");
		xb.addAttribute("class", STATIC_VALUE_CSS_CLASS);
		xb.addAttribute("id", id);
		if (href == null) {
			xb.write(stringValue);
		} else {
			xb.writeAnchor(href, stringValue, null, title);
		}
		xb.closeElement("p");
	}

	public String getStringValue() {
		return stringValue;
	}

	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}

	public boolean isAutoCapitalize() {
		return autoCapitalize;
	}

	public void setAutoCapitalize(boolean autoCapitalize) {
		this.autoCapitalize = autoCapitalize;
	}

	public Integer getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(Integer maxLength) {
		this.maxLength = maxLength;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public boolean isReplaceBadUnicodeCharacters() {
		return replaceBadUnicodeCharacters;
	}

	public void setReplaceBadUnicodeCharacters(boolean replaceBadUnicodeCharacters) {
		this.replaceBadUnicodeCharacters = replaceBadUnicodeCharacters;
	}

	public boolean isLabelPlaceholder() {
		return labelPlaceholder;
	}

	public void setLabelPlaceholder(boolean labelPlaceholder) {
		this.labelPlaceholder = labelPlaceholder;
	}
}
