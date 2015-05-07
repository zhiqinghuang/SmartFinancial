package com.manydesigns.portofino.actions.admin.database;

import com.manydesigns.elements.ElementsThreadLocals;
import com.manydesigns.elements.messages.SessionMessages;
import com.manydesigns.portofino.buttons.annotations.Button;
import com.manydesigns.portofino.di.Inject;
import com.manydesigns.portofino.modules.DatabaseModule;
import com.manydesigns.portofino.persistence.Persistence;
import com.manydesigns.portofino.security.RequiresAdministrator;
import com.manydesigns.portofino.stripes.AbstractActionBean;
import net.sourceforge.stripes.action.*;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequiresAuthentication
@RequiresAdministrator
@UrlBinding(ReloadModelAction.URL_BINDING)
public class ReloadModelAction extends AbstractActionBean {
	public static final String URL_BINDING = "/actions/admin/reload-model";

	@Inject(DatabaseModule.PERSISTENCE)
	Persistence persistence;

	public final static Logger logger = LoggerFactory.getLogger(ReloadModelAction.class);

	@DefaultHandler
	public Resolution execute() {
		return new ForwardResolution("/m/admin/reload-model.jsp");
	}

	@Button(list = "reload-model", key = "reload", order = 1, type = Button.TYPE_PRIMARY)
	public Resolution reloadModel() {
		synchronized (persistence) {
			persistence.loadXmlModel();
			SessionMessages.addInfoMessage(ElementsThreadLocals.getText("model.successfully.reloaded"));
			return new ForwardResolution("/m/admin/reload-model.jsp");
		}
	}

	@Button(list = "reload-model-bar", key = "return.to.pages", order = 1)
	public Resolution returnToPages() {
		return new RedirectResolution("/");
	}
}