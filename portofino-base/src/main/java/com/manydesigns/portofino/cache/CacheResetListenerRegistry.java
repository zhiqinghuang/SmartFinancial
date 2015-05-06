package com.manydesigns.portofino.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CacheResetListenerRegistry {

	public static final Logger logger = LoggerFactory.getLogger(CacheResetListenerRegistry.class);

	protected final List<CacheResetListener> listeners = Collections.synchronizedList(new ArrayList<CacheResetListener>());

	public List<CacheResetListener> getCacheResetListeners() {
		return listeners;
	}

	public void fireReset(CacheResetEvent e) {
		List<CacheResetListener> listeners;
		synchronized (this.listeners) {
			listeners = new ArrayList<CacheResetListener>(this.listeners);
		}
		for (CacheResetListener listener : listeners) {
			try {
				listener.handleReset(e);
			} catch (Throwable t) {
				logger.error(t.getMessage(), t);
			}
		}
	}
}