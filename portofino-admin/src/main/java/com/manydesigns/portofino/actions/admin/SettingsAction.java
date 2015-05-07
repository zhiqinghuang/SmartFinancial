package com.manydesigns.portofino.actions.admin;

import com.manydesigns.elements.ElementsThreadLocals;
import com.manydesigns.elements.annotations.CssClass;
import com.manydesigns.elements.annotations.Label;
import com.manydesigns.elements.annotations.Required;
import com.manydesigns.elements.configuration.CommonsConfigurationUtils;
import com.manydesigns.elements.forms.Form;
import com.manydesigns.elements.forms.FormBuilder;
import com.manydesigns.elements.messages.SessionMessages;
import com.manydesigns.elements.options.SelectionProvider;
import com.manydesigns.elements.util.BootstrapSizes;
import com.manydesigns.portofino.PortofinoProperties;
import com.manydesigns.portofino.buttons.annotations.Button;
import com.manydesigns.portofino.di.Inject;
import com.manydesigns.portofino.dispatcher.DispatcherLogic;
import com.manydesigns.portofino.modules.BaseModule;
import com.manydesigns.portofino.modules.PageactionsModule;
import com.manydesigns.portofino.security.RequiresAdministrator;
import com.manydesigns.portofino.stripes.AbstractActionBean;
import net.sourceforge.stripes.action.*;
import org.apache.commons.configuration.Configuration;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

@RequiresAuthentication
@RequiresAdministrator
@UrlBinding(SettingsAction.URL_BINDING)
public class SettingsAction extends AbstractActionBean {

	public static final String URL_BINDING = "/actions/admin/settings";

	@Inject(BaseModule.PORTOFINO_CONFIGURATION)
	public Configuration configuration;

	@Inject(PageactionsModule.PAGES_DIRECTORY)
	public File pagesDir;

	Form form;

	public final static Logger logger = LoggerFactory.getLogger(SettingsAction.class);

	@DefaultHandler
	public Resolution execute() {
		setupFormAndBean();
		return new ForwardResolution("/m/admin/settings.jsp");
	}

	private void setupFormAndBean() {
		SelectionProvider pagesSelectionProvider = DispatcherLogic.createPagesSelectionProvider(pagesDir);

		Settings settings = new Settings();
		settings.appName = configuration.getString(PortofinoProperties.APP_NAME);
		settings.landingPage = configuration.getString(PortofinoProperties.LANDING_PAGE);
		settings.loginPage = configuration.getString(PortofinoProperties.LOGIN_PAGE);
		settings.preloadGroovyPages = configuration.getBoolean(PortofinoProperties.GROOVY_PRELOAD_PAGES, false);
		settings.preloadGroovyClasses = configuration.getBoolean(PortofinoProperties.GROOVY_PRELOAD_CLASSES, false);

		form = new FormBuilder(Settings.class).configSelectionProvider(pagesSelectionProvider, "landingPage").configSelectionProvider(pagesSelectionProvider, "loginPage").build();

		form.readFromObject(settings);
	}

	@Button(list = "settings", key = "update", order = 1, type = Button.TYPE_PRIMARY)
	public Resolution update() {
		setupFormAndBean();
		form.readFromRequest(context.getRequest());
		if (form.validate()) {
			logger.debug("Applying settings to model");
			try {
				Settings settings = new Settings();
				form.writeToObject(settings);
				configuration.setProperty(PortofinoProperties.APP_NAME, settings.appName);
				configuration.setProperty(PortofinoProperties.LANDING_PAGE, settings.landingPage);
				configuration.setProperty(PortofinoProperties.LOGIN_PAGE, settings.loginPage);
				if (!settings.preloadGroovyPages || configuration.getProperty(PortofinoProperties.GROOVY_PRELOAD_PAGES) != null) {
					configuration.setProperty(PortofinoProperties.GROOVY_PRELOAD_PAGES, settings.preloadGroovyPages);
				}
				if (!settings.preloadGroovyClasses || configuration.getProperty(PortofinoProperties.GROOVY_PRELOAD_CLASSES) != null) {
					configuration.setProperty(PortofinoProperties.GROOVY_PRELOAD_CLASSES, settings.preloadGroovyClasses);
				}
				CommonsConfigurationUtils.save(configuration);
				logger.info("Configuration saved");
			} catch (Exception e) {
				logger.error("Configuration not saved", e);
				SessionMessages.addErrorMessage(ElementsThreadLocals.getText("the.configuration.could.not.be.saved"));
				return new ForwardResolution("/m/admin/settings.jsp");
			}
			SessionMessages.addInfoMessage(ElementsThreadLocals.getText("configuration.updated.successfully"));
			return new RedirectResolution(this.getClass());
		} else {
			return new ForwardResolution("/m/admin/settings.jsp");
		}
	}

	@Button(list = "settings", key = "return.to.pages", order = 2)
	public Resolution returnToPages() {
		return new RedirectResolution("/");
	}

	public Form getForm() {
		return form;
	}

	public static class Settings {

		@Required
		@Label("Application name")
		@CssClass(BootstrapSizes.FILL_ROW)
		public String appName;
		@Required
		public String landingPage;
		@Required
		public String loginPage;
		@Required
		@Label("Preload Groovy pages at startup")
		public boolean preloadGroovyPages;
		@Required
		@Label("Preload Groovy shared classes at startup")
		public boolean preloadGroovyClasses;

	}
}