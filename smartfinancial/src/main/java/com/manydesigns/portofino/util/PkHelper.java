package com.manydesigns.portofino.util;

import com.manydesigns.elements.ognl.OgnlUtils;
import com.manydesigns.elements.reflection.ClassAccessor;
import com.manydesigns.elements.reflection.PropertyAccessor;
import com.manydesigns.elements.text.OgnlTextFormat;
import com.manydesigns.elements.text.TextFormat;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class PkHelper {
	public final static Logger logger = LoggerFactory.getLogger(PkHelper.class);

	protected final ClassAccessor classAccessor;

	public PkHelper(ClassAccessor classAccessor) {
		this.classAccessor = classAccessor;
	}

	public TextFormat createPkGenerator() {
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (PropertyAccessor property : classAccessor.getKeyProperties()) {
			if (first) {
				first = false;
			} else {
				sb.append("/");
			}
			sb.append("%{");
			sb.append(property.getName());
			sb.append("}");
		}
		return OgnlTextFormat.create(sb.toString());
	}

	public Serializable getPrimaryKey(String... params) {
		int i = 0;
		Serializable result = (Serializable) classAccessor.newInstance();
		if (params.length != classAccessor.getKeyProperties().length) {
			throw new RuntimeException("Wrong number of parameters for primary key: expected " + classAccessor.getKeyProperties().length + ", got " + params.length);
		}
		for (PropertyAccessor property : classAccessor.getKeyProperties()) {
			String stringValue = params[i];
			Object value = OgnlUtils.convertValue(stringValue, property.getType());
			property.set(result, value);
			i++;
		}

		return result;
	}

	public String[] generatePkStringArray(Object object) {
		PropertyAccessor[] keyProperties = classAccessor.getKeyProperties();
		String[] array = new String[keyProperties.length];
		for (int i = 0; i < keyProperties.length; i++) {
			PropertyAccessor property = keyProperties[i];
			Object value = property.get(object);
			String stringValue = (String) OgnlUtils.convertValue(value, String.class);
			array[i] = stringValue;
		}
		return array;
	}

	public String getPkStringForUrl(Object o, String encoding) throws UnsupportedEncodingException {
		return getPkStringForUrl(generatePkStringArray(o), encoding);
	}

	public String getPkStringForUrl(String[] pk, String encoding) throws UnsupportedEncodingException {
		String[] escapedPk = new String[pk.length];
		for (int i = 0; i < pk.length; i++) {
			escapedPk[i] = URLEncoder.encode(pk[i], encoding);
		}
		return StringUtils.join(escapedPk, "/");
	}

}