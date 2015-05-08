package com.manydesigns.elements.configuration;

import org.apache.commons.configuration.Configuration;

import java.math.BigDecimal;
import java.math.BigInteger;

public class CommonsConfigurationFunctions {

	public static String getString(Configuration configuration, String key) {
		return configuration.getString(key);
	}

	public static int getInt(Configuration configuration, String key) {
		return configuration.getInt(key);
	}

	public static String[] getStringArray(Configuration configuration, String key) {
		return configuration.getStringArray(key);
	}

	public static BigDecimal getBigDecimal(Configuration configuration, String key) {
		return configuration.getBigDecimal(key);
	}

	public static BigInteger getBigInteger(Configuration configuration, String key) {
		return configuration.getBigInteger(key);
	}

	public static boolean getBoolean(Configuration configuration, String key) {
		return configuration.getBoolean(key);
	}

	public static byte getByte(Configuration configuration, String key) {
		return configuration.getByte(key);
	}

	public static double getDouble(Configuration configuration, String key) {
		return configuration.getDouble(key);
	}

	public static boolean containsKey(Configuration configuration, String key) {
		return configuration.containsKey(key);
	}

	public static float getFloat(Configuration configuration, String key) {
		return configuration.getFloat(key);
	}

	public static long getLong(Configuration configuration, String key) {
		return configuration.getLong(key);
	}

	public static short getShort(Configuration configuration, String key) {
		return configuration.getShort(key);
	}
}