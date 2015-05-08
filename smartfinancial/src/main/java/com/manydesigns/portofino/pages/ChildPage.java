package com.manydesigns.portofino.pages;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(value = XmlAccessType.NONE)
public class ChildPage {
	protected String name;
	protected String container;
	protected String order;
	protected boolean showInNavigation;

	protected int actualOrder;

	public static final Logger logger = LoggerFactory.getLogger(ChildPage.class);

	public ChildPage() {
	}

	public void init() {
		actualOrder = 0;
		if (order != null) {
			try {
				actualOrder = Integer.parseInt(order);
			} catch (NumberFormatException e) {
				logger.warn("Cannot parse value of 'order': " + order, e);
			}
		}
	}

	@XmlAttribute(required = true)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlAttribute(required = false)
	public String getContainer() {
		return container;
	}

	public void setContainer(String container) {
		this.container = container;
	}

	@XmlAttribute(required = false)
	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	@XmlAttribute(required = false)
	public boolean isShowInNavigation() {
		return showInNavigation;
	}

	public void setShowInNavigation(boolean showInNavigation) {
		this.showInNavigation = showInNavigation;
	}

	public int getActualOrder() {
		return actualOrder;
	}
}
