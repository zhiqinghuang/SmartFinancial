package com.manydesigns.portofino.security;

public enum AccessLevel {

	NONE(0), VIEW(1), EDIT(2), DEVELOP(3), DENY(Integer.MAX_VALUE);

	private AccessLevel(int level) {
		this.level = level;
	}

	public boolean isGreaterThanOrEqual(AccessLevel accessLevel) {
		return level >= accessLevel.level;
	}

	private final int level;

}