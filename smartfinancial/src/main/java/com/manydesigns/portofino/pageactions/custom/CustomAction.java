package com.manydesigns.portofino.pageactions.custom;

import com.manydesigns.elements.ElementsThreadLocals;
import com.manydesigns.elements.messages.SessionMessages;
import com.manydesigns.portofino.buttons.annotations.Button;
import com.manydesigns.portofino.pageactions.AbstractPageAction;
import com.manydesigns.portofino.pageactions.PageActionName;
import com.manydesigns.portofino.pageactions.annotations.ScriptTemplate;
import com.manydesigns.portofino.security.AccessLevel;
import com.manydesigns.portofino.security.RequiresPermissions;
import net.sourceforge.stripes.action.ErrorResolution;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequiresPermissions(level = AccessLevel.VIEW)
@PageActionName("Custom")
@ScriptTemplate("script_template.groovy")
public class CustomAction extends AbstractPageAction {
	public static final Logger logger = LoggerFactory.getLogger(CustomAction.class);

	@Button(list = "pageHeaderButtons", titleKey = "configure", order = 1, icon = Button.ICON_WRENCH)
	@RequiresPermissions(level = AccessLevel.DEVELOP)
	public Resolution configure() {
		prepareConfigurationForms();
		return new ForwardResolution("/m/pageactions/pageactions/custom/configure.jsp");
	}

	@Button(list = "configuration", key = "update.configuration", order = 1, type = Button.TYPE_PRIMARY)
	@RequiresPermissions(level = AccessLevel.DEVELOP)
	public Resolution updateConfiguration() {
		prepareConfigurationForms();
		readPageConfigurationFromRequest();
		boolean valid = validatePageConfiguration();
		if (valid) {
			updatePageConfiguration();
			SessionMessages.addInfoMessage(ElementsThreadLocals.getText("configuration.updated.successfully"));
		}
		return cancel();
	}

	public Resolution preparePage() {
		if (!pageInstance.getParameters().isEmpty()) {
			return new ErrorResolution(404);
		}
		return null;
	}
}