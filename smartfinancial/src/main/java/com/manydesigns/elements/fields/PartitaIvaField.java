package com.manydesigns.elements.fields;

import com.manydesigns.elements.Mode;
import com.manydesigns.elements.reflection.PropertyAccessor;

public class PartitaIvaField extends RegExpTextField {
    public final static String partitaIvaRegExp = "[0-9]{11}";

    public PartitaIvaField(PropertyAccessor accessor, Mode mode, String prefix) {
        super(accessor, mode, prefix, partitaIvaRegExp);
        setErrorString(getText("elements.error.field.partita.iva.format"));
    }
}