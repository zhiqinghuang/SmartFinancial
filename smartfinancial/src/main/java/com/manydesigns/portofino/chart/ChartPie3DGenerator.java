package com.manydesigns.portofino.chart;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.PieDataset;

public class ChartPie3DGenerator extends Chart1DGenerator {
	protected JFreeChart createChart(ChartDefinition chartDefinition, PieDataset dataset) {
		return ChartFactory.createPieChart3D(chartDefinition.getName(), dataset, true, true, true);
	}
}