package com.manydesigns.portofino.pageactions.form;

import com.manydesigns.elements.ElementsThreadLocals;
import com.manydesigns.elements.Mode;
import com.manydesigns.elements.forms.Form;
import com.manydesigns.elements.forms.FormBuilder;
import com.manydesigns.elements.messages.SessionMessages;
import com.manydesigns.elements.reflection.ClassAccessor;
import com.manydesigns.portofino.buttons.annotations.Button;
import com.manydesigns.portofino.pageactions.AbstractPageAction;
import com.manydesigns.portofino.pageactions.PageActionName;
import com.manydesigns.portofino.pageactions.annotations.ScriptTemplate;
import com.manydesigns.portofino.security.AccessLevel;
import com.manydesigns.portofino.security.RequiresPermissions;
import com.manydesigns.portofino.security.SupportsPermissions;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ErrorResolution;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequiresPermissions(level = AccessLevel.VIEW)
@PageActionName("Form")
@ScriptTemplate("form_template.groovy")
@SupportsPermissions(FormAction.POST_FORM_PERMISSION)
public abstract class FormAction extends AbstractPageAction {

	public static final Logger logger = LoggerFactory.getLogger(FormAction.class);
	public static final String POST_FORM_PERMISSION = "post-form";

	protected Form form;

	@Button(list = "pageHeaderButtons", titleKey = "configure", order = 1, icon = Button.ICON_WRENCH)
	@RequiresPermissions(level = AccessLevel.DEVELOP)
	public Resolution configure() {
		prepareConfigurationForms();
		return new ForwardResolution("/m/pageactions/pageactions/form/configure.jsp");
	}

	@Button(list = "configuration", key = "update.configuration", order = 1, type = Button.TYPE_PRIMARY)
	@RequiresPermissions(level = AccessLevel.DEVELOP)
	public Resolution updateConfiguration() {
		prepareConfigurationForms();
		readPageConfigurationFromRequest();
		boolean valid = validatePageConfiguration();
		if (valid) {
			updatePageConfiguration();
			SessionMessages.addInfoMessage(ElementsThreadLocals.getText("configuration.updated.successfully"));
		}
		return cancel();
	}

	public Resolution preparePage() {
		if (!pageInstance.getParameters().isEmpty()) {
			return new ErrorResolution(404);
		}
		return null;
	}

	protected void setupForm(Mode mode) {
		FormBuilder formBuilder = createFormBuilder();
		configureFormBuilder(formBuilder, mode);
		form = buildForm(formBuilder);
	}

	protected FormBuilder createFormBuilder() {
		return new FormBuilder(getClassAccessor());
	}

	protected FormBuilder configureFormBuilder(FormBuilder formBuilder, Mode mode) {
		return formBuilder.configMode(mode);
	}

	protected Form buildForm(FormBuilder formBuilder) {
		return formBuilder.build();
	}

	protected abstract ClassAccessor getClassAccessor();

	protected abstract Object getObject();

	protected Mode getMode() {
		return Mode.EDIT;
	}

	@DefaultHandler
	public Resolution execute() {
		setupForm(getMode());
		Object object = getObject();
		form.readFromObject(object);
		return getShowFormResolution();
	}

	protected Resolution doWithForm(ActionOnForm closure) {
		return doWithForm(getMode(), closure);
	}

	protected Resolution doWithForm(Mode mode, ActionOnForm closure) {
		setupForm(mode);
		Object object = getObject();
		form.readFromObject(object);
		form.readFromRequest(context.getRequest());
		if (form.validate()) {
			form.writeToObject(object);
			Object result = closure.invoke(form, object);
			if (result instanceof Resolution) {
				return (Resolution) result;
			}
		} else {
			validationFailed(form, object);
		}
		return getShowFormResolution();
	}

	protected Resolution getShowFormResolution() {
		return forwardTo("/m/pageactions/pageactions/form/form.jsp");
	}

	protected void validationFailed(Form form, Object object) {
		logger.debug("Form validation failed");
	}

	public Form getForm() {
		return form;
	}

	public static interface ActionOnForm {
		Object invoke(Form form, Object object);
	}
}