package com.manydesigns.portofino.pages;

import com.manydesigns.portofino.security.AccessLevel;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.*;

@XmlAccessorType(XmlAccessType.NONE)
public class Permissions {
	protected final List<Group> groups;

	protected final Map<String, AccessLevel> actualLevels;
	// <group, set<permission>>
	protected final Map<String, Set<String>> actualPermissions;

	public Permissions() {
		groups = new ArrayList<Group>();

		actualLevels = new HashMap<String, AccessLevel>();
		actualPermissions = new HashMap<String, Set<String>>();
	}

	public void init() {
		for (Group group : groups) {
			group.init();
			actualLevels.put(group.getName(), group.getActualAccessLevel());
			actualPermissions.put(group.getName(), group.getPermissions());
		}
	}

	@XmlElement(name = "group", type = Group.class)
	public List<Group> getGroups() {
		return groups;
	}

	public Map<String, Set<String>> getActualPermissions() {
		return actualPermissions;
	}

	public Map<String, AccessLevel> getActualLevels() {
		return actualLevels;
	}
}