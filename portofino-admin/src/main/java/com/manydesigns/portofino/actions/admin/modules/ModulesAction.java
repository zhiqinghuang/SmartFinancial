package com.manydesigns.portofino.actions.admin.modules;

import com.manydesigns.elements.Mode;
import com.manydesigns.elements.annotations.Status;
import com.manydesigns.elements.forms.TableForm;
import com.manydesigns.elements.forms.TableFormBuilder;
import com.manydesigns.portofino.buttons.annotations.Button;
import com.manydesigns.portofino.di.Inject;
import com.manydesigns.portofino.modules.BaseModule;
import com.manydesigns.portofino.modules.Module;
import com.manydesigns.portofino.modules.ModuleRegistry;
import com.manydesigns.portofino.security.RequiresAdministrator;
import com.manydesigns.portofino.stripes.AbstractActionBean;
import net.sourceforge.stripes.action.*;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@RequiresAuthentication
@RequiresAdministrator
@UrlBinding(ModulesAction.URL_BINDING)
public class ModulesAction extends AbstractActionBean {
	public static final String URL_BINDING = "/actions/admin/modules";

	@Inject(BaseModule.MODULE_REGISTRY)
	ModuleRegistry moduleRegistry;

	TableForm form;
	public final static Logger logger = LoggerFactory.getLogger(ModulesAction.class);

	@DefaultHandler
	public Resolution execute() {
		setupForm();
		return new ForwardResolution("/m/admin/modules/list.jsp");
	}

	protected void setupForm() {
		TableFormBuilder builder = new TableFormBuilder(ModuleView.class);
		builder.configNRows(moduleRegistry.getModules().size());
		builder.configMode(Mode.VIEW);
		form = builder.build();
		List<ModuleView> modules = new ArrayList<ModuleView>();
		for (Module module : moduleRegistry.getModules()) {
			ModuleView view = new ModuleView();
			view.id = module.getId();
			view.name = module.getName();
			view.status = module.getStatus().name();
			view.version = module.getModuleVersion();
			modules.add(view);
		}
		form.readFromObject(modules);
	}

	@Button(list = "modules", key = "return.to.pages", order = 2)
	public Resolution returnToPages() {
		return new RedirectResolution("/");
	}

	public TableForm getForm() {
		return form;
	}

	public static final class ModuleView {

		public String id;
		public String name;
		public String version;
		@Status(red = { "FAILED", "DESTROYED" }, amber = { "CREATED", "STOPPED" }, green = { "ACTIVE", "STARTED" })
		public String status;

	}
}