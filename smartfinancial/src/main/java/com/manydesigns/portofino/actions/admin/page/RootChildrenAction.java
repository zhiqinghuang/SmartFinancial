package com.manydesigns.portofino.actions.admin.page;

import java.io.File;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

import org.apache.shiro.authz.annotation.RequiresAuthentication;

import com.manydesigns.portofino.buttons.annotations.Button;
import com.manydesigns.portofino.di.Inject;
import com.manydesigns.portofino.modules.PageactionsModule;
import com.manydesigns.portofino.security.RequiresAdministrator;

@RequiresAuthentication
@RequiresAdministrator
@UrlBinding(RootChildrenAction.URL_BINDING)
public class RootChildrenAction extends RootConfigurationAction {

	public static final String URL_BINDING = "/actions/admin/root-page/children";

	@Inject(PageactionsModule.PAGES_DIRECTORY)
	public File pagesDir;

	@Override
	@DefaultHandler
	public Resolution pageChildren() {
		return super.pageChildren();
	}

	@Override
	protected Resolution forwardToPageChildren() {
		return new ForwardResolution("/m/admin/page/rootChildren.jsp");
	}

	@Override
	@Button(list = "root-children", key = "update", order = 1, type = Button.TYPE_PRIMARY)
	public Resolution updatePageChildren() {
		return super.updatePageChildren();
	}

	@Override
	protected void setupChildPages() {
		childPagesForm = setupChildPagesForm(childPages, pagesDir, getPage().getLayout(), "");
	}

	@Override
	protected String[] getChildPagesFormFields() {
		return new String[] { "active", "name", "title", "showInNavigation", };
	}

}