package com.manydesigns.portofino.breadcrumbs;

public class BreadcrumbItem {

	protected final String href;
	protected final String text;
	protected final String title;

	public BreadcrumbItem(String href, String text, String title) {
		this.href = href;
		this.text = text;
		this.title = title;
	}

	public String getHref() {
		return href;
	}

	public String getText() {
		return text;
	}

	public String getTitle() {
		return title;
	}
}