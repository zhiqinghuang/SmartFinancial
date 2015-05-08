package com.manydesigns.elements.xml;

import java.io.IOException;

public class IOError extends Error {
	private static final long serialVersionUID = -5444774139601809697L;

	public IOError(IOException cause) {
		super(cause);
	}
}