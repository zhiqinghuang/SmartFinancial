package com.manydesigns.elements.messages;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.manydesigns.elements.ElementsThreadLocals;
import com.manydesigns.elements.xml.XhtmlBuffer;
import com.manydesigns.elements.xml.XhtmlFragment;

public class SessionMessages {
	public static final String INFO_MESSAGES_KEY = "info_messages_key";
	public static final String WARNING_MESSAGES_KEY = "warning_messages_key";
	public static final String ERROR_MESSAGES_KEY = "error_messages_key";

	public final static Logger logger = LoggerFactory.getLogger(SessionMessages.class);

	public static void addInfoMessage(String msg) {
		getInfoQueue().add(StringEscapeUtils.escapeXml(msg));
	}

	public static void addInfoMessage(XhtmlFragment xml) {
		XhtmlBuffer buf = new XhtmlBuffer();
		xml.toXhtml(buf);
		getInfoQueue().add(buf.toString());
	}

	public static void addWarningMessage(String msg) {
		getWarningQueue().add(StringEscapeUtils.escapeXml(msg));
	}

	public static void addWarningMessage(XhtmlFragment xml) {
		XhtmlBuffer buf = new XhtmlBuffer();
		xml.toXhtml(buf);
		getWarningQueue().add(buf.toString());
	}

	public static void addErrorMessage(String msg) {
		getErrorQueue().add(StringEscapeUtils.escapeXml(msg));
	}

	public static void addErrorMessage(XhtmlFragment xml) {
		XhtmlBuffer buf = new XhtmlBuffer();
		xml.toXhtml(buf);
		getErrorQueue().add(buf.toString());
	}

	public static List<String> consumeInfoMessages() {
		List<String> result = new ArrayList<String>();
		getInfoQueue().drainTo(result);
		return result;
	}

	public static List<String> consumeWarningMessages() {
		List<String> result = new ArrayList<String>();
		getWarningQueue().drainTo(result);
		return result;
	}

	public static List<String> consumeErrorMessages() {
		List<String> result = new ArrayList<String>();
		getErrorQueue().drainTo(result);
		return result;
	}

	public static BlockingQueue<String> getInfoQueue() {
		return getQueue(INFO_MESSAGES_KEY);
	}

	public static BlockingQueue<String> getWarningQueue() {
		return getQueue(WARNING_MESSAGES_KEY);
	}

	public static BlockingQueue<String> getErrorQueue() {
		return getQueue(ERROR_MESSAGES_KEY);
	}

	protected static BlockingQueue<String> getQueue(String queueName) {
		HttpServletRequest req = ElementsThreadLocals.getHttpServletRequest();
		if (req == null) {
			logger.debug("No request available. Returning dummy queue.");
			return new LinkedBlockingQueue<String>();
		}
		HttpSession session = req.getSession();
		BlockingQueue<String> infoQueue;
		synchronized (session) {
			infoQueue = (BlockingQueue) session.getAttribute(queueName);
			if (infoQueue == null) {
				// install a new queue
				infoQueue = new LinkedBlockingQueue<String>();
				session.setAttribute(queueName, infoQueue);
			}
		}
		return infoQueue;
	}
}