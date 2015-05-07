package com.manydesigns.mail.queue.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.NONE)
public class Recipient {
	public static enum Type {
		TO, CC, BCC
	}

	protected Type type;
	protected String address;

	public Recipient() {
	}

	public Recipient(Type type, String address) {
		this.type = type;
		this.address = address;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	@XmlAttribute(required = true)
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@XmlAttribute(name = "type", required = true)
	public String getTypeString() {
		return type.name();
	}

	public void setTypeString(String type) {
		this.type = Type.valueOf(type);
	}
}