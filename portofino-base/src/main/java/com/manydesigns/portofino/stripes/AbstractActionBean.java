package com.manydesigns.portofino.stripes;

import com.manydesigns.elements.stripes.ElementsActionBeanContext;
import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;

public abstract class AbstractActionBean implements ActionBean {

	protected ElementsActionBeanContext context;

	public void setContext(ActionBeanContext context) {
		this.context = (ElementsActionBeanContext) context;
	}

	public ElementsActionBeanContext getContext() {
		return context;
	}
}