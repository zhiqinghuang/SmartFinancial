package com.manydesigns.elements.fields;

import com.manydesigns.elements.ElementsThreadLocals;
import com.manydesigns.elements.annotations.Label;
import com.manydesigns.elements.annotations.LabelI18N;
import com.manydesigns.elements.reflection.PropertyAccessor;
import com.manydesigns.elements.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;

public class FieldUtils {
	public static final Logger logger = LoggerFactory.getLogger(FieldUtils.class);

	public static String getLabel(PropertyAccessor accessor) {
		String label;
		if (accessor.isAnnotationPresent(LabelI18N.class)) {
			String text = accessor.getAnnotation(LabelI18N.class).value();
			logger.debug("LabelI18N annotation present with value: {}", text);

			String args = null;
			String textCompare = MessageFormat.format(text, args);
			String i18NText = ElementsThreadLocals.getTextProvider().getText(text);
			label = i18NText;
			if (textCompare.equals(i18NText) && accessor.isAnnotationPresent(Label.class)) {
				label = accessor.getAnnotation(Label.class).value();
			}
		} else if (accessor.isAnnotationPresent(Label.class)) {
			label = accessor.getAnnotation(Label.class).value();
			logger.debug("Label annotation present with value: {}", label);
		} else {
			label = Util.guessToWords(accessor.getName());
			logger.debug("Setting label from property name: {}", label);
		}
		return label;
	}
}