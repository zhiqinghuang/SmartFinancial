package com.manydesigns.portofino.pageactions.calendar;

import com.manydesigns.elements.gfx.ColorUtils;

import java.awt.*;

public class Calendar {
	protected String id;
	protected String name;
	protected Color foregroundColor;
	protected Color backgroundColor;

	public Calendar() {
	}

	public Calendar(String id, String name, Color color) {
		this.id = id;
		this.name = name;
		this.backgroundColor = color;
		this.foregroundColor = ColorUtils.subtract(backgroundColor, new Color(0x16, 0x16, 0x16));
	}

	public Calendar(String id, String name, Color backgroundColor, Color foregroundColor) {
		this.id = id;
		this.name = name;
		this.foregroundColor = foregroundColor;
		this.backgroundColor = backgroundColor;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Color getForegroundColor() {
		return foregroundColor;
	}

	public void setForegroundColor(Color foregroundColor) {
		this.foregroundColor = foregroundColor;
	}

	public Color getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public String getForegroundHtmlColor() {
		return ColorUtils.toHtmlColor(foregroundColor);
	}

	public String getBackgroundHtmlColor() {
		return ColorUtils.toHtmlColor(backgroundColor);
	}
}