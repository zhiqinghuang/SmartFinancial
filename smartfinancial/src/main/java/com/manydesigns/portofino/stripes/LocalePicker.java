package com.manydesigns.portofino.stripes;

import net.sourceforge.stripes.config.Configuration;
import net.sourceforge.stripes.localization.DefaultLocalePicker;

import java.util.Locale;

public class LocalePicker extends DefaultLocalePicker {

	@Override
	public void init(Configuration configuration) throws Exception {
		super.init(configuration);

		for (Locale locale : locales) {
			// Set UTF-8 as the default encoding
			if (!encodings.containsKey(locale)) {
				encodings.put(locale, "UTF-8");
			}
		}
	}
}