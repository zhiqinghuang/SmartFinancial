package com.manydesigns.elements.fields.search;

import com.manydesigns.elements.ElementsProperties;
import com.manydesigns.elements.annotations.DateFormat;
import com.manydesigns.elements.reflection.PropertyAccessor;
import com.manydesigns.elements.util.Util;
import com.manydesigns.elements.xml.XhtmlBuffer;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;

public class DateSearchField extends RangeSearchField {
	public static final String copyright = "Copyright (c) 2005-2015, ManyDesigns srl";

	protected String datePattern;
	protected DateTimeFormatter dateTimeFormatter;
	protected boolean containsTime;

	public DateSearchField(PropertyAccessor accessor) {
		this(accessor, null);
	}

	public DateSearchField(PropertyAccessor accessor, String prefix) {
		super(accessor, prefix);

		DateFormat dateFormatAnnotation = accessor.getAnnotation(DateFormat.class);
		if (dateFormatAnnotation != null) {
			datePattern = dateFormatAnnotation.value();
		} else {
			Configuration elementsConfiguration = ElementsProperties.getConfiguration();
			datePattern = elementsConfiguration.getString(ElementsProperties.FIELDS_DATE_FORMAT);
		}
		dateTimeFormatter = DateTimeFormat.forPattern(datePattern);
		setSize(dateTimeFormatter.getParser().estimateParsedLength());

		containsTime = datePattern.contains("HH") || datePattern.contains("mm") || datePattern.contains("ss");
	}

	@Override
	public void rangeEndToXhtml(XhtmlBuffer xb, String id, String inputName, String stringValue, String label) {
		// Must be before to avoid breaking Bootstrap input-group
		String js = MessageFormat.format("$(function() '{' setupDatePicker(''#{0}'', ''{1}''); '}');", StringEscapeUtils.escapeJavaScript(id), StringEscapeUtils.escapeJavaScript(datePattern));
		xb.writeJavaScript(js);
		super.rangeEndToXhtml(xb, id, inputName, stringValue, label);
	}

	@Override
	public void readFromRequest(HttpServletRequest req) {
		minStringValue = StringUtils.trimToNull(req.getParameter(minInputName));
		try {
			minValue = Util.parseDateTime(dateTimeFormatter, minStringValue, containsTime);
		} catch (Throwable e) {
			minValue = null;
		}

		maxStringValue = StringUtils.trimToNull(req.getParameter(maxInputName));
		try {
			maxValue = Util.parseDateTime(dateTimeFormatter, maxStringValue, containsTime);
		} catch (Throwable e) {
			maxValue = null;
		}

		searchNullValue = (NULL_VALUE.equals(minStringValue) || NULL_VALUE.equals(maxStringValue));
	}

	public String getDatePattern() {
		return datePattern;
	}

	public void setDatePattern(String datePattern) {
		this.datePattern = datePattern;
	}

	public DateTimeFormatter getDateTimeFormatter() {
		return dateTimeFormatter;
	}

	public void setDateTimeFormatter(DateTimeFormatter dateTimeFormatter) {
		this.dateTimeFormatter = dateTimeFormatter;
	}

	public boolean isContainsTime() {
		return containsTime;
	}

	public void setContainsTime(boolean containsTime) {
		this.containsTime = containsTime;
	}
}