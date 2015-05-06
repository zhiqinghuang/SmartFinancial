package com.manydesigns.portofino.dispatcher;

import com.manydesigns.portofino.pages.Layout;
import com.manydesigns.portofino.pages.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PageInstance {

	protected final Page page;
	protected final File directory;
	protected final List<String> parameters;
	protected final PageInstance parent;
	protected final Class<? extends PageAction> actionClass;
	protected Object configuration;
	protected PageAction actionBean;
	protected String title;
	protected String description;
	protected boolean prepared;

	public static final String DETAIL = "_detail";

	public static final Logger logger = LoggerFactory.getLogger(PageInstance.class);

	public PageInstance(PageInstance parent, File directory, Page page, Class<? extends PageAction> actionClass) {
		this.parent = parent;
		this.directory = directory;
		this.page = page;
		this.actionClass = actionClass;
		parameters = new ArrayList<String>();
		this.title = page.getTitle();
		this.description = page.getDescription();
	}

	public PageInstance copy() {
		PageInstance pageInstance = new PageInstance(parent, directory, page, actionClass);
		pageInstance.prepared = false;
		pageInstance.parameters.addAll(parameters);
		pageInstance.configuration = configuration;
		pageInstance.actionBean = actionBean;
		pageInstance.title = title;
		pageInstance.description = description;
		return pageInstance;
	}

	public Page getPage() {
		return page;
	}

	public String getUrlFragment() {
		String fragment = directory.getName();
		for (String param : parameters) {
			fragment += "/" + param;
		}
		return fragment;
	}

	public String getPath() {
		if (getParent() == null) {
			return "";
		} else {
			return getParent().getPath() + "/" + getUrlFragment();
		}
	}

	public File getDirectory() {
		return directory;
	}

	public List<String> getParameters() {
		return parameters;
	}

	public Object getConfiguration() {
		return configuration;
	}

	public void setConfiguration(Object configuration) {
		this.configuration = configuration;
	}

	public Class<? extends PageAction> getActionClass() {
		return actionClass;
	}

	public PageAction getActionBean() {
		return actionBean;
	}

	public void setActionBean(PageAction actionBean) {
		this.actionBean = actionBean;
	}

	public PageInstance getParent() {
		return parent;
	}

	public Layout getLayout() {
		if (getParameters().isEmpty()) {
			return getPage().getLayout();
		} else {
			return getPage().getDetailLayout();
		}
	}

	public void setLayout(Layout layout) {
		if (getParameters().isEmpty()) {
			getPage().setLayout(layout);
		} else {
			getPage().setDetailLayout(layout);
		}
	}

	public Page getChildPage(String name) throws Exception {
		File childDirectory = getChildPageDirectory(name);
		return DispatcherLogic.getPage(childDirectory);
	}

	public File getChildPageDirectory(String name) {
		File baseDir = getChildrenDirectory();
		return new File(baseDir, name);
	}

	public File getChildrenDirectory() {
		File baseDir = directory;
		if (!parameters.isEmpty()) {
			baseDir = new File(baseDir, DETAIL);
		}
		return baseDir;
	}

	public String getName() {
		return directory.getName();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isPrepared() {
		return prepared;
	}

	public void setPrepared(boolean prepared) {
		this.prepared = prepared;
	}
}