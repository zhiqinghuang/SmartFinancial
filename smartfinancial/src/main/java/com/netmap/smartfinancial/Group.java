package com.netmap.smartfinancial;

public class Group {
	private String groupName = null;
	private String url = null;
	private int groupCount;

	public Group(String groupName, String url, int groupCount) {
		this.groupName = groupName;
		this.url = url;
		this.groupCount = groupCount;
	}

	public String getGroupName() {
		return groupName;
	}

	public String getUrl() {
		return url;
	}

	public int getGroupCount() {
		return groupCount;
	}
}