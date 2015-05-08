package com.manydesigns.portofino.actions.admin.servletcontext;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletContext;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.manydesigns.elements.Mode;
import com.manydesigns.elements.forms.TableForm;
import com.manydesigns.elements.forms.TableFormBuilder;
import com.manydesigns.elements.ognl.OgnlUtils;
import com.manydesigns.portofino.buttons.annotations.Button;
import com.manydesigns.portofino.security.RequiresAdministrator;
import com.manydesigns.portofino.stripes.AbstractActionBean;

@RequiresAuthentication
@RequiresAdministrator
@UrlBinding(ServletContextAction.URL_BINDING)
public class ServletContextAction extends AbstractActionBean {

	public static final String URL_BINDING = "/actions/admin/servlet-context";

	TableForm form;

	public final static Logger logger = LoggerFactory.getLogger(ServletContextAction.class);

	@DefaultHandler
	@RequiresPermissions("com.manydesigns.portofino.servletcontext:list")
	public Resolution execute() {
		setupForm();
		return new ForwardResolution("/m/admin/servletcontext/list.jsp");
	}

	protected void setupForm() {
		ServletContext servletContext = context.getServletContext();
		Enumeration<String> attributeNames = servletContext.getAttributeNames();
		List<KeyValue> attributes = new ArrayList<KeyValue>();
		while (attributeNames.hasMoreElements()) {
			String key = attributeNames.nextElement();
			String value = StringUtils.abbreviate(OgnlUtils.convertValueToString(servletContext.getAttribute(key)), 300);
			attributes.add(new KeyValue(key, value));
		}
		TableFormBuilder builder = new TableFormBuilder(KeyValue.class);
		builder.configNRows(attributes.size());
		builder.configMode(Mode.VIEW);
		form = builder.build();
		form.readFromObject(attributes);
	}

	@Button(list = "modules", key = "return.to.pages", order = 2)
	public Resolution returnToPages() {
		return new RedirectResolution("/");
	}

	public TableForm getForm() {
		return form;
	}

	public static class KeyValue {

		public KeyValue(String key, String value) {
			this.key = key;
			this.value = value;
		}

		public String key;
		public String value;
	}
}