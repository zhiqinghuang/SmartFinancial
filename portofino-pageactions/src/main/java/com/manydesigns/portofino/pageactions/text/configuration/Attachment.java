package com.manydesigns.portofino.pageactions.text.configuration;

import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.NONE)
public class Attachment {
	protected String id;
	protected String contentType;
	protected String filename;
	protected boolean downloadable = true;
	protected long size;

	public static final Logger logger = LoggerFactory.getLogger(Attachment.class);

	public Attachment() {
		this(null);
	}

	public Attachment(@Nullable String id) {
		this.id = id;
	}

	@XmlAttribute(required = true)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@XmlAttribute(required = true)
	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	@XmlAttribute(required = true)
	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	@XmlAttribute(required = true)
	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	@XmlAttribute
	public boolean isDownloadable() {
		return downloadable;
	}

	public void setDownloadable(boolean downloadable) {
		this.downloadable = downloadable;
	}
}