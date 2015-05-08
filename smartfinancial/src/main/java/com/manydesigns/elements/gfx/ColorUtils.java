package com.manydesigns.elements.gfx;

import java.awt.Color;

public class ColorUtils {

	public static Color add(Color color1, Color color2) {
		float[] components1 = color1.getRGBComponents(null);
		float[] components2 = color2.getRGBComponents(null);
		float[] components = new float[4];
		for (int i = 0; i < 4; i++) {
			components[i] = Math.min(components1[i] + components2[i], 1.0f);
		}
		return new Color(components[0], components[1], components[2], components[3]);
	}

	public static Color subtract(Color color1, Color color2) {
		float[] components1 = color1.getRGBComponents(null);
		float[] components2 = color2.getRGBComponents(null);
		float[] components = new float[4];
		for (int i = 0; i < 4; i++) {
			components[i] = Math.max(components1[i] - components2[i], 0.0f);
		}
		return new Color(components[0], components[1], components[2], components[3]);
	}

	public static Color average(Color color1, Color color2) {
		float[] components1 = color1.getRGBComponents(null);
		float[] components2 = color2.getRGBComponents(null);
		float[] components = new float[4];
		for (int i = 0; i < 4; i++) {
			components[i] = 0.5f * (components1[i] + components2[i]);
		}
		return new Color(components[0], components[1], components[2], components[3]);
	}

	public static Color multiply(Color color1, Color color2) {
		float[] components1 = color1.getRGBComponents(null);
		float[] components2 = color2.getRGBComponents(null);
		float[] components = new float[4];
		for (int i = 0; i < 4; i++) {
			components[i] = components1[i] * components2[i];
		}
		return new Color(components[0], components[1], components[2], components[3]);
	}

	public static Color screen(Color color1, Color color2) {
		float[] components1 = color1.getRGBComponents(null);
		float[] components2 = color2.getRGBComponents(null);
		float[] components = new float[4];
		for (int i = 0; i < 4; i++) {
			components[i] = 1.0f - (1.0f - components1[i]) * (1.0f - components2[i]);
		}
		return new Color(components[0], components[1], components[2], components[3]);
	}

	public static float getLuminance(Color color) {
		float[] components = color.getRGBComponents(null);
		return 0.3f * components[0] + 0.59f * components[1] + 0.11f * components[2];
	}

	public static String toHtmlColor(Color color) {
		return String.format("#%06x", color.getRGB() & 0xffffff);
	}
}