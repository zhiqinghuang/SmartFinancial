package com.manydesigns.elements.json;

import com.manydesigns.elements.ognl.OgnlUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.text.MessageFormat;

public class JsonBuffer {

	public static boolean checkWellFormed = false;

	public Logger logger = LoggerFactory.getLogger(JsonBuffer.class);

	protected final Writer writer;
	protected boolean first;

	public JsonBuffer() {
		this(new StringWriter());
	}

	public JsonBuffer(Writer writer) {
		this.writer = writer;
		first = true;
	}

	public void openArray() {
		try {
			writeCommaIfNeeded();
			writer.write("[");
		} catch (IOException e) {
			logger.error("Json writer exception", e);
		}
		first = true;
	}

	public void closeArray() {
		try {
			writer.write("]");
		} catch (IOException e) {
			logger.error("Json writer exception", e);
		}
		first = false;
	}

	public void openObject() {
		try {
			writeCommaIfNeeded();
			writer.write("{");
		} catch (IOException e) {
			logger.error("Json writer exception", e);
		}
		first = true;
	}

	public void closeObject() {
		try {
			writer.write("}");
		} catch (IOException e) {
			logger.error("Json writer exception", e);
		}
		first = false;
	}

	public void writeKeyValue(String key, String value) {
		String rawValue = MessageFormat.format("\"{0}\"", StringEscapeUtils.escapeJava(value));
		writeKeyRawValue(key, rawValue);
	}

	public void writeKeyValue(String key, Integer intValue) {
		String rawValue = OgnlUtils.convertValueToString(intValue);
		writeKeyRawValue(key, rawValue);
	}

	public void writeKeyValue(String key, Boolean booleanValue) {
		String rawValue;
		if (booleanValue == null) {
			rawValue = null;
		} else if (booleanValue) {
			rawValue = "true";
		} else {
			rawValue = "false";
		}
		writeKeyRawValue(key, rawValue);
	}

	protected void writeKeyRawValue(String key, String rawValue) {
		if (rawValue == null) {
			rawValue = "null";
		}
		try {
			writeCommaIfNeeded();
			String text = MessageFormat.format("\"{0}\":{1}", StringEscapeUtils.escapeJava(key), rawValue);
			writer.write(text);
		} catch (IOException e) {
			logger.error("Json writer exception", e);
		}
	}

	protected void writeCommaIfNeeded() throws IOException {
		if (first) {
			first = false;
		} else {
			writer.write(",");
		}
	}

	public String toString() {
		return writer.toString();
	}

}
