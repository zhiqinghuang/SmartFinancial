package com.manydesigns.portofino.pageactions.registry;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class TemplateRegistry implements Iterable<String> {
	protected final List<String> registry = new CopyOnWriteArrayList<String>();

	public String register(String name) {
		registry.add(name);
		return name;
	}

	public Iterator<String> iterator() {
		return registry.iterator();
	}
}