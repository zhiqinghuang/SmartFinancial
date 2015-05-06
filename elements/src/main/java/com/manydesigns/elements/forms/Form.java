package com.manydesigns.elements.forms;

import com.manydesigns.elements.Mode;
import com.manydesigns.elements.composites.AbstractCompositeElement;
import com.manydesigns.elements.xml.XhtmlBuffer;
import org.jetbrains.annotations.NotNull;

public class Form extends AbstractCompositeElement<FieldSet> {

	private static final long serialVersionUID = 3060122521819550194L;
	protected final Mode mode;

	public Form(Mode mode) {
		this.mode = mode;
	}

	public void toXhtml(@NotNull XhtmlBuffer xb) {
		for (FieldSet current : this) {
			current.toXhtml(xb);
		}
	}

	public boolean isRequiredFieldsPresent() {
		for (FieldSet current : this) {
			if (current.isRequiredFieldsPresent()) {
				return true;
			}
		}
		return false;
	}

	public boolean isMultipartRequest() {
		for (FieldSet current : this) {
			if (current.isMultipartRequest()) {
				return true;
			}
		}
		return false;
	}

	public Mode getMode() {
		return mode;
	}

}
