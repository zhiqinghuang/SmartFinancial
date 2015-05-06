package com.manydesigns.elements.forms;

import com.manydesigns.elements.FormElement;
import com.manydesigns.elements.Mode;
import com.manydesigns.elements.composites.AbstractCompositeElement;
import com.manydesigns.elements.fields.MultipartRequestField;
import com.manydesigns.elements.xml.XhtmlBuffer;
import org.jetbrains.annotations.NotNull;

public class FieldSet extends AbstractCompositeElement<FormElement> {

	private static final long serialVersionUID = -5260689697394018257L;
	protected final int nColumns;
	protected final Mode mode;

	protected String name;

	protected int currentColumn;
	protected boolean rowOpened;

	public FieldSet(String name, int nColumns, Mode mode) {
		this.name = name;
		this.nColumns = nColumns;
		this.mode = mode;
		if (nColumns > 4 && nColumns != 6) {
			throw new IllegalArgumentException("nColumns = " + nColumns + " but only 1, 2, 3, 4, 6 columns are supported");
		}
	}

	public void toXhtml(@NotNull XhtmlBuffer xb) {
		if (mode.isHidden()) {
			for (FormElement current : this) {
				current.toXhtml(xb);
			}
		} else {
			xb.openElement("fieldset");
			xb.addAttribute("class", "mde-columns-" + nColumns);
			if (name != null) {
				xb.writeLegend(name, null);
			}

			currentColumn = 0;
			rowOpened = false;

			for (FormElement current : this) {
				int colSpan = Math.min(current.getColSpan(), nColumns);
				if (current.isForceNewRow() || currentColumn + colSpan > nColumns) {
					closeCurrentRow(xb);
				}

				if (currentColumn == 0) {
					xb.openElement("div");
					xb.addAttribute("class", "row");
					rowOpened = true;
				}

				xb.openElement("div");
				xb.addAttribute("class", "col-md-" + (colSpan * (12 / nColumns)) + " mde-colspan-" + colSpan);
				current.toXhtml(xb);
				xb.closeElement("div");

				currentColumn = currentColumn + colSpan;

				if (currentColumn >= nColumns) {
					closeCurrentRow(xb);
				}
			}

			closeCurrentRow(xb);
			xb.closeElement("fieldset");
		}
	}

	protected void closeCurrentRow(XhtmlBuffer xb) {
		if (!rowOpened) {
			return;
		}
		if (currentColumn < nColumns) {
			xb.openElement("div");
			xb.addAttribute("class", "col-md-" + ((nColumns - currentColumn) * (12 / nColumns)));
			xb.closeElement("div");
		}
		xb.closeElement("div");
		currentColumn = 0;
		rowOpened = false;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Mode getMode() {
		return mode;
	}

	public boolean isRequiredFieldsPresent() {
		for (FormElement current : this) {
			if (current.hasRequiredFields()) {
				return true;
			}
		}
		return false;
	}

	public boolean isMultipartRequest() {
		for (FormElement current : this) {
			if (current instanceof MultipartRequestField) {
				MultipartRequestField field = (MultipartRequestField) current;
				if (!field.getMode().isView(field.isInsertable(), field.isUpdatable())) {
					return true;
				}
			}
		}
		return false;
	}
}