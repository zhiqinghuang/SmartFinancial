package com.manydesigns.elements.fields;

import com.manydesigns.elements.ElementsProperties;
import com.manydesigns.elements.ElementsThreadLocals;
import com.manydesigns.elements.Mode;
import com.manydesigns.elements.annotations.Password;
import com.manydesigns.elements.reflection.PropertyAccessor;
import com.manydesigns.elements.xml.XhtmlBuffer;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

import javax.servlet.http.HttpServletRequest;

public class PasswordField extends TextField {
	public static final String copyright = "Copyright (c) 2005-2015, ManyDesigns srl";

	public final static String PASSWORD_PLACEHOLDER = "********";

	private String confirmationValue;
	private boolean confirmationRequired = false;

	public PasswordField(PropertyAccessor accessor, Mode mode) {
		this(accessor, mode, null);
		confirmationRequired = accessor.getAnnotation(Password.class).confirmationRequired();
	}

	public PasswordField(PropertyAccessor accessor, Mode mode, String prefix) {
		super(accessor, mode, prefix);
		confirmationRequired = accessor.getAnnotation(Password.class).confirmationRequired();
	}

	@Override
	public void readFromRequest(HttpServletRequest req) {
		if (mode.isView(insertable, updatable)) {
			return;
		}

		String reqValue = req.getParameter(inputName);
		if (reqValue == null) {
			return;
		}

		stringValue = reqValue.trim();

		String confirmationInputName = inputName + "_confirm";

		reqValue = req.getParameter(confirmationInputName);
		if (reqValue == null) {
			return;
		}

		confirmationValue = reqValue.trim();
	}

	@Override
	public boolean validate() {
		if (mode.isView(insertable, updatable) || (mode.isBulk() && !bulkChecked)) {
			return true;
		}

		if (!super.validate()) {
			return false;
		}

		if (!confirmationRequired) {
			return true;
		}

		if (!StringUtils.equals(stringValue, confirmationValue)) {
			errors.add(getText("elements.error.field.passwords.dont.match"));
			return false;
		}

		return true;
	}

	@Override
	public void readFromObject(Object obj) {
		super.readFromObject(obj);
		confirmationValue = stringValue;
	}

	@Override
	public void toXhtml(@NotNull XhtmlBuffer xb) {
		if (mode.isEdit() && (mode.isCreate() || updatable)) { // was !immutable
			openVisibleField(xb);
			valueToXhtml(xb, id, inputName, stringValue);
			helpToXhtml(xb);
			if (confirmationRequired) {
				closeVisibleField(xb);
				// Open another input
				xb.openElement("div");
				String cssClass = "form-group readwrite required";
				if (errors.size() > 0) {
					cssClass += " has-error";
				}
				xb.addAttribute("class", cssClass);
				String confirmationHtmlId = id + "_confirm";
				String confirmationInputName = inputName + "_confirm";
				String confirmLabel = ElementsThreadLocals.getText("elements.field.password.confirm") + " " + label;
				String actualLabel;
				boolean capitalize = elementsConfiguration.getBoolean(ElementsProperties.FIELDS_LABEL_CAPITALIZE);
				if (capitalize) {
					actualLabel = StringUtils.capitalize(confirmLabel);
				} else {
					actualLabel = confirmLabel;
				}
				xb.writeLabel(actualLabel, confirmationHtmlId, FORM_LABEL_CSS_CLASS);
				xb.openElement("div");
				xb.addAttribute("class", INPUT_CONTAINER_CSS_CLASS + " " + fieldCssClass);
				// print out confirmation input field
				valueToXhtml(xb, confirmationHtmlId, confirmationInputName, confirmationValue);
			}
			errorsToXhtml(xb);
			closeVisibleField(xb);
		} else {
			super.toXhtml(xb);
		}
	}

	@Override
	public void labelToXhtml(XhtmlBuffer xb) {
		labelToXhtml(xb, id, label);
	}

	public void labelToXhtml(XhtmlBuffer xb, String actualHtmlId, String actualLabel) {
		xb.openElement("label");
		xb.addAttribute("for", actualHtmlId);
		xb.addAttribute("class", FORM_LABEL_CSS_CLASS);
		xb.write(StringUtils.capitalize(actualLabel));
		xb.closeElement("label");
	}

	@Override
	public void valueToXhtml(XhtmlBuffer xb) {
		valueToXhtml(xb, id, inputName, stringValue);
	}

	public void valueToXhtml(XhtmlBuffer xb, String actualHtmlId, String actualInputName, String actualStringValue) {
		if (mode.isView(insertable, updatable)) {
			valueToXhtmlView(xb);
		} else if (mode.isEdit()) {
			valueToXhtmlEdit(xb, actualHtmlId, actualInputName, actualStringValue);
		} else if (mode.isPreview()) {
			valueToXhtmlPreview(xb, actualInputName, actualStringValue);
		} else if (mode.isHidden()) {
			xb.writeInputHidden(actualInputName, actualStringValue);
		} else {
			throw new IllegalStateException("Unknown mode: " + mode);
		}
	}

	protected void valueToXhtmlPreview(XhtmlBuffer xb, String actualInputName, String actualStringValue) {
		valueToXhtmlView(xb);
		xb.writeInputHidden(actualInputName, actualStringValue);
	}

	protected void valueToXhtmlView(XhtmlBuffer xb) {
		xb.openElement("p");
		xb.addAttribute("class", STATIC_VALUE_CSS_CLASS);
		xb.addAttribute("id", id);
		xb.write(PASSWORD_PLACEHOLDER);
		xb.closeElement("p");
	}

	protected void valueToXhtmlEdit(XhtmlBuffer xb, String actualHtmlId, String actualInputName, String actualStringValue) {
		xb.openElement("input");
		xb.addAttribute("type", "password");
		xb.addAttribute("class", EDITABLE_FIELD_CSS_CLASS);
		xb.addAttribute("id", actualHtmlId);
		xb.addAttribute("name", actualInputName);
		xb.addAttribute("value", actualStringValue);
		if (maxLength != null) {
			int textInputSize = (size != null) && (maxLength > size) ? size : maxLength;
			xb.addAttribute("maxlength", Integer.toString(maxLength));
			xb.addAttribute("size", Integer.toString(textInputSize));
		}
		xb.closeElement("input");
	}

	public String getConfirmationValue() {
		return confirmationValue;
	}

	public void setConfirmationValue(String confirmationValue) {
		this.confirmationValue = confirmationValue;
	}

	public boolean isConfirmationRequired() {
		return confirmationRequired;
	}

	public void setConfirmationRequired(boolean confirmationRequired) {
		this.confirmationRequired = confirmationRequired;
	}
}