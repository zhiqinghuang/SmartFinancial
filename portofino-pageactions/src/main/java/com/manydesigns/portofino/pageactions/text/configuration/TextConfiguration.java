package com.manydesigns.portofino.pageactions.text.configuration;

import com.manydesigns.portofino.dispatcher.PageActionConfiguration;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "configuration")
@XmlAccessorType(XmlAccessType.NONE)
public class TextConfiguration implements PageActionConfiguration {

	protected final List<Attachment> attachments;

	public TextConfiguration() {
		super();
		attachments = new ArrayList<Attachment>();
	}

	public void init() {
	}

	@XmlElementWrapper(name = "attachments")
	@XmlElement(name = "attachment", type = Attachment.class)
	public List<Attachment> getAttachments() {
		return attachments;
	}
}