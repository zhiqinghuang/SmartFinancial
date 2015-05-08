package com.manydesigns.mail.queue.model;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement
public class Email {
	String subject;
	String textBody;
	String htmlBody;
	final List<Recipient> recipients = new ArrayList<Recipient>();
	final List<Attachment> attachments = new ArrayList<Attachment>();
	String from;

	@XmlElement
	public List<Recipient> getRecipients() {
		return recipients;
	}

	@XmlElement
	public List<Attachment> getAttachments() {
		return attachments;
	}

	@XmlAttribute
	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	@XmlElement
	public String getTextBody() {
		return textBody;
	}

	public void setTextBody(String textBody) {
		this.textBody = textBody;
	}

	@XmlElement
	public String getHtmlBody() {
		return htmlBody;
	}

	public void setHtmlBody(String htmlBody) {
		this.htmlBody = htmlBody;
	}

	@XmlAttribute
	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}
}