package com.manydesigns.portofino.chart;

import com.manydesigns.portofino.persistence.Persistence;
import org.jfree.chart.JFreeChart;

import java.util.Locale;

public interface ChartGenerator {
	JFreeChart generate(ChartDefinition chartDefinition, Persistence persistence, Locale locale);

	boolean isAntiAlias();

	void setAntiAlias(boolean antiAlias);

	int getWidth();

	void setWidth(int width);

	int getHeight();

	void setHeight(int height);

	boolean isBorderVisible();

	void setBorderVisible(boolean borderVisible);

}