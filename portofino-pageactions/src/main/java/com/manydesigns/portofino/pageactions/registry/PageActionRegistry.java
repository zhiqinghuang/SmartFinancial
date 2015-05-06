package com.manydesigns.portofino.pageactions.registry;

import com.manydesigns.portofino.dispatcher.PageAction;
import com.manydesigns.portofino.pageactions.PageActionLogic;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class PageActionRegistry implements Iterable<PageActionInfo> {

	protected final List<PageActionInfo> registry = new CopyOnWriteArrayList<PageActionInfo>();

	public PageActionInfo register(Class<?> actionClass) {
		String descriptionKey = PageActionLogic.getDescriptionKey(actionClass);
		Class<?> configurationClass = PageActionLogic.getConfigurationClass(actionClass);
		String scriptTemplate = PageActionLogic.getScriptTemplate(actionClass);
		boolean supportsDetail = PageActionLogic.supportsDetail(actionClass);
		PageActionInfo info = new PageActionInfo(actionClass, configurationClass, scriptTemplate, supportsDetail, descriptionKey);
		registry.add(info);
		return info;
	}

	public Iterator<PageActionInfo> iterator() {
		return registry.iterator();
	}

	public PageActionInfo getInfo(Class<? extends PageAction> actionClass) {
		for (PageActionInfo info : registry) {
			if (info.actionClass == actionClass) {
				return info;
			}
		}
		return null;
	}

}