package com.manydesigns.portofino.chart;

import com.manydesigns.elements.ElementsThreadLocals;
import com.manydesigns.portofino.persistence.Persistence;
import com.manydesigns.portofino.persistence.QueryUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.DrawingSupplier;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.chart.title.Title;
import org.jfree.chart.urls.PieURLGenerator;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.ui.HorizontalAlignment;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.VerticalAlignment;

import java.awt.*;
import java.util.Locale;

public abstract class Chart1DGenerator extends AbstractChartGenerator {
	private final Font titleFont = new Font("SansSerif", Font.BOLD, 12);
	private final Font legendFont = new Font("SansSerif", Font.BOLD, 10);
	private final Font legendItemFont = new Font("SansSerif", Font.PLAIN, 10);
	private final Color transparentColor = new Color(0, true);

	public JFreeChart generate(ChartDefinition chartDefinition, Persistence persistence, Locale locale) {
		DefaultPieDataset dataset = new DefaultPieDataset();
		java.util.List<Object[]> result;
		String query = chartDefinition.getQuery();
		Session session = persistence.getSession(chartDefinition.getDatabase());
		result = QueryUtils.runSql(session, query);
		for (Object[] current : result) {
			ComparableWrapper key = new ComparableWrapper((Comparable) current[0]);
			dataset.setValue(key, (Number) current[1]);
			if (current.length > 2) {
				key.setLabel(current[2].toString());
			}
		}

		JFreeChart chart = createChart(chartDefinition, dataset);

		chart.setAntiAlias(isAntiAlias());

		// impostiamo il bordo invisibile
		// eventualmente e' il css a fornirne uno
		// eventualmente e' il css a fornirne uno
		chart.setBorderVisible(isBorderVisible());

		// impostiamo il titolo
		TextTitle title = chart.getTitle();
		title.setFont(titleFont);
		title.setMargin(10.0, 0.0, 0.0, 0.0);

		// ottieni il Plot
		PiePlot plot = (PiePlot) chart.getPlot();

		String urlExpression = chartDefinition.getUrlExpression();
		if (!StringUtils.isBlank(urlExpression)) {
			PieURLGenerator urlGenerator = new ChartPieUrlGenerator(urlExpression);
			plot.setURLGenerator(urlGenerator);
		} else {
			plot.setURLGenerator(null);
		}

		// il plot ha sfondo e bordo trasparente
		// (quindi si vede il colore del chart)
		plot.setBackgroundPaint(transparentColor);
		plot.setOutlinePaint(transparentColor);

		// Modifico il toolTip
		// plot.setToolTipGenerator(new
		// StandardPieToolTipGenerator("{0} = {1} ({2})"));

		// imposta la distanza delle etichette dal plot
		plot.setLabelGap(0.03);
		// plot.setLabelGenerator(new MyPieSectionLabelGenerator());

		// imposta il messaggio se non ci sono dati
		plot.setNoDataMessage(ElementsThreadLocals.getText("no.data.available"));

		plot.setCircular(true);

		plot.setBaseSectionOutlinePaint(Color.BLACK);

		DrawingSupplier supplier = new DesaturatedDrawingSupplier(plot.getDrawingSupplier());
		plot.setDrawingSupplier(supplier);

		// impostiamo il titolo della legenda
		String legendString = chartDefinition.getLegend();
		Title subtitle = new TextTitle(legendString, legendFont, Color.BLACK, RectangleEdge.BOTTOM, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, new RectangleInsets(0, 0, 0, 0));
		subtitle.setMargin(0, 0, 5, 0);
		chart.addSubtitle(subtitle);

		// impostiamo la legenda
		LegendTitle legend = chart.getLegend();
		legend.setBorder(0, 0, 0, 0);
		legend.setItemFont(legendItemFont);
		int legendMargin = 10;
		legend.setMargin(0.0, legendMargin, legendMargin, legendMargin);
		legend.setBackgroundPaint(transparentColor);

		// impostiamo un gradiente orizzontale
		Paint chartBgPaint = new GradientPaint(0, 0, new Color(255, 253, 240), 0, getHeight(), Color.WHITE);
		chart.setBackgroundPaint(chartBgPaint);
		return chart;
	}

	protected abstract JFreeChart createChart(ChartDefinition chartDefinition, PieDataset dataset);

}