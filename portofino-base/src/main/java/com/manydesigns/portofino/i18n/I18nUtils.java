package com.manydesigns.portofino.i18n;

import com.manydesigns.elements.ElementsThreadLocals;
import com.manydesigns.elements.i18n.SimpleTextProvider;
import com.manydesigns.elements.i18n.TextProvider;
import com.manydesigns.portofino.modules.BaseModule;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.jsp.jstl.core.Config;
import javax.servlet.jsp.jstl.fmt.LocalizationContext;
import java.util.Locale;
import java.util.ResourceBundle;

public class I18nUtils {
	public static void setupTextProvider(ServletContext servletContext, ServletRequest request) {
		Locale locale = request.getLocale();
		ResourceBundleManager resourceBundleManager = (ResourceBundleManager) servletContext.getAttribute(BaseModule.RESOURCE_BUNDLE_MANAGER);
		ResourceBundle portofinoResourceBundle = resourceBundleManager.getBundle(locale);

		LocalizationContext localizationContext = new LocalizationContext(portofinoResourceBundle, locale);
		request.setAttribute(Config.FMT_LOCALIZATION_CONTEXT + ".request", localizationContext);

		// Setup Elements I18n
		ResourceBundle elementsResourceBundle = ResourceBundle.getBundle(SimpleTextProvider.DEFAULT_MESSAGE_RESOURCE, locale);

		TextProvider textProvider = new MultipleTextProvider(portofinoResourceBundle, elementsResourceBundle);
		ElementsThreadLocals.setTextProvider(textProvider);
	}
}