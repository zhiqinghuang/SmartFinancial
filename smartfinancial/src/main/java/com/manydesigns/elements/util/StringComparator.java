package com.manydesigns.elements.util;

import java.util.Comparator;

public class StringComparator implements Comparator<String> {
	final boolean nullGreater;

	public StringComparator() {
		this(true);
	}

	public StringComparator(boolean nullGreater) {
		this.nullGreater = nullGreater;
	}

	public int compare(String s1, String s2) {
		if (s1 == null) {
			if (s2 == null) {
				return 0;
			} else {
				if (nullGreater) {
					return 1;
				} else {
					return -1;
				}
			}
		} else {
			if (s2 == null) {
				if (nullGreater) {
					return -1;
				} else {
					return 1;
				}
			} else {
				return s1.compareTo(s2);
			}
		}
	}
}