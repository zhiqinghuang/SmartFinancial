package com.manydesigns.elements;

import com.manydesigns.elements.xml.XhtmlFragment;

import javax.servlet.http.HttpServletRequest;

public interface Element extends XhtmlFragment {
	public void readFromRequest(HttpServletRequest req);

	public boolean validate();

	public void readFromObject(Object obj);

	public void writeToObject(Object obj);
}