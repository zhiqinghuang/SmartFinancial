package com.manydesigns.portofino.navigation;

import com.manydesigns.elements.xml.XhtmlBuffer;
import com.manydesigns.portofino.stripes.AbstractActionBean;
import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.controller.StripesConstants;

import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;
import java.net.URL;

public class BaseHref {

	public static void emit(HttpServletRequest request, XhtmlBuffer xb) {
		// Setup base href - uniform handling of .../resource and .../resource/
		ActionBean actionBean = (ActionBean) request.getAttribute(StripesConstants.REQ_ATTR_ACTION_BEAN);
		if (actionBean instanceof AbstractActionBean) {
			String baseHref = request.getContextPath() + ((AbstractActionBean) actionBean).getContext().getActionPath();
			// Remove all trailing slashes
			while (baseHref.length() > 1 && baseHref.endsWith("/")) {
				baseHref = baseHref.substring(0, baseHref.length() - 1);
			}
			// Add a single trailing slash so all relative URLs use this page as
			// the root
			baseHref += "/";
			// Try to make the base HREF absolute
			try {
				URL url = new URL(request.getRequestURL().toString());
				String port = url.getPort() > 0 ? ":" + url.getPort() : "";
				baseHref = url.getProtocol() + "://" + url.getHost() + port + baseHref;
			} catch (MalformedURLException e) {
				// Ignore
			}

			xb.openElement("base");
			xb.addAttribute("href", baseHref);
			xb.closeElement("base");
		}
	}
}