package com.manydesigns.elements.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Locale;

public class MutableHttpServletResponse implements HttpServletResponse {
	public final static Logger logger = LoggerFactory.getLogger(MutableHttpServletResponse.class);

	final ServletOutputStream outputStream;
	PrintWriter writer;
	String characterEncoding;
	int contentLength;
	int status;
	String statusMessage;
	String contentType;
	Locale locale;

	public MutableHttpServletResponse(ServletOutputStream outputStream) {
		this.outputStream = outputStream;
	}

	@Override
	public void addCookie(Cookie cookie) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean containsHeader(String s) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String encodeURL(String s) {
		return s;
	}

	@Override
	public String encodeRedirectURL(String s) {
		return s;
	}

	@Override
	public String encodeUrl(String s) {
		return encodeURL(s);
	}

	@Override
	public String encodeRedirectUrl(String s) {
		return encodeRedirectURL(s);
	}

	@Override
	public void sendError(int i, String s) throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void sendError(int i) throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void sendRedirect(String s) throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setDateHeader(String s, long l) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void addDateHeader(String s, long l) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setHeader(String s, String s1) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void addHeader(String s, String s1) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setIntHeader(String s, int i) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void addIntHeader(String s, int i) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setStatus(int i) {
		logger.debug("Setting status to: {}", i);
		status = i;
	}

	@Override
	public void setStatus(int i, String s) {
		logger.debug("Setting status and message to: {} - {}", i, s);
		status = i;
		statusMessage = s;
	}

	@Override
	public int getStatus() {
		return status;
	}

	@Override
	public String getHeader(String s) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Collection<String> getHeaders(String s) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Collection<String> getHeaderNames() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getCharacterEncoding() {
		return characterEncoding;
	}

	@Override
	public String getContentType() {
		return contentType;
	}

	@Override
	public ServletOutputStream getOutputStream() throws IOException {
		return outputStream;
	}

	@Override
	public PrintWriter getWriter() throws IOException {
		if (writer == null) {
			OutputStreamWriter osw;
			if (characterEncoding == null) {
				logger.debug("Creating writer with default encoding");
				osw = new OutputStreamWriter(outputStream);
			} else {
				logger.debug("Creating writer with encoding: {}", characterEncoding);
				osw = new OutputStreamWriter(outputStream, characterEncoding);
			}
			writer = new PrintWriter(osw);
		}
		return writer;
	}

	@Override
	public void setCharacterEncoding(String s) {
		logger.debug("Setting encoding to: {}", s);
		characterEncoding = s;
	}

	@Override
	public void setContentLength(int i) {
		logger.debug("Setting content length to: {}", i);
		contentLength = i;
	}

	@Override
	public void setContentType(String s) {
		logger.debug("Setting content type to: {}", s);
		contentType = s;
	}

	@Override
	public void setBufferSize(int i) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getBufferSize() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void flushBuffer() throws IOException {
		if (writer == null) {
			logger.debug("Flushing output stream");
			outputStream.flush();
		} else {
			logger.debug("Flushing writer");
			writer.flush();
		}
	}

	@Override
	public void resetBuffer() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isCommitted() {
		return true;
	}

	@Override
	public void reset() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setLocale(Locale locale) {
		logger.debug("Setting locale to: {}", locale);
		this.locale = locale;
	}

	@Override
	public Locale getLocale() {
		return locale;
	}
}