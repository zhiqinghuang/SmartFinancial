package com.manydesigns.elements.fields.search;

import com.manydesigns.elements.annotations.MaxLength;
import com.manydesigns.elements.reflection.PropertyAccessor;
import com.manydesigns.elements.xml.XhtmlBuffer;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

import javax.servlet.http.HttpServletRequest;

public class TextSearchField extends AbstractSearchField {

	public final static String MODE_SUFFIX = "_mode";
	public final static String MATCH_MODE_LABEL = "Match mode";

	protected String value;
	protected TextMatchMode matchMode = TextMatchMode.CONTAINS;
	protected String matchModeId;
	protected String matchModeParam;
	protected boolean showMatchMode = true;
	protected Integer maxLength = null;

	public TextSearchField(PropertyAccessor accessor) {
		this(accessor, null);
	}

	public TextSearchField(PropertyAccessor accessor, String prefix) {
		super(accessor, prefix);

		if (accessor.isAnnotationPresent(MaxLength.class)) {
			maxLength = accessor.getAnnotation(MaxLength.class).value();
		}

		matchModeId = id + MODE_SUFFIX;
		matchModeParam = this.inputName + MODE_SUFFIX;
	}

	public void toXhtml(@NotNull XhtmlBuffer xb) {
		xb.openElement("div");
		xb.addAttribute("class", "form-group");
		xb.writeLabel(StringUtils.capitalize(label), id, ATTR_NAME_HTML_CLASS);
		xb.openElement("div");
		xb.addAttribute("class", "text-search-form-control");
		if (showMatchMode) {
			xb.writeLabel(MATCH_MODE_LABEL, matchModeId, "match_mode");
			xb.openElement("select");
			xb.addAttribute("id", matchModeId);
			xb.addAttribute("name", matchModeParam);
			xb.addAttribute("class", FORM_CONTROL_CSS_CLASS + " match_mode");
			for (TextMatchMode m : TextMatchMode.values()) {
				boolean checked = matchMode == m;
				String option = m.getStringValue();
				xb.writeOption(option, checked, getText(m.getI18nKey()));
			}
			xb.closeElement("select");
			xb.write(" ");
		}
		xb.writeInputText(id, inputName, value, FORM_CONTROL_CSS_CLASS, 18, maxLength);
		xb.closeElement("div");
		xb.closeElement("div");
	}

	public void readFromRequest(HttpServletRequest req) {
		value = StringUtils.trimToNull(req.getParameter(inputName));
		if (showMatchMode) {
			String matchModeStr = req.getParameter(matchModeParam);
			matchMode = TextMatchMode.CONTAINS; // default
			for (TextMatchMode m : TextMatchMode.values()) {
				if (m.getStringValue().equals(matchModeStr)) {
					matchMode = m;
				}
			}
		}
	}

	public boolean validate() {
		return true;
	}

	public void toSearchString(StringBuilder sb, String encoding) {
		if (value != null) {
			appendToSearchString(sb, inputName, value, encoding);
		}
		if (matchMode != null && matchMode != TextMatchMode.CONTAINS && showMatchMode) {
			appendToSearchString(sb, matchModeParam, matchMode.getStringValue(), encoding);
		}
	}

	public void configureCriteria(Criteria criteria) {
		if (value != null) {
			criteria.ilike(accessor, value, matchMode);
		}
	}

	public TextMatchMode getMatchMode() {
		return matchMode;
	}

	public void setMatchMode(TextMatchMode matchMode) {
		this.matchMode = matchMode;
	}

	public boolean isShowMatchMode() {
		return showMatchMode;
	}

	public void setShowMatchMode(boolean showMatchMode) {
		this.showMatchMode = showMatchMode;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getMatchModeId() {
		return matchModeId;
	}

	public void setMatchModeId(String matchModeId) {
		this.matchModeId = matchModeId;
	}

	public String getMatchModeParam() {
		return matchModeParam;
	}

	public void setMatchModeParam(String matchModeParam) {
		this.matchModeParam = matchModeParam;
	}

	public Integer getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(Integer maxLength) {
		this.maxLength = maxLength;
	}
}
