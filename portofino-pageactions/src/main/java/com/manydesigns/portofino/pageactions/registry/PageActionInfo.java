package com.manydesigns.portofino.pageactions.registry;

public class PageActionInfo {
	public final Class<?> actionClass;
	public final Class<?> configurationClass;
	public final String scriptTemplate;
	public final boolean supportsDetail;
	public final String description;

	public PageActionInfo(Class<?> actionClass, Class<?> configurationClass, String scriptTemplate, boolean supportsDetail, String description) {
		this.actionClass = actionClass;
		this.configurationClass = configurationClass;
		this.scriptTemplate = scriptTemplate;
		this.supportsDetail = supportsDetail;
		this.description = description;
	}
}