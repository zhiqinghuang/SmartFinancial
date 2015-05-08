package com.manydesigns.portofino.actions.admin;

import com.manydesigns.portofino.di.Inject;
import com.manydesigns.portofino.menu.*;
import com.manydesigns.portofino.modules.AdminModule;
import com.manydesigns.portofino.security.RequiresAdministrator;
import com.manydesigns.portofino.stripes.AbstractActionBean;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RequiresAuthentication
@RequiresAdministrator
@UrlBinding(AdminAction.URL_BINDING)
public class AdminAction extends AbstractActionBean {

	public static final String URL_BINDING = "/actions/admin";

	@Inject(AdminModule.ADMIN_MENU)
	MenuBuilder adminMenu;

	public final static Logger logger = LoggerFactory.getLogger(AdminAction.class);

	@DefaultHandler
	public Resolution execute() {
		Menu menu = adminMenu.build();
		for (MenuItem item : menu.items) {
			if (item instanceof MenuLink) {
				return new RedirectResolution(((MenuLink) item).link);
			} else if (item instanceof MenuGroup) {
				List<MenuLink> menuLinks = ((MenuGroup) item).menuLinks;
				if (!menuLinks.isEmpty()) {
					return new RedirectResolution(menuLinks.get(0).link);
				}
			}
		}
		throw new Error("BUG! There should be at least one registered admin menu item!");
	}
}