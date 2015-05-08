package com.manydesigns.elements.ognl;

import ognl.TypeConverter;

import java.lang.reflect.Member;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;

public class CustomTypeConverter implements TypeConverter {

	private TypeConverter conv;

	public CustomTypeConverter(TypeConverter conv) {
		this.conv = conv;
	}

	public Object convertValue(Map context, Object target, Member member, String propertyName, Object value, Class toType) {
		if ((toType == Boolean.class || toType == Boolean.TYPE) && (value instanceof String)) {
			String thisValue = (String) value;
			return Boolean.valueOf(thisValue);
		} else if ((toType == Timestamp.class) && (value instanceof Date)) {
			Date thisValue = (Date) value;
			return new Timestamp(thisValue.getTime());
		} else if (toType == Timestamp.class) {
			if (value instanceof String) {
				try {
					return Timestamp.valueOf((String) value);
				} catch (Exception e) {
					return throwFailedConversion(value, toType, e);
				}
			} else if (value instanceof Date) {
				return new Timestamp(((Date) value).getTime());
			}
		} else if (toType == java.sql.Date.class) {
			if (value instanceof String) {
				try {
					return java.sql.Date.valueOf((String) value);
				} catch (Exception e) {
					return throwFailedConversion(value, toType, e);
				}
			} else if (value instanceof Date) {
				return new java.sql.Date(((Date) value).getTime());
			}
		} else if ((toType == Time.class) && (value instanceof Date)) {
			Date thisValue = (Date) value;
			return new Time(thisValue.getTime());
		} else if (toType.isEnum() && value instanceof String) {
			return Enum.valueOf(toType, (String) value);
		} else if (toType == Class.class && value instanceof String) {
			try {
				return Class.forName((String) value);
			} catch (Exception e) {
				return throwFailedConversion(value, toType, e);
			}
		}
		return conv.convertValue(context, target, member, propertyName, value, toType);
	}

	private Object throwFailedConversion(Object value, Class toType, Exception e) {
		throw new IllegalArgumentException("Unable to convert type " + value.getClass().getName() + " of " + value + " to type of " + toType.getName(), e);
	}
}