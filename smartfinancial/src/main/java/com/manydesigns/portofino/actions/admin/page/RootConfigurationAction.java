package com.manydesigns.portofino.actions.admin.page;

import com.manydesigns.portofino.actions.admin.SettingsAction;
import com.manydesigns.portofino.buttons.annotations.Button;
import com.manydesigns.portofino.buttons.annotations.Buttons;
import com.manydesigns.portofino.di.Inject;
import com.manydesigns.portofino.dispatcher.Dispatch;
import com.manydesigns.portofino.dispatcher.DispatcherLogic;
import com.manydesigns.portofino.dispatcher.PageInstance;
import com.manydesigns.portofino.actions.safemode.SafeModeAction;
import com.manydesigns.portofino.modules.PageactionsModule;
import com.manydesigns.portofino.pages.Page;
import com.manydesigns.portofino.security.RequiresAdministrator;
import net.sourceforge.stripes.action.Before;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

@RequiresAdministrator
public abstract class RootConfigurationAction extends PageAdminAction {
	public final static Logger logger = LoggerFactory.getLogger(SettingsAction.class);

	@Inject(PageactionsModule.PAGES_DIRECTORY)
	public File pagesDir;

	@Override
	@Before
	public Resolution prepare() {
		originalPath = "/";
		File rootDir = pagesDir;
		Page rootPage;
		try {
			rootPage = DispatcherLogic.getPage(rootDir);
		} catch (Exception e) {
			throw new Error("Couldn't load root page", e);
		}
		pageInstance = new PageInstance(null, rootDir, rootPage, SafeModeAction.class);
		dispatch = new Dispatch(pageInstance);
		return null;
	}

	@Buttons({ @Button(list = "root-permissions", key = "return.to.pages", order = 2), @Button(list = "root-children", key = "return.to.pages", order = 2) })
	public Resolution returnToPages() {
		return new RedirectResolution("/");
	}
}