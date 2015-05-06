package com.manydesigns.portofino.pages;

import com.manydesigns.elements.annotations.FieldSize;
import com.manydesigns.elements.annotations.Required;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.annotation.*;

@XmlRootElement
@XmlAccessorType(value = XmlAccessType.NONE)
public class Page {
	protected String id;
	protected String title;
	protected String description;
	protected Layout layout;
	protected Layout detailLayout;
	protected Permissions permissions;
	protected String navigationRoot;

	protected NavigationRoot actualNavigationRoot;

	public static final Logger logger = LoggerFactory.getLogger(Page.class);

	public Page() {
		layout = new Layout();
		detailLayout = new Layout();
		permissions = new Permissions();
	}

	public void init() {
		assert title != null;
		assert description != null;

		if (navigationRoot == null) {
			actualNavigationRoot = NavigationRoot.INHERIT;
			navigationRoot = actualNavigationRoot.name();
		} else {
			actualNavigationRoot = NavigationRoot.valueOf(navigationRoot);
		}

		if (layout != null) {
			layout.init();
		}
		if (detailLayout != null) {
			detailLayout.init();
		}
		if (permissions != null) {
			permissions.init();
		}
	}

	@XmlAttribute(required = true)
	@Required
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@XmlAttribute(required = true)
	@Required
	@FieldSize(50)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@XmlAttribute(required = true)
	@Required
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@XmlElement()
	public Layout getLayout() {
		return layout;
	}

	public void setLayout(Layout layout) {
		this.layout = layout;
	}

	@XmlElement()
	public Layout getDetailLayout() {
		return detailLayout;
	}

	public void setDetailLayout(Layout detailLayout) {
		this.detailLayout = detailLayout;
	}

	@XmlElement()
	public Permissions getPermissions() {
		return permissions;
	}

	public void setPermissions(Permissions permissions) {
		this.permissions = permissions;
	}

	@XmlAttribute(required = true)
	public String getNavigationRoot() {
		return navigationRoot;
	}

	public void setNavigationRoot(String navigationRoot) {
		this.navigationRoot = navigationRoot;
	}

	public NavigationRoot getActualNavigationRoot() {
		return actualNavigationRoot;
	}
}