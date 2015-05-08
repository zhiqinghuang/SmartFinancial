package com.manydesigns.elements.fields;

import com.manydesigns.elements.reflection.PropertyAccessor;
import com.manydesigns.elements.Mode;

public class CAPField extends RegExpTextField {

	public static final String regExp = "[0-9]{5}";

	public CAPField(PropertyAccessor accessor, Mode mode) {
		this(accessor, mode, null);
	}

	public CAPField(PropertyAccessor accessor, Mode mode, String prefix) {
		super(accessor, mode, prefix, regExp);
		setErrorString(getText("elements.error.field.cap.format"));
	}
}