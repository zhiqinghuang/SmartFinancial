package com.manydesigns.portofino.pageactions;

import com.manydesigns.portofino.pages.Page;

public class EmbeddedPageAction implements Comparable<EmbeddedPageAction> {

	protected final String path;
	protected final String id;
	protected final Integer index;
	protected final Page page;

	public EmbeddedPageAction(String id, Integer index, String path, Page page) {
		this.id = id;
		this.index = index;
		this.path = path;
		this.page = page;
	}

	public String getPath() {
		return path;
	}

	public String getId() {
		return id;
	}

	public int getIndex() {
		return index;
	}

	public Page getPage() {
		return page;
	}

	public int compareTo(EmbeddedPageAction that) {
		if (this.index == null) {
			if (that.index == null) {
				return this.path.compareTo(that.path);
			} else {
				return -1;
			}
		} else {
			if (that.index == null) {
				return 1;
			} else {
				return this.index.compareTo(that.index);
			}
		}
	}
}