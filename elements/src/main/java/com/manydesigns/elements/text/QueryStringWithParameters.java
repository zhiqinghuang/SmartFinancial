package com.manydesigns.elements.text;

import java.io.Serializable;
import java.util.Arrays;

public class QueryStringWithParameters implements Serializable {
	private static final long serialVersionUID = 5447516413086159730L;
	protected final String queryString;
	protected final Object[] parameters;

	public QueryStringWithParameters(String queryString, Object[] parameters) {
		this.queryString = queryString;
		this.parameters = parameters;
	}

	public String getQueryString() {
		return queryString;
	}

	public Object[] getParameters() {
		return parameters;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		QueryStringWithParameters that = (QueryStringWithParameters) o;

		// Probably incorrect - comparing Object[] arrays with Arrays.equals
		if (!Arrays.equals(parameters, that.parameters))
			return false;
		if (queryString != null ? !queryString.equals(that.queryString) : that.queryString != null)
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = queryString != null ? queryString.hashCode() : 0;
		result = 31 * result + (parameters != null ? Arrays.hashCode(parameters) : 0);
		return result;
	}
}