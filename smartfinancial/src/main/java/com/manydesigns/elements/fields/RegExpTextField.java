package com.manydesigns.elements.fields;

import com.manydesigns.elements.reflection.PropertyAccessor;
import com.manydesigns.elements.Mode;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegExpTextField extends TextField {
	protected final Pattern pattern;
	protected String errorString;

	public RegExpTextField(PropertyAccessor accessor, Mode mode, String regExp) {
		this(accessor, mode, null, regExp);
	}

	public RegExpTextField(PropertyAccessor accessor, Mode mode, String prefix, String regExp) {
		super(accessor, mode, prefix);
		pattern = Pattern.compile(regExp);
		setErrorString(getText("", regExp));
	}

	@Override
	public boolean validate() {
		if (mode.isView(insertable, updatable) || (mode.isBulk() && !bulkChecked)) {
			return true;
		}

		if (!super.validate()) {
			return false;
		}
		if (stringValue == null || stringValue.length() == 0) {
			return true;
		}
		Matcher matcher = pattern.matcher(stringValue);
		if (!matcher.matches()) {
			errors.add(errorString);
			return false;
		}
		return true;
	}

	public String getErrorString() {
		return errorString;
	}

	public void setErrorString(String errorString) {
		this.errorString = errorString;
	}
}
