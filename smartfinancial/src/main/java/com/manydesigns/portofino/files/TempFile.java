package com.manydesigns.portofino.files;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class TempFile {
	public final String name;
	public final String mimeType;

	public TempFile(String mimeType, String name) {
		this.name = name;
		this.mimeType = mimeType;
	}

	public abstract OutputStream getOutputStream() throws IOException;

	public abstract InputStream getInputStream() throws IOException;

	public abstract void dispose();
}