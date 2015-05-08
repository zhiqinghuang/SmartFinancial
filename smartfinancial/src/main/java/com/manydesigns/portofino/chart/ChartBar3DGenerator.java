package com.manydesigns.portofino.chart;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;

public class ChartBar3DGenerator extends Chart2DGenerator {
	protected JFreeChart createChart(ChartDefinition chartDefinition, CategoryDataset dataset, PlotOrientation plotOrientation) {
		return ChartFactory.createBarChart3D(chartDefinition.getName(), chartDefinition.getXAxisName(), chartDefinition.getYAxisName(), dataset, plotOrientation, true, true, true);
	}
}