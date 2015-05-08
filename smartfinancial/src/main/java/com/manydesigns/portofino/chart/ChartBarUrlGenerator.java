package com.manydesigns.portofino.chart;

import org.jfree.chart.urls.CategoryURLGenerator;
import org.jfree.data.category.CategoryDataset;

import com.manydesigns.elements.text.OgnlTextFormat;

public class ChartBarUrlGenerator implements CategoryURLGenerator {

	protected final OgnlTextFormat format;
	protected final BarURLGeneratorValue value;

	public ChartBarUrlGenerator(String expression) {
		format = OgnlTextFormat.create(expression);
		format.setUrl(true);
		value = new BarURLGeneratorValue();
	}

	public String generateURL(CategoryDataset dataset, int series, int category) {
		ComparableWrapper c1 = (ComparableWrapper) dataset.getRowKey(series);
		ComparableWrapper c2 = (ComparableWrapper) dataset.getColumnKey(category);
		value.dataset = dataset;
		value.series = c1.getObject();
		value.category = c2.getObject();
		return format.format(value);
	}

	static class BarURLGeneratorValue {
		public CategoryDataset dataset;
		public Comparable series;
		public Comparable category;
	}
}