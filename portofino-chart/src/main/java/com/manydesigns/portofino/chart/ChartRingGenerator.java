package com.manydesigns.portofino.chart;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.PieDataset;

public class ChartRingGenerator extends Chart1DGenerator {
	protected JFreeChart createChart(ChartDefinition chartDefinition, PieDataset dataset) {
		return ChartFactory.createRingChart(chartDefinition.getName(), dataset, true, true, true);
	}
}