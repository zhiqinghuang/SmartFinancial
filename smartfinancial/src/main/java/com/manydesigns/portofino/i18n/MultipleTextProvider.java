package com.manydesigns.portofino.i18n;

import com.manydesigns.elements.i18n.TextProvider;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class MultipleTextProvider implements TextProvider {

	protected final List<ResourceBundle> resourceBundles;

	public MultipleTextProvider(ResourceBundle... resourceBundles) {
		this.resourceBundles = new ArrayList<ResourceBundle>(Arrays.asList(resourceBundles));
	}

	public String getText(String key, Object... args) {
		String localizedString = getLocalizedString(key);
		return MessageFormat.format(localizedString, args);
	}

	public List<ResourceBundle> getResourceBundles() {
		return resourceBundles;
	}

	public String getLocalizedString(String key) {
		for (ResourceBundle current : resourceBundles) {
			try {
				return current.getString(key);
			} catch (Throwable t) {
				/* IGNORE */
			}
		}
		return key;
	}
}