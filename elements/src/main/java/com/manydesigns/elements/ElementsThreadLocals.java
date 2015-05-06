package com.manydesigns.elements;

import com.manydesigns.elements.i18n.SimpleTextProvider;
import com.manydesigns.elements.i18n.TextProvider;
import com.manydesigns.elements.ognl.CustomTypeConverter;
import ognl.Ognl;
import ognl.OgnlContext;
import ognl.TypeConverter;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public final class ElementsThreadLocals {
	private static ThreadLocal<ElementsContext> threadLocalElementsContext = new ThreadLocal<ElementsContext>() {
		@Override
		protected ElementsContext initialValue() {
			return new ElementsContext();
		}
	};

	public static void destroy() {
		threadLocalElementsContext = null;
	}

	private ElementsThreadLocals() {
	}

	public static ElementsContext getElementsContext() {
		return threadLocalElementsContext.get();
	}

	public static TextProvider getTextProvider() {
		return getElementsContext().getTextProvider();
	}

	public static void setTextProvider(TextProvider textProvider) {
		getElementsContext().setTextProvider(textProvider);
	}

	public static HttpServletRequest getHttpServletRequest() {
		return getElementsContext().getHttpServletRequest();
	}

	public static void setHttpServletRequest(HttpServletRequest httpServletRequest) {
		getElementsContext().setHttpServletRequest(httpServletRequest);
	}

	public static HttpServletResponse getHttpServletResponse() {
		return getElementsContext().getHttpServletResponse();
	}

	public static void setHttpServletResponse(HttpServletResponse httpServletResponse) {
		getElementsContext().setHttpServletResponse(httpServletResponse);
	}

	public static ServletContext getServletContext() {
		return getElementsContext().getServletContext();
	}

	public static void setServletContext(ServletContext servletContext) {
		getElementsContext().setServletContext(servletContext);
	}

	public static OgnlContext getOgnlContext() {
		return getElementsContext().getOgnlContext();
	}

	public static void setOgnlContext(OgnlContext context) {
		getElementsContext().setOgnlContext(context);
	}

	public static void setupDefaultElementsContext() {
		OgnlContext ognlContext = (OgnlContext) Ognl.createDefaultContext(null);
		TypeConverter conv = ognlContext.getTypeConverter();
		ognlContext.setTypeConverter(new CustomTypeConverter(conv));
		TextProvider textProvider = SimpleTextProvider.create();

		ElementsContext elementsContext = getElementsContext();

		elementsContext.setOgnlContext(ognlContext);
		elementsContext.setTextProvider(textProvider);
		elementsContext.setHttpServletRequest(null);
		elementsContext.setHttpServletResponse(null);
		elementsContext.setServletContext(null);
	}

	public static void removeElementsContext() {
		threadLocalElementsContext.remove();
	}

	public static String getText(String key, Object... args) {
		return getTextProvider().getText(key, args);
	}
}