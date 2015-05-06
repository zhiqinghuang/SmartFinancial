package com.manydesigns.portofino.actions.safemode;

import com.manydesigns.portofino.pageactions.custom.CustomAction;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.Resolution;

public class SafeModeAction extends CustomAction {
	@DefaultHandler
	public Resolution execute() {
		String fwd = "/m/pageactions/safemode/safemode.jsp";
		return forwardTo(fwd);
	}
}