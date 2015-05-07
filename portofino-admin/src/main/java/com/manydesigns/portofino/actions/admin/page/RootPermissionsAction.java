package com.manydesigns.portofino.actions.admin.page;

import com.manydesigns.portofino.buttons.annotations.Button;
import com.manydesigns.portofino.security.RequiresAdministrator;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import org.apache.shiro.authz.annotation.RequiresAuthentication;

@RequiresAuthentication
@RequiresAdministrator
@UrlBinding(RootPermissionsAction.URL_BINDING)
public class RootPermissionsAction extends RootConfigurationAction {
	public static final String URL_BINDING = "/actions/admin/root-page/permissions";

	@Override
	@DefaultHandler
	public Resolution pagePermissions() {
		return super.pagePermissions();
	}

	@Override
	protected Resolution forwardToPagePermissions() {
		return new ForwardResolution("/m/admin/page/rootPermissions.jsp");
	}

	@Button(list = "root-permissions", key = "update", order = 1, type = Button.TYPE_PRIMARY)
	public Resolution updatePagePermissions() {
		return super.updatePagePermissions();
	}
}