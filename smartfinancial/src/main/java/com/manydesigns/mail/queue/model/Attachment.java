package com.manydesigns.mail.queue.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import java.io.InputStream;

@XmlAccessorType(XmlAccessType.NONE)
public class Attachment {
	protected String name;
	protected String description;
	protected String disposition;

	protected String contentId;
	protected boolean embedded;

	protected InputStream inputStream;
	protected String filePath;

	public Attachment() {
	}

	@XmlAttribute(required = true)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlAttribute
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@XmlAttribute
	public String getDisposition() {
		return disposition;
	}

	public void setDisposition(String disposition) {
		this.disposition = disposition;
	}

	@XmlAttribute
	public String getContentId() {
		return contentId;
	}

	public void setContentId(String contentId) {
		this.contentId = contentId;
	}

	@XmlAttribute
	public boolean isEmbedded() {
		return embedded;
	}

	public void setEmbedded(boolean embedded) {
		this.embedded = embedded;
	}

	@XmlTransient
	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	@XmlAttribute(required = true)
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
}