package com.manydesigns.portofino.buttons.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface Button {

	public static final String TYPE_DEFAULT = " btn-default ", TYPE_PRIMARY = " btn-primary ", TYPE_INFO = " btn-info ", TYPE_SUCCESS = " btn-success ", TYPE_WARNING = " btn-warning ", TYPE_DANGER = " btn-danger ", TYPE_INVERSE = " btn-inverse ", TYPE_LINK = " btn-link ", TYPE_NO_UI_BLOCK = " no-ui-block ";

	public static final String ICON_WHITE = " white ", ICON_EDIT = " glyphicon-edit ", ICON_PLUS = " glyphicon-plus ", ICON_MINUS = " glyphicon-minus ", ICON_TRASH = " glyphicon-trash ", ICON_WRENCH = " glyphicon-wrench ", ICON_PICTURE = " glyphicon-picture ";

	String list();

	double order() default 1.0;

	String key() default "";

	String titleKey() default "";

	String icon() default "";

	String type() default TYPE_DEFAULT;

	String group() default "";

}