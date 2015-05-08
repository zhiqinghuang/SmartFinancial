package com.manydesigns.portofino.pageactions.crud;

public class ResultSetNavigation {
	public int position;
	public int size;
	public String firstUrl;
	public String previousUrl;
	public String nextUrl;
	public String lastUrl;

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String getFirstUrl() {
		return firstUrl;
	}

	public void setFirstUrl(String firstUrl) {
		this.firstUrl = firstUrl;
	}

	public String getPreviousUrl() {
		return previousUrl;
	}

	public void setPreviousUrl(String previousUrl) {
		this.previousUrl = previousUrl;
	}

	public String getNextUrl() {
		return nextUrl;
	}

	public void setNextUrl(String nextUrl) {
		this.nextUrl = nextUrl;
	}

	public String getLastUrl() {
		return lastUrl;
	}

	public void setLastUrl(String lastUrl) {
		this.lastUrl = lastUrl;
	}
}