package com.manydesigns.portofino.actions.systemadmin;

import com.manydesigns.elements.Mode;
import com.manydesigns.elements.forms.Form;
import com.manydesigns.elements.forms.FormBuilder;
import com.manydesigns.portofino.modules.BaseModule;
import com.manydesigns.portofino.stripes.AbstractActionBean;
import com.manydesigns.portofino.servlets.ServerInfo;
import com.manydesigns.portofino.di.Inject;

public class ServerInfoAction extends AbstractActionBean {
	public String execute() {
		form = new FormBuilder(ServerInfo.class).configFields("contextPath", "realPath", "servletContextName", "serverInfo", "servletApiVersion", "usedMemory", "totalMemory", "maxMemory", "availableProcessors").configMode(Mode.VIEW).build();
		form.readFromObject(serverInfo);
		return "SUCCESS";
	}

	@Inject(BaseModule.SERVER_INFO)
	public ServerInfo serverInfo;

	public Form form;
}