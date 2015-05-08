package com.manydesigns.portofino.cache;

import java.util.EventObject;

public class CacheResetEvent extends EventObject {
	private static final long serialVersionUID = 5441352643821453019L;

	public CacheResetEvent(Object source) {
		super(source);
	}
}