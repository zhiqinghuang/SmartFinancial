package com.manydesigns.portofino.pageactions;

import com.manydesigns.elements.annotations.CssClass;
import com.manydesigns.elements.annotations.LabelI18N;
import com.manydesigns.elements.annotations.Required;
import com.manydesigns.elements.annotations.Updatable;
import com.manydesigns.elements.util.BootstrapSizes;

public class EditPage {

	@LabelI18N("id")
	@Updatable(false)
	public String id;

	@LabelI18N("title")
	@Required
	@CssClass(BootstrapSizes.FILL_ROW)
	public String title;

	@LabelI18N("description")
	@Required
	@CssClass(BootstrapSizes.FILL_ROW)
	public String description;

	@LabelI18N("template")
	@Required
	public String template;

	@LabelI18N("detail.template")
	@Required
	public String detailTemplate;

	@LabelI18N("apply.template.recursively")
	@Required
	public boolean applyTemplateRecursively;

}
