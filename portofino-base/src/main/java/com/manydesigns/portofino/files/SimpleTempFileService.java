package com.manydesigns.portofino.files;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class SimpleTempFileService extends TempFileService {

	public static final Logger logger = LoggerFactory.getLogger(SimpleTempFileService.class);

	public static class SimpleTempFile extends TempFile {

		public final File file;

		public SimpleTempFile(String mimeType, String name) throws IOException {
			super(mimeType, name);
			file = File.createTempFile("temp." + name, ".temp");
		}

		@Override
		public OutputStream getOutputStream() throws IOException {
			return new FileOutputStream(file);
		}

		@Override
		public InputStream getInputStream() throws IOException {
			return new FileInputStream(file);
		}

		@Override
		public void dispose() {
			try {
				file.delete();
			} catch (Exception e) {
				logger.warn("Could not delete temp file: " + file.getAbsolutePath(), e);
			}
		}
	}

	@Override
	public TempFile newTempFile(String mimeType, String name) throws IOException {
		return new SimpleTempFile(mimeType, name);
	}
}