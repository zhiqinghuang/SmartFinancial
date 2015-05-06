package com.manydesigns.elements.fields;

import com.manydesigns.elements.FormElement;
import com.manydesigns.elements.reflection.PropertyAccessor;
import com.manydesigns.elements.xml.XhtmlBuffer;

public interface Field extends FormElement {

	PropertyAccessor getPropertyAccessor();

	void valueToXhtml(XhtmlBuffer xb);

	void labelToXhtml(XhtmlBuffer xb);

	void helpToXhtml(XhtmlBuffer xb);

	void errorsToXhtml(XhtmlBuffer xb);

	public String getId();

	void setId(String id);

	String getInputName();

	void setInputName(String inputName);

	boolean isRequired();

	void setRequired(boolean required);

	String getHref();

	void setHref(String href);

	String getTitle();

	void setTitle(String alt);

	String getStringValue();

	boolean isEnabled();

	void setEnabled(boolean enabled);

	boolean isInsertable();

	void setInsertable(boolean insertable);

	boolean isUpdatable();

	void setUpdatable(boolean updatable);

	Object getValue();

	String getDisplayValue();
}