package com.manydesigns.elements.servlet;

import com.manydesigns.elements.xml.XhtmlBuffer;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.util.List;

public class SessionMessagesTag extends TagSupport {
	private static final long serialVersionUID = 2554938185684219220L;

	public int doStartTag() {
		JspWriter out = pageContext.getOut();
		XhtmlBuffer xb = new XhtmlBuffer(out);

		List<String> errorMessages = com.manydesigns.elements.messages.SessionMessages.consumeErrorMessages();
		List<String> warningMessages = com.manydesigns.elements.messages.SessionMessages.consumeWarningMessages();
		List<String> infoMessages = com.manydesigns.elements.messages.SessionMessages.consumeInfoMessages();

		if (!errorMessages.isEmpty()) {
			xb.openElement("div");
			xb.addAttribute("class", "alert alert-danger alert-dismissable fade in");
			writeCloseButton(xb);
			writeList(xb, errorMessages, "errorMessages");
			xb.closeElement("div");
		}

		if (!warningMessages.isEmpty()) {
			xb.openElement("div");
			xb.addAttribute("class", "alert alert-warning alert-dismissable fade in");
			writeCloseButton(xb);
			writeList(xb, warningMessages, "warningMessages");
			xb.closeElement("div");
		}

		if (!infoMessages.isEmpty()) {
			xb.openElement("div");
			xb.addAttribute("class", "alert alert-success alert-dismissable fade in");
			writeCloseButton(xb);
			writeList(xb, infoMessages, "infoMessages");
			xb.closeElement("div");
		}

		return SKIP_BODY;
	}

	private void writeCloseButton(XhtmlBuffer xb) {
		xb.writeNoHtmlEscape("<button data-dismiss=\"alert\" class=\"close\" type=\"button\">&times;</button>");
	}

	private void writeList(XhtmlBuffer xb, List<String> errorMessages, String htmlClass) {
		if (errorMessages.size() == 0) {
			return;
		}

		xb.openElement("ul");
		xb.addAttribute("class", htmlClass);
		for (String current : errorMessages) {
			xb.openElement("li");
			xb.writeNoHtmlEscape(current);
			xb.closeElement("li");
		}
		xb.closeElement("ul");
	}
}