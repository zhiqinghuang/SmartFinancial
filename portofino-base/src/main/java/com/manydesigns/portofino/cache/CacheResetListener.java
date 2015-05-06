package com.manydesigns.portofino.cache;

import java.util.EventListener;

public interface CacheResetListener extends EventListener {
	void handleReset(CacheResetEvent e);
}