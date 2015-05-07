package com.manydesigns.portofino.pageactions.chart.jfreechart.configuration;

import com.manydesigns.elements.annotations.*;
import com.manydesigns.elements.util.BootstrapSizes;
import com.manydesigns.portofino.chart.*;
import com.manydesigns.portofino.di.Inject;
import com.manydesigns.portofino.dispatcher.PageActionConfiguration;
import com.manydesigns.portofino.model.database.Database;
import com.manydesigns.portofino.model.database.DatabaseLogic;
import com.manydesigns.portofino.modules.DatabaseModule;
import com.manydesigns.portofino.persistence.Persistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "configuration")
@XmlAccessorType(XmlAccessType.NONE)
public class JFreeChartConfiguration implements PageActionConfiguration, ChartDefinition {
	protected String name;
	protected String type;
	protected String legend;
	protected String database;
	protected String query;
	protected String urlExpression;
	protected String xAxisName;
	protected String yAxisName;
	protected String orientation;

	protected Database actualDatabase;
	protected Type actualType;
	protected Class<? extends ChartGenerator> generatorClass;
	protected Orientation actualOrientation;

	@Inject(DatabaseModule.PERSISTENCE)
	public Persistence persistence;

	public static enum Type {
		AREA(ChartAreaGenerator.class), BAR(ChartBarGenerator.class), BAR3D(ChartBar3DGenerator.class), LINE(ChartLineGenerator.class), LINE3D(ChartLine3DGenerator.class), PIE(ChartPieGenerator.class), PIE3D(ChartPie3DGenerator.class), RING(ChartRingGenerator.class), STACKED_BAR(ChartStackedBarGenerator.class), STACKED_BAR_3D(ChartStackedBar3DGenerator.class);

		private Class<? extends ChartGenerator> generatorClass;

		Type(Class<? extends ChartGenerator> generatorClass) {
			this.generatorClass = generatorClass;
		}

		public Class<? extends ChartGenerator> getGeneratorClass() {
			return generatorClass;
		}
	}

	public static final Logger logger = LoggerFactory.getLogger(JFreeChartConfiguration.class);

	public JFreeChartConfiguration() {
		super();
	}

	public void init() {
		assert name != null;
		assert type != null;
		assert legend != null;
		assert database != null;
		assert query != null;

		try {
			actualType = Type.valueOf(type);
			generatorClass = actualType.getGeneratorClass();
		} catch (Exception e) {
			logger.error("Invalid chart type: " + type, e);
		}

		if (orientation != null) {
			try {
				actualOrientation = Orientation.valueOf(orientation);
			} catch (Exception e) {
				logger.error("Invalid orientation: " + actualOrientation, e);
			}
		}
		actualDatabase = DatabaseLogic.findDatabaseByName(persistence.getModel(), database);
	}

	@Required
	@XmlAttribute(required = true)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlAttribute(name = "type", required = true)
	@Required
	@Label("Type")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Type getActualType() {
		return actualType;
	}

	@Required
	@XmlAttribute(required = true)
	public String getLegend() {
		return legend;
	}

	public void setLegend(String legend) {
		this.legend = legend;
	}

	@Required
	@XmlAttribute(required = true)
	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}

	@Required
	@Label("SQL Query")
	@Multiline
	@XmlAttribute(required = true)
	@CssClass(BootstrapSizes.FILL_ROW)
	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	@Label("URL expression")
	@XmlAttribute(required = true)
	public String getUrlExpression() {
		return urlExpression;
	}

	public void setUrlExpression(String urlExpression) {
		this.urlExpression = urlExpression;
	}

	public Database getActualDatabase() {
		return actualDatabase;
	}

	public Class<? extends ChartGenerator> getGeneratorClass() {
		return generatorClass;
	}

	@XmlAttribute(name = "xAxisName")
	public String getXAxisName() {
		return xAxisName;
	}

	public void setXAxisName(String xAxisName) {
		this.xAxisName = xAxisName;
	}

	@XmlAttribute(name = "yAxisName")
	public String getYAxisName() {
		return yAxisName;
	}

	public void setYAxisName(String yAxisName) {
		this.yAxisName = yAxisName;
	}

	@XmlAttribute(name = "orientation")
	@Label("Orientation")
	@Required
	@Select(nullOption = false)
	public String getOrientation() {
		return orientation;
	}

	public void setOrientation(String orientation) {
		this.orientation = orientation;
	}

	public Orientation getActualOrientation() {
		return actualOrientation;
	}
}