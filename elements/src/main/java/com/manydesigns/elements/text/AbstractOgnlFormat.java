package com.manydesigns.elements.text;

import com.manydesigns.elements.ElementsThreadLocals;
import com.manydesigns.elements.ognl.OgnlUtils;
import ognl.OgnlContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractOgnlFormat {
	public static final Pattern pattern = Pattern.compile("%\\{[^\\}]*\\}");

	protected final String formatString;
	protected final String[] ognlExpressions;
	protected final Object[] parsedOgnlExpressions;

	public static final Logger logger = LoggerFactory.getLogger(AbstractOgnlFormat.class);

	protected AbstractOgnlFormat(String ognlFormat) {
		List<String> ognlExpressionList = new ArrayList<String>();
		List<Object> parsedOgnlExpressionList = new ArrayList<Object>();
		Matcher m = pattern.matcher(ognlFormat);
		int previousEnd = 0;
		StringBuilder sb = new StringBuilder();
		int index = 0;
		while (m.find()) {
			int start = m.start();
			String text = ognlFormat.substring(previousEnd, start);
			String escapedText = escapeText(text);
			sb.append(escapedText);
			String group = m.group();
			String ognlExpression = group.substring(2, group.length() - 1);
			replaceOgnlExpression(sb, index, ognlExpression);
			int end = m.end();
			Object parsedOgnlExpression = OgnlUtils.parseExpressionQuietly(ognlExpression);
			ognlExpressionList.add(ognlExpression);
			parsedOgnlExpressionList.add(parsedOgnlExpression);
			previousEnd = end;
			index++;
		}
		String text = ognlFormat.substring(previousEnd, ognlFormat.length());
		String escapedText = escapeText(text);
		sb.append(escapedText);

		formatString = sb.toString();

		ognlExpressions = new String[ognlExpressionList.size()];
		ognlExpressionList.toArray(ognlExpressions);

		parsedOgnlExpressions = new Object[parsedOgnlExpressionList.size()];
		parsedOgnlExpressionList.toArray(parsedOgnlExpressions);
	}

	protected String escapeText(String text) {
		return text;
	}

	protected abstract void replaceOgnlExpression(StringBuilder sb, int index, String ognlExpression);

	public Object[] evaluateOgnlExpressions(Object root) {
		OgnlContext ognlContext = ElementsThreadLocals.getOgnlContext();
		Object[] result = new Object[parsedOgnlExpressions.length];
		for (int i = 0; i < result.length; i++) {
			evaluateOneOgnlExpression(root, ognlContext, result, i);
		}
		return result;
	}

	protected void evaluateOneOgnlExpression(Object root, Map ognlContext, Object[] result, int i) {
		Object parsedOgnlExpression = parsedOgnlExpressions[i];
		Object ognlResult = OgnlUtils.getValueQuietly(parsedOgnlExpression, ognlContext, root);
		result[i] = ognlResult;
	}

	public String getFormatString() {
		return formatString;
	}

	public String[] getOgnlExpressions() {
		return ognlExpressions;
	}

	public Object[] getParsedOgnlExpressions() {
		return parsedOgnlExpressions;
	}
}