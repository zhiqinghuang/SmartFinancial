package com.manydesigns.portofino.pages;

import com.manydesigns.portofino.security.AccessLevel;
import org.apache.commons.lang.StringUtils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.util.HashSet;
import java.util.Set;

@XmlAccessorType(XmlAccessType.NONE)
public class Group {
	protected final Set<String> permissions;
	protected String accessLevel;
	protected String name;

	protected AccessLevel actualAccessLevel;

	public Group() {
		permissions = new HashSet<String>();
	}

	public void init() {
		actualAccessLevel = null;
		if (!StringUtils.isEmpty(accessLevel)) {
			actualAccessLevel = AccessLevel.valueOf(accessLevel);
		}
	}

	@XmlElement(name = "permission", type = String.class)
	public Set<String> getPermissions() {
		return permissions;
	}

	@XmlAttribute
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlAttribute(name = "level")
	public String getAccessLevel() {
		return accessLevel;
	}

	public void setAccessLevel(String accessLevel) {
		this.accessLevel = accessLevel;
	}

	public AccessLevel getActualAccessLevel() {
		return actualAccessLevel;
	}
}