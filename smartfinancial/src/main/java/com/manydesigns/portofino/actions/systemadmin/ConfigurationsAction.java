package com.manydesigns.portofino.actions.systemadmin;

import com.manydesigns.elements.ElementsProperties;
import com.manydesigns.elements.Mode;
import com.manydesigns.elements.forms.Form;
import com.manydesigns.elements.forms.FormBuilder;
import com.manydesigns.elements.reflection.ClassAccessor;
import com.manydesigns.elements.reflection.CommonsConfigurationAccessor;
import com.manydesigns.portofino.modules.BaseModule;
import com.manydesigns.portofino.stripes.AbstractActionBean;
import com.manydesigns.portofino.di.Inject;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.SystemConfiguration;

public class ConfigurationsAction extends AbstractActionBean {

	@Inject(BaseModule.PORTOFINO_CONFIGURATION)
	public Configuration portofinoConfiguration;

	public Form form;

	public String execute() {
		return portofinoConfiguration();
	}

	public String portofinoConfiguration() {
		form = configureForm(portofinoConfiguration);

		return "portofinoConfiguration";
	}

	public String elementsConfiguration() {
		form = configureForm(ElementsProperties.getConfiguration());

		return "elementsConfiguration";
	}

	public String systemConfiguration() {
		form = configureForm(new SystemConfiguration());

		return "systemConfiguration";
	}

	private Form configureForm(Configuration configuration) {
		ClassAccessor accessor = new CommonsConfigurationAccessor(configuration);
		Form form = new FormBuilder(accessor).configMode(Mode.VIEW).build();
		form.readFromObject(configuration);
		return form;
	}
}