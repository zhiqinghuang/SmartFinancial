package com.manydesigns.portofino.chart;

import com.manydesigns.elements.text.OgnlTextFormat;
import org.jfree.chart.urls.PieURLGenerator;
import org.jfree.data.general.PieDataset;

public class ChartPieUrlGenerator implements PieURLGenerator {

	protected final OgnlTextFormat format;
	protected final PieURLGeneratorValue value;

	public ChartPieUrlGenerator(String expression) {
		format = OgnlTextFormat.create(expression);
		format.setUrl(true);
		value = new PieURLGeneratorValue();
	}

	public String generateURL(PieDataset dataset, Comparable key, int index) {
		value.dataset = dataset;
		value.key = ((ComparableWrapper) key).getObject();
		value.index = index;
		return format.format(value);
	}

	static class PieURLGeneratorValue {
		public PieDataset dataset;
		public Comparable key;
		public int index;
	}
}