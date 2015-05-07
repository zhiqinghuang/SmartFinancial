package com.manydesigns.portofino.pageactions.chart.jfreechart;

import com.manydesigns.elements.xml.XhtmlBuffer;
import com.manydesigns.elements.xml.XhtmlFragment;
import org.jetbrains.annotations.NotNull;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.entity.ChartEntity;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class JFreeChartInstance implements XhtmlFragment {
	protected final JFreeChart chart;
	protected final File file;
	protected final ChartRenderingInfo renderingInfo;
	protected final int width;
	protected final int height;

	protected final String chartUrl;
	protected final String alt;
	protected final String mapId;

	public JFreeChartInstance(JFreeChart chart, File file, String mapId, String alt, int width, int height, String chartUrl) throws IOException {
		this.chart = chart;
		this.file = file;
		this.mapId = mapId;
		this.alt = alt;
		this.width = width;
		this.height = height;
		this.chartUrl = chartUrl;
		renderingInfo = new ChartRenderingInfo();
		ChartUtilities.saveChartAsPNG(file, chart, width, height, renderingInfo);
	}

	public void toXhtml(@NotNull XhtmlBuffer xb) {
		xb.openElement("img");
		xb.addAttribute("src", chartUrl);
		xb.addAttribute("alt", alt);
		xb.addAttribute("usemap", "#" + mapId);
		xb.addAttribute("style", "border: none;");
		xb.addAttribute("class", "img-responsive");
		xb.closeElement("img");

		xb.openElement("map");
		xb.addAttribute("id", mapId);
		xb.addAttribute("name", mapId);
		Iterator iter = renderingInfo.getEntityCollection().iterator();
		while (iter.hasNext()) {
			ChartEntity ce = (ChartEntity) iter.next();
			String shapeType = ce.getShapeType();
			String shapeCoords = ce.getShapeCoords();
			String tooltipText = ce.getToolTipText();
			String urltext = ce.getURLText();

			if (urltext == null)
				continue;

			xb.openElement("area");
			xb.addAttribute("shape", shapeType);
			xb.addAttribute("coords", shapeCoords);
			xb.addAttribute("title", tooltipText);
			xb.addAttribute("alt", tooltipText);
			xb.addAttribute("href", urltext);
			xb.closeElement("area");
		}
		xb.closeElement("map");
	}
}