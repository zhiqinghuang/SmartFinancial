package com.manydesigns.portofino.pages;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@XmlAccessorType(value = XmlAccessType.NONE)
public class Layout {

	protected String template = "default";
	protected final ArrayList<ChildPage> childPages;

	public static final Logger logger = LoggerFactory.getLogger(Layout.class);

	public Layout() {
		childPages = new ArrayList<ChildPage>();
	}

	public void init() {
		for (ChildPage current : childPages) {
			current.init();
		}
	}

	@XmlAttribute
	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	@XmlElementWrapper(name = "childPages")
	@XmlElement(name = "childPage", type = ChildPage.class)
	public ArrayList<ChildPage> getChildPages() {
		return childPages;
	}
}