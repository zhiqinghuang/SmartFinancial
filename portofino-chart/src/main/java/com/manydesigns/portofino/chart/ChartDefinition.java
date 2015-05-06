package com.manydesigns.portofino.chart;

public interface ChartDefinition {

	String getQuery();

	String getDatabase();

	String getLegend();

	String getUrlExpression();

	Orientation getActualOrientation();

	String getName();

	String getXAxisName();

	String getYAxisName();

	public static enum Orientation {
		HORIZONTAL, VERTICAL
	}
}