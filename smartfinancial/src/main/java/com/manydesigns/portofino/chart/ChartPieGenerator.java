package com.manydesigns.portofino.chart;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.PieDataset;

public class ChartPieGenerator extends Chart1DGenerator {
	protected JFreeChart createChart(ChartDefinition chartDefinition, PieDataset dataset) {
		return ChartFactory.createPieChart(chartDefinition.getName(), dataset, true, true, true);
	}
}