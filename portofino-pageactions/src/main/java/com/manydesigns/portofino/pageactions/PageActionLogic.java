package com.manydesigns.portofino.pageactions;

import com.manydesigns.portofino.dispatcher.PageAction;
import com.manydesigns.portofino.dispatcher.PageInstance;
import com.manydesigns.portofino.pageactions.annotations.ConfigurationClass;
import com.manydesigns.portofino.pageactions.annotations.ScriptTemplate;
import com.manydesigns.portofino.pageactions.annotations.SupportsDetail;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

public class PageActionLogic {
	public static final String copyright = "Copyright (c) 2005-2015, ManyDesigns srl";

	public static final Logger logger = LoggerFactory.getLogger(PageActionLogic.class);

	public static boolean supportsDetail(Class<?> actionClass) {
		if (!PageAction.class.isAssignableFrom(actionClass)) {
			return false;
		}
		SupportsDetail supportsDetail = actionClass.getAnnotation(SupportsDetail.class);
		if (supportsDetail != null) {
			return supportsDetail.value();
		} else {
			return supportsDetail(actionClass.getSuperclass());
		}
	}

	public static Class<?> getConfigurationClass(Class<?> actionClass) {
		if (!PageAction.class.isAssignableFrom(actionClass)) {
			return null;
		}
		ConfigurationClass configurationClass = actionClass.getAnnotation(ConfigurationClass.class);
		if (configurationClass != null) {
			return configurationClass.value();
		} else {
			return getConfigurationClass(actionClass.getSuperclass());
		}
	}

	public static String getScriptTemplate(Class<?> actionClass) {
		if (!PageAction.class.isAssignableFrom(actionClass)) {
			return null;
		}
		ScriptTemplate scriptTemplate = actionClass.getAnnotation(ScriptTemplate.class);
		if (scriptTemplate != null) {
			String templateLocation = scriptTemplate.value();
			try {
				return IOUtils.toString(actionClass.getResourceAsStream(templateLocation));
			} catch (Exception e) {
				logger.error("Can't load script template: " + templateLocation + " for class: " + actionClass.getName(), e);
			}
		} else {
			String template = getScriptTemplate(actionClass.getSuperclass());
			if (template != null) {
				return template;
			}
		}
		logger.debug("Falling back to default template for {}", actionClass);
		try {
			InputStream stream = PageActionLogic.class.getResourceAsStream("/com/manydesigns/portofino/pageactions/default_script_template.txt");
			return IOUtils.toString(stream);
		} catch (Exception e) {
			throw new Error("Can't load script template", e);
		}
	}

	public static String getDescriptionKey(Class<?> actionClass) {
		PageActionName annotation = actionClass.getAnnotation(PageActionName.class);
		if (annotation != null) {
			return annotation.value();
		} else {
			return actionClass.getName();
		}
	}

	public static boolean isEmbedded(PageAction pageAction) {
		PageInstance parent = pageAction.getPageInstance().getParent();
		if (parent == null) {
			return false; // Root page
		}
		PageAction parentActionBean = parent.getActionBean();
		if (parentActionBean == null) {
			return false;
		}
		String parentPath = parentActionBean.getContext().getActionPath();
		String myPath = pageAction.getContext().getActionPath();
		return !StringUtils.equals(parentPath, myPath);
	}
}