package com.manydesigns.elements.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ByteArrayServletOutputStream extends ServletOutputStream {
	public final static Logger logger = LoggerFactory.getLogger(ByteArrayServletOutputStream.class);

	final ByteArrayOutputStream byteArrayOutputStream;

	public ByteArrayServletOutputStream() {
		byteArrayOutputStream = new ByteArrayOutputStream();
	}

	@Override
	public void write(int i) throws IOException {
		logger.debug("write: {}", i);
		byteArrayOutputStream.write(i);
	}

	public ByteArrayOutputStream getByteArrayOutputStream() {
		return byteArrayOutputStream;
	}
}