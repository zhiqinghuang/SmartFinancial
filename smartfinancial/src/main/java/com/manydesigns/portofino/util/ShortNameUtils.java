package com.manydesigns.portofino.util;

import com.manydesigns.elements.annotations.ShortName;
import com.manydesigns.elements.reflection.ClassAccessor;
import com.manydesigns.elements.reflection.PropertyAccessor;
import com.manydesigns.elements.text.OgnlTextFormat;

public class ShortNameUtils {
	public static final String PK_ELEMENT_SEPARATOR = " ";

	public static String getName(ClassAccessor classAccessor, Object object) {
		ShortName annotation = classAccessor.getAnnotation(ShortName.class);
		String formatString;
		if (annotation == null) {
			StringBuilder sb = new StringBuilder();
			boolean first = true;
			// sintetizziamo una stringa a partire dalla chiave primaria
			for (PropertyAccessor propertyAccessor : classAccessor.getKeyProperties()) {
				if (first) {
					first = false;
				} else {
					sb.append(PK_ELEMENT_SEPARATOR);
				}
				sb.append(String.format("%%{%s}", propertyAccessor.getName()));
			}
			formatString = sb.toString();
		} else {
			formatString = annotation.value();
		}
		OgnlTextFormat ognlTextFormat = OgnlTextFormat.create(formatString);
		return ognlTextFormat.format(object);
	}
}