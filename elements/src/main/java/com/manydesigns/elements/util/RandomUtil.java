package com.manydesigns.elements.util;

import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.text.MessageFormat;

public class RandomUtil {
	public final static int RANDOM_CODE_LENGTH = 25;

	public final static Logger logger = LoggerFactory.getLogger(RandomUtil.class);

	protected static final File tempDir;

	static {
		tempDir = new File(System.getProperty("java.io.tmpdir", "/tmp"));
	}

	public static File getTempDir() {
		return tempDir;
	}

	public static String createRandomId() {
		return createRandomId(RANDOM_CODE_LENGTH);
	}

	public static String createRandomId(int length) {
		return RandomStringUtils.random(length, "abcdefghijklmnopqrstuvwxyz0123456789");
	}

	public static File getTempCodeFile(String fileNameFormat, String randomCode) {
		return getCodeFile(tempDir, fileNameFormat, randomCode);
	}

	public static File getCodeFile(File dir, String fileNameFormat, String code) {
		return new File(dir, getCodeFileName(fileNameFormat, code));
	}

	public static String getCodeFileName(String fileNameFormat, String randomCode) {
		return MessageFormat.format(fileNameFormat, randomCode);
	}
}