package com.manydesigns.elements.i18n;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class SimpleTextProvider implements TextProvider {
	public static final String DEFAULT_MESSAGE_RESOURCE = "com.manydesigns.elements.messages";

	protected final Locale locale;
	protected final String messageResource;
	protected final ResourceBundle resourceBundle;

	public static SimpleTextProvider create() {
		return create(Locale.getDefault(), DEFAULT_MESSAGE_RESOURCE);
	}

	public static SimpleTextProvider create(Locale locale) {
		return create(locale, DEFAULT_MESSAGE_RESOURCE);
	}

	public static SimpleTextProvider create(String messageResource) {
		return create(Locale.getDefault(), messageResource);
	}

	public static SimpleTextProvider create(Locale locale, String messageResource) {
		return new SimpleTextProvider(locale, messageResource);
	}

	private SimpleTextProvider(Locale locale, String messageResource) {
		this.locale = locale;
		this.messageResource = messageResource;
		ResourceBundle tmpBundle;
		try {
			if (locale == null)
				tmpBundle = ResourceBundle.getBundle(messageResource);
			else {
				tmpBundle = ResourceBundle.getBundle(messageResource, locale);
			}
		} catch (Throwable e) {
			tmpBundle = null;
		}
		resourceBundle = tmpBundle;
	}

	public String getText(String key, Object... args) {
		String localizedString = getLocalizedString(key);
		return MessageFormat.format(localizedString, args);
	}

	public String getLocalizedString(String key) {
		try {
			if (resourceBundle == null) {
				return key;
			} else {
				return resourceBundle.getString(key);
			}
		} catch (Throwable e) {
			return key;
		}
	}
}