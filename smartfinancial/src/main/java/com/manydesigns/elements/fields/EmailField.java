package com.manydesigns.elements.fields;

import com.manydesigns.elements.reflection.PropertyAccessor;
import com.manydesigns.elements.Mode;

public class EmailField extends RegExpTextField {

	public final static String emailRegExp = "[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";

	public EmailField(PropertyAccessor accessor, Mode mode) {
		this(accessor, mode, null);
	}

	public EmailField(PropertyAccessor accessor, Mode mode, String prefix) {
		super(accessor, mode, prefix, emailRegExp);
		setErrorString(getText("elements.error.field.email.format"));
	}
}