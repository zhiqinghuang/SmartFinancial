package com.manydesigns.elements.text;

public class OgnlSqlFormat extends AbstractOgnlFormat {
	public static OgnlSqlFormat create(String queryString) {
		return new OgnlSqlFormat(queryString);
	}

	protected OgnlSqlFormat(String ognlFormat) {
		super(ognlFormat);
	}

	protected void replaceOgnlExpression(StringBuilder sb, int index, String ognlExpression) {
		sb.append("?");
	}
}