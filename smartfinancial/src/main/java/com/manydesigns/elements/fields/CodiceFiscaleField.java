package com.manydesigns.elements.fields;

import com.manydesigns.elements.Mode;
import com.manydesigns.elements.reflection.PropertyAccessor;

public class CodiceFiscaleField extends RegExpTextField {
	// regex per codice fiscale con gestione omocodie.
	public final static String codiceFiscaleRegExp = "[A-Z]{6}[0-9LMNPQRSTUV]{2}[A-Z][0-9LMNPQRSTUV]{2}[A-Z][0-9LMNPQRSTUV]{3}[A-Z]";

	public CodiceFiscaleField(PropertyAccessor accessor, Mode mode, String prefix) {
		super(accessor, mode, prefix, codiceFiscaleRegExp);
		setErrorString(getText("elements.error.field.codice.fiscale.format"));
		setAutoCapitalize(true);
	}
}