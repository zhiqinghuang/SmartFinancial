package com.manydesigns.elements.util;

import java.text.MessageFormat;

public class MemoryUtil {
	public final static double LOG2 = Math.log(2);

	public final static String[] suffixes = { " bytes", "K", "M", "G", "T", "P" };

	public static String bytesToHumanString(long bytes) {
		if (bytes < 0) {
			throw new IllegalArgumentException("Negative argument: " + bytes);
		}
		if (bytes == 1) {
			return "1 byte";
		} else if (bytes == 0) {
			return MessageFormat.format("0{1}", bytes, suffixes[0]);
		}
		int log = (int) Math.log10(bytes);
		int pos = log / 3;
		double scaled = bytes / Math.pow(10, pos * 3);
		long rounded = Math.round(scaled);
		double rounded2 = (double) Math.round(scaled * 10) / 10d;

		if (pos == 0) {
			return MessageFormat.format("{0,number,0}{1}", rounded, suffixes[pos]);
		} else if (rounded >= 1000) {
			scaled = scaled / 1000;
			pos = pos + 1;
			rounded2 = (double) Math.round(scaled * 10) / 10d;
			return MessageFormat.format("{0,number,0.0}{1}", rounded2, suffixes[pos]);
		} else if (rounded2 < 10) {
			return MessageFormat.format("{0,number,0.0}{1}", rounded2, suffixes[pos]);
		} else {
			return MessageFormat.format("{0,number,0}{1}", rounded, suffixes[pos]);
		}
	}
}