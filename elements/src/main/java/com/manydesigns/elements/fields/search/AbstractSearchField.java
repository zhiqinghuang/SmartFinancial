package com.manydesigns.elements.fields.search;

import com.manydesigns.elements.ElementsThreadLocals;
import com.manydesigns.elements.annotations.Id;
import com.manydesigns.elements.annotations.InputName;
import com.manydesigns.elements.annotations.Required;
import com.manydesigns.elements.fields.FieldUtils;
import com.manydesigns.elements.reflection.PropertyAccessor;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public abstract class AbstractSearchField implements SearchField {

	protected final PropertyAccessor accessor;

	protected String id;
	protected String inputName;
	protected String label;
	protected boolean required;

	public static final Logger logger = LoggerFactory.getLogger(AbstractSearchField.class);
	public static final String ATTR_NAME_HTML_CLASS = "control-label";
	public static final String FORM_CONTROL_CSS_CLASS = "form-control";

	public AbstractSearchField(PropertyAccessor accessor) {
		this(accessor, null);
	}

	public AbstractSearchField(PropertyAccessor accessor, String prefix) {
		this.accessor = accessor;

		String localId;
		if (accessor.isAnnotationPresent(Id.class)) {
			localId = accessor.getAnnotation(Id.class).value();
		} else {
			localId = accessor.getName();
		}
		Object[] idArgs = { prefix, localId };
		id = StringUtils.join(idArgs);

		String localInputName;
		if (accessor.isAnnotationPresent(InputName.class)) {
			localInputName = accessor.getAnnotation(InputName.class).value();
		} else {
			localInputName = accessor.getName();
		}
		Object[] inputNameArgs = { prefix, localInputName };
		inputName = StringUtils.join(inputNameArgs);

		label = FieldUtils.getLabel(accessor);

		Required requiredAnnotation = accessor.getAnnotation(Required.class);
		if (requiredAnnotation != null) {
			required = requiredAnnotation.value();
			logger.debug("Required annotation present with value: {}", required);
		}
	}

	public String getText(String key, Object... args) {
		return ElementsThreadLocals.getTextProvider().getText(key, args);
	}

	public void readFromObject(Object obj) {
	}

	public void writeToObject(Object obj) {
	}

	public PropertyAccessor getPropertyAccessor() {
		return accessor;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getInputName() {
		return inputName;
	}

	public void setInputName(String inputName) {
		this.inputName = inputName;
	}

	protected void appendToSearchString(StringBuilder sb, String name, String value, String encoding) {
		if (sb.length() > 0) {
			sb.append(",");
		}
		try {
			sb.append(URLEncoder.encode(name, encoding));
			sb.append("=");
			sb.append(URLEncoder.encode(value, encoding));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
}
