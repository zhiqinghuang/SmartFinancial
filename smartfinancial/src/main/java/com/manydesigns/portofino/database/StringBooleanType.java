package com.manydesigns.portofino.database;

import org.apache.commons.lang.ObjectUtils;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.usertype.EnhancedUserType;
import org.hibernate.usertype.ParameterizedType;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Properties;

public class StringBooleanType implements EnhancedUserType, ParameterizedType {
	private int sqlType = Types.CHAR;

	public static final String NULL = new StringBuilder("null").toString();

	private String trueString = "T";
	private String falseString = "F";

	public int[] sqlTypes() {
		return new int[] { sqlType };
	}

	public Class returnedClass() {
		return Boolean.class;
	}

	public boolean equals(Object x, Object y) throws HibernateException {
		return ObjectUtils.equals(x, y);
	}

	public int hashCode(Object x) throws HibernateException {
		return ObjectUtils.hashCode(x);
	}

	public Object nullSafeGet(ResultSet resultSet, String[] names, SessionImplementor sessionImplementor, Object owner) throws HibernateException, SQLException {
		String value = resultSet.getString(names[0]);
		return parseBoolean(value);
	}

	protected Object parseBoolean(String value) {
		if (value != null) {
			if (value.trim().equalsIgnoreCase(trueString)) {
				return Boolean.TRUE;
			} else if (value.trim().equalsIgnoreCase(falseString)) {
				return Boolean.FALSE;
			} else {
				throw new HibernateException("Invalid boolean value: " + value + "; possible values are " + trueString + ", " + falseString + ", null");
			}
		} else {
			if (trueString == null) {
				return true;
			} else if (falseString == null) {
				return false;
			} else {
				return null;
			}
		}
	}

	public void nullSafeSet(PreparedStatement statement, Object value, int index, SessionImplementor sessionImplementor) throws HibernateException, SQLException {
		if (value == null) {
			if (trueString != null && falseString != null) {
				statement.setString(index, null);
			} else {
				throw new HibernateException("Null is not supported as a boolean value for this type");
			}
		} else {
			statement.setString(index, (Boolean) value ? trueString : falseString);
		}
	}

	public Object deepCopy(Object value) throws HibernateException {
		return value;
	}

	public boolean isMutable() {
		return false;
	}

	public Serializable disassemble(Object value) throws HibernateException {
		return (Serializable) value;
	}

	public Object assemble(Serializable cached, Object owner) throws HibernateException {
		return cached;
	}

	public Object replace(Object original, Object target, Object owner) throws HibernateException {
		return original;
	}

	public String getTrueString() {
		return trueString;
	}

	public String getFalseString() {
		return falseString;
	}

	public void setParameterValues(Properties parameters) {
		// Strings are compared with == and != on purpose
		String nullValue = new String();
		String newString = parameters.getProperty("true", nullValue);
		if (newString != nullValue) {
			trueString = newString != NULL ? newString : null;
		}
		newString = parameters.getProperty("false", nullValue);
		if (newString != nullValue) {
			falseString = newString != NULL ? newString : null;
		}
		if ((trueString == falseString) || ((trueString != null) && trueString.equals(falseString))) {
			throw new IllegalArgumentException("trueString and falseString must be distinct");
		}
		String sqlTypeCode = parameters.getProperty("sqlType");
		if (sqlTypeCode != null) {
			sqlType = Integer.parseInt(sqlTypeCode);
		}
	}

	public String objectToSQLString(Object value) {
		if ((Boolean) value) {
			return '\'' + trueString + '\'';
		} else {
			return '\'' + falseString + '\'';
		}
	}

	public String toXMLString(Object value) {
		if ((Boolean) value) {
			return trueString;
		} else {
			return falseString;
		}
	}

	public Object fromXMLString(String xmlValue) {
		return parseBoolean(xmlValue);
	}
}