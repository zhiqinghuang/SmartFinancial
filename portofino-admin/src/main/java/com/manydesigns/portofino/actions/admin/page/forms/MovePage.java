package com.manydesigns.portofino.actions.admin.page.forms;

import com.manydesigns.elements.annotations.LabelI18N;
import com.manydesigns.elements.annotations.Required;

public class MovePage {
	@Required
	@LabelI18N("where.to.move")
	public String destinationPagePath;
}