package com.manydesigns.portofino.actions.admin.page;

import com.manydesigns.elements.annotations.Insertable;
import com.manydesigns.elements.annotations.LabelI18N;
import com.manydesigns.elements.annotations.Updatable;

public class EditChildPage {
	@Updatable(false)
	@Insertable(false)
	public String name;
	public boolean active;
	@LabelI18N("embed.in.parent")
	public boolean embedded;
	@LabelI18N("show.in.navigation")
	public boolean showInNavigation;
	@Updatable(false)
	@Insertable(false)
	public String title;
}