package com.manydesigns.portofino.dispatcher;

import com.manydesigns.elements.stripes.ElementsActionBeanContext;
import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.Resolution;

public interface PageAction extends ActionBean {
	Resolution preparePage();

	PageInstance getPageInstance();

	void setPageInstance(PageInstance pageInstance);

	@Override
	ElementsActionBeanContext getContext();

	String getReturnUrl();
}