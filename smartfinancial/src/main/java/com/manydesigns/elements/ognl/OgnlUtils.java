package com.manydesigns.elements.ognl;

import com.manydesigns.elements.ElementsThreadLocals;
import ognl.Ognl;
import ognl.OgnlContext;
import ognl.OgnlException;
import ognl.TypeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class OgnlUtils {

	public final static Logger logger = LoggerFactory.getLogger(OgnlUtils.class);

	public static Object getValueQuietly(String expression, Map ognlContext, Object root) {
		Object parsedOgnlExpression = parseExpressionQuietly(expression);
		return getValueQuietly(parsedOgnlExpression, ognlContext, root);
	}

	public static Object parseExpressionQuietly(String expression) {
		if (expression == null) {
			logger.warn("Null expression");
			return null;
		}

		Object result;
		try {
			result = Ognl.parseExpression(expression);
		} catch (OgnlException e) {
			result = null;
			logger.warn("Error during parsing of ognl expression: " + expression, e);
		}
		return result;
	}

	public static Object getValueQuietly(Object parsedExpression, Map ognlContext, Object root) {
		if (parsedExpression == null) {
			logger.warn("Null parsed expression");
			return null;
		}

		Object result;
		try {
			if (ognlContext == null) {
				result = Ognl.getValue(parsedExpression, root);
			} else {
				result = Ognl.getValue(parsedExpression, ognlContext, root);
			}
		} catch (OgnlException e) {
			result = null;
			logger.debug("Error during evaluation of ognl expression: " + parsedExpression.toString(), e);
		}
		return result;
	}

	public static String convertValueToString(Object value) {
		return (String) convertValue(value, String.class);
	}

	public static Object convertValue(Object value, Class toType) {
		OgnlContext ognlContext = ElementsThreadLocals.getOgnlContext();
		TypeConverter typeConverter = ognlContext.getTypeConverter();

		return typeConverter.convertValue(ognlContext, null, null, null, value, toType);
	}

	public static Object convertValueQuietly(Object value, Class toType) {
		try {
			return convertValue(value, toType);
		} catch (Throwable e) {
			logger.debug("Error during conversion of value: " + value, e);
			return null;
		}
	}
}