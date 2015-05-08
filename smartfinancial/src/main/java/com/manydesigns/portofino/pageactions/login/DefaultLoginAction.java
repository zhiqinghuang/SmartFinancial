package com.manydesigns.portofino.pageactions.login;

import com.manydesigns.mail.queue.MailQueue;
import com.manydesigns.mail.queue.QueueException;
import com.manydesigns.mail.queue.model.Email;
import com.manydesigns.mail.queue.model.Recipient;
import com.manydesigns.portofino.PortofinoProperties;
import com.manydesigns.portofino.actions.user.LoginAction;
import com.manydesigns.portofino.di.Inject;
import com.manydesigns.portofino.dispatcher.Dispatch;
import com.manydesigns.portofino.dispatcher.PageAction;
import com.manydesigns.portofino.dispatcher.PageInstance;
import com.manydesigns.portofino.modules.MailModule;
import com.manydesigns.portofino.pageactions.PageActionName;
import com.manydesigns.portofino.pageactions.annotations.ScriptTemplate;
import net.sourceforge.stripes.action.Resolution;

@ScriptTemplate("script_template.groovy")
@PageActionName("Login")
public class DefaultLoginAction extends LoginAction implements PageAction {

	public Dispatch dispatch;

	public PageInstance pageInstance;

	@Inject(MailModule.MAIL_QUEUE)
	public MailQueue mailQueue;

	@Override
	protected void sendForgotPasswordEmail(String from, String to, String subject, String body) {
		sendMail(from, to, subject, body);
	}

	@Override
	protected void sendSignupConfirmationEmail(String from, String to, String subject, String body) {
		sendMail(from, to, subject, body);
	}

	protected void sendMail(String from, String to, String subject, String body) {
		if (mailQueue == null) {
			throw new UnsupportedOperationException("Mail queue is not enabled");
		}

		Email email = new Email();
		email.getRecipients().add(new Recipient(Recipient.Type.TO, to));
		email.setFrom(from);
		email.setSubject(subject);
		email.setHtmlBody(body);
		try {
			mailQueue.enqueue(email);
		} catch (QueueException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String getApplicationName() {
		return portofinoConfiguration.getString(PortofinoProperties.APP_NAME);
	}

	@Override
	public Resolution preparePage() {
		return null;
	}

	@Override
	public PageInstance getPageInstance() {
		return pageInstance;
	}

	@Override
	public void setPageInstance(PageInstance pageInstance) {
		this.pageInstance = pageInstance;
	}
}