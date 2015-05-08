package com.manydesigns.portofino.modules;

import com.manydesigns.portofino.actions.admin.SettingsAction;
import com.manydesigns.portofino.actions.admin.appwizard.ApplicationWizard;
import com.manydesigns.portofino.actions.admin.database.ConnectionProvidersAction;
import com.manydesigns.portofino.actions.admin.database.ReloadModelAction;
import com.manydesigns.portofino.actions.admin.database.TablesAction;
import com.manydesigns.portofino.actions.admin.mail.MailSettingsAction;
import com.manydesigns.portofino.actions.admin.modules.ModulesAction;
import com.manydesigns.portofino.actions.admin.page.RootChildrenAction;
import com.manydesigns.portofino.actions.admin.page.RootPermissionsAction;
import com.manydesigns.portofino.actions.admin.servletcontext.ServletContextAction;
import com.manydesigns.portofino.di.Inject;
import com.manydesigns.portofino.menu.MenuBuilder;
import com.manydesigns.portofino.menu.SimpleMenuAppender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;

public class AdminModule implements Module {

	public final static String ADMIN_MENU = "com.manydesigns.portofino.menu.Menu.admin";

	@Inject(BaseModule.SERVLET_CONTEXT)
	public ServletContext servletContext;

	protected ModuleStatus status = ModuleStatus.CREATED;

	public static final Logger logger = LoggerFactory.getLogger(AdminModule.class);

	@Override
	public String getModuleVersion() {
		return ModuleRegistry.getPortofinoVersion();
	}

	@Override
	public int getMigrationVersion() {
		return 1;
	}

	@Override
	public double getPriority() {
		return 30;
	}

	@Override
	public String getId() {
		return "admin";
	}

	@Override
	public String getName() {
		return "Admin";
	}

	@Override
	public int install() {
		return 1;
	}

	@Override
	public void init() {
		logger.debug("Installing standard menu builders");
		MenuBuilder adminMenu = new MenuBuilder();

		SimpleMenuAppender group;
		SimpleMenuAppender link;

		// General configuration
		group = SimpleMenuAppender.group("configuration", null, "Configuration", 1.0);
		adminMenu.menuAppenders.add(group);

		link = SimpleMenuAppender.link("configuration", "settings", null, "Settings", SettingsAction.URL_BINDING, 1.0);
		adminMenu.menuAppenders.add(link);

		link = SimpleMenuAppender.link("configuration", "modules", null, "Modules", ModulesAction.URL_BINDING, 2.0);
		adminMenu.menuAppenders.add(link);

		link = SimpleMenuAppender.link("configuration", "servlet-context", null, "Servlet Context", ServletContextAction.URL_BINDING, 3.0);
		adminMenu.menuAppenders.add(link);

		link = SimpleMenuAppender.link("configuration", "topLevelPages", null, "Top-level pages", RootChildrenAction.URL_BINDING, 4.0);
		adminMenu.menuAppenders.add(link);

		// Security
		group = SimpleMenuAppender.group("security", null, "Security", 2.0);
		adminMenu.menuAppenders.add(group);

		link = SimpleMenuAppender.link("security", "rootPermissions", null, "Root permissions", RootPermissionsAction.URL_BINDING, 1.0);
		adminMenu.menuAppenders.add(link);

		// Database & modeling
		group = SimpleMenuAppender.group("dataModeling", null, "Data modeling", 3.0);
		adminMenu.menuAppenders.add(group);

		link = SimpleMenuAppender.link("dataModeling", "wizard", null, "Wizard", ApplicationWizard.URL_BINDING, 1.0);
		adminMenu.menuAppenders.add(link);

		link = SimpleMenuAppender.link("dataModeling", "connectionProviders", null, "Connection providers", ConnectionProvidersAction.URL_BINDING, 2.0);
		adminMenu.menuAppenders.add(link);
		link = SimpleMenuAppender.link("dataModeling", "tables", null, "Tables", TablesAction.BASE_ACTION_PATH, 3.0);
		adminMenu.menuAppenders.add(link);
		link = SimpleMenuAppender.link("dataModeling", "reloadModel", null, "Reload model", ReloadModelAction.URL_BINDING, 4.0);
		adminMenu.menuAppenders.add(link);

		// Mail
		group = SimpleMenuAppender.group("mail", null, "Mail", 4.0);
		adminMenu.menuAppenders.add(group);

		link = SimpleMenuAppender.link("mail", "Mail", null, "Mail", MailSettingsAction.URL_BINDING, 1.0);
		adminMenu.menuAppenders.add(link);

		servletContext.setAttribute(ADMIN_MENU, adminMenu);

		status = ModuleStatus.ACTIVE;
	}

	@Override
	public void start() {
		status = ModuleStatus.STARTED;
	}

	@Override
	public void stop() {
		status = ModuleStatus.STOPPED;
	}

	@Override
	public void destroy() {
		status = ModuleStatus.DESTROYED;
	}

	@Override
	public ModuleStatus getStatus() {
		return status;
	}
}