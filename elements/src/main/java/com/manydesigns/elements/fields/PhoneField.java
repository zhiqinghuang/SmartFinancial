package com.manydesigns.elements.fields;

import com.manydesigns.elements.reflection.PropertyAccessor;
import com.manydesigns.elements.Mode;

public class PhoneField extends RegExpTextField {
	public final static String phoneRegExp = "[0-9+-]+";

	public PhoneField(PropertyAccessor accessor, Mode mode) {
		this(accessor, mode, null);
	}

	public PhoneField(PropertyAccessor accessor, Mode mode, String prefix) {
		super(accessor, mode, prefix, phoneRegExp);
		setErrorString(getText("elements.error.field.phone.format"));
	}
}