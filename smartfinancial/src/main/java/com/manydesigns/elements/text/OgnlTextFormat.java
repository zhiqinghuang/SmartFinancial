package com.manydesigns.elements.text;

import com.manydesigns.elements.ElementsThreadLocals;
import com.manydesigns.elements.ognl.OgnlUtils;
import com.manydesigns.elements.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.Locale;

public class OgnlTextFormat extends AbstractOgnlFormat implements TextFormat {

	protected boolean url = false;
	protected String encoding = "ISO-8859-1";
	protected Locale locale;

	public static final Logger logger = LoggerFactory.getLogger(OgnlTextFormat.class);

	public static OgnlTextFormat create(String ognlFormat) {
		return new OgnlTextFormat(ognlFormat);
	}

	public static String format(String expression, Object root) {
		return create(expression).format(root);
	}

	public OgnlTextFormat(String ognlFormat) {
		super(ognlFormat);
	}

	public String format(Object root) {
		Object[] args = evaluateOgnlExpressions(root);
		String[] argStrings = new String[args.length];
		for (int i = 0; i < args.length; i++) {
			Object arg = args[i];
			String argString = (String) OgnlUtils.convertValue(arg, String.class);
			if (argString == null) {
				argStrings[i] = null;
			} else {
				try {
					argStrings[i] = url ? URLEncoder.encode(argString, encoding) : argString;
				} catch (UnsupportedEncodingException e) {
					throw new Error(e);
				}
			}
		}

		Locale locale = this.locale;
		if (locale == null) {
			HttpServletRequest req = ElementsThreadLocals.getHttpServletRequest();
			if (req != null) {
				locale = req.getLocale();
			} else { // Not in a HTTP request.
				locale = Locale.getDefault();
			}
		}
		String result = new MessageFormat(getFormatString(), locale).format(argStrings);

		if (url) {
			result = Util.getAbsoluteUrl(result);
		}

		return result;
	}

	@Override
	protected String escapeText(String text) {
		return text.replace("'", "''").replace("{", "'{'");
	}

	protected void replaceOgnlExpression(StringBuilder sb, int index, String ognlExpression) {
		sb.append("{");
		sb.append(index);
		sb.append("}");
	}

	public boolean isUrl() {
		return url;
	}

	public void setUrl(boolean url) {
		this.url = url;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}
}