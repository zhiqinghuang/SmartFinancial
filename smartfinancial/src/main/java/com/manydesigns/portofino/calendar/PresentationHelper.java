package com.manydesigns.portofino.calendar;

import com.manydesigns.elements.ElementsThreadLocals;
import com.manydesigns.elements.xml.XhtmlBuffer;

public class PresentationHelper {

	public static void writeDialogCloseButtonInFooter(XhtmlBuffer xhtmlBuffer) {
		xhtmlBuffer.openElement("button");
		xhtmlBuffer.addAttribute("class", "btn btn-primary");
		xhtmlBuffer.addAttribute("data-dismiss", "modal");
		xhtmlBuffer.addAttribute("aria-hidden", "true");
		xhtmlBuffer.write(ElementsThreadLocals.getText("close"));
		xhtmlBuffer.closeElement("button");
	}

	public static void writeDialogCloseButtonInHeader(XhtmlBuffer xhtmlBuffer) {
		xhtmlBuffer.openElement("button");
		xhtmlBuffer.addAttribute("type", "button");
		xhtmlBuffer.addAttribute("class", "close");
		xhtmlBuffer.addAttribute("data-dismiss", "modal");
		xhtmlBuffer.addAttribute("aria-hidden", "true");
		xhtmlBuffer.writeNoHtmlEscape("&times;");
		xhtmlBuffer.closeElement("button");
	}

	public static void closeDialog(XhtmlBuffer xhtmlBuffer) {
		xhtmlBuffer.closeElement("div");
		xhtmlBuffer.closeElement("div");
		xhtmlBuffer.closeElement("div");
	}

	public static void openDialog(XhtmlBuffer xhtmlBuffer, String dialogId, String dialogLabelId) {
		xhtmlBuffer.openElement("div");
		xhtmlBuffer.addAttribute("id", dialogId);
		xhtmlBuffer.addAttribute("class", "modal");
		xhtmlBuffer.addAttribute("tabindex", "-1");
		xhtmlBuffer.addAttribute("role", "dialog");
		xhtmlBuffer.addAttribute("aria-hidden", "true");
		if (dialogLabelId != null) {
			xhtmlBuffer.addAttribute("aria-labelledby", dialogLabelId);
		}

		xhtmlBuffer.openElement("div");
		xhtmlBuffer.addAttribute("class", "modal-dialog");
		xhtmlBuffer.openElement("div");
		xhtmlBuffer.addAttribute("class", "modal-content");
	}
}