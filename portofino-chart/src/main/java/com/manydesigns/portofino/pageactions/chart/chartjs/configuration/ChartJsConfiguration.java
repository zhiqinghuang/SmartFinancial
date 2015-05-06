package com.manydesigns.portofino.pageactions.chart.chartjs.configuration;

import com.manydesigns.elements.annotations.*;
import com.manydesigns.elements.util.BootstrapSizes;
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
public class ChartJsConfiguration implements PageActionConfiguration {
	protected String name;
	protected String type;
	protected String database;
	protected String query;

	protected Type actualType;
	protected Database actualDatabase;

	@Inject(DatabaseModule.PERSISTENCE)
	public Persistence persistence;

	public static final Logger logger = LoggerFactory.getLogger(ChartJsConfiguration.class);

	public ChartJsConfiguration() {
		super();
	}

	public static enum Type {

		LINE("Line", 2), BAR("Bar", 2), RADAR("Radar", 2), PIE("Pie", 1), POLAR("PolarArea", 1), DOUGHNUT("Doughnut", 1);

		public final String jsName;
		public final int kind;

		Type(String jsName, int kind) {
			this.jsName = jsName;
			this.kind = kind;
		}

		public String getJsName() {
			return jsName;
		}

		public int getKind() {
			return kind;
		}
	}

	public void init() {
		assert name != null;
		assert type != null;
		assert database != null;
		assert query != null;
		actualDatabase = DatabaseLogic.findDatabaseByName(persistence.getModel(), database);
		actualType = Type.valueOf(type);
	}

	@Required
	@XmlAttribute(required = true)
	@LabelI18N("name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlAttribute(name = "type", required = true)
	@Required
	@LabelI18N("type")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Required
	@XmlAttribute(required = true)
	@LabelI18N("database")
	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}

	@Required
	@LabelI18N("sql.query")
	@Multiline
	@XmlAttribute(required = true)
	@CssClass(BootstrapSizes.FILL_ROW)
	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public Database getActualDatabase() {
		return actualDatabase;
	}

	public Type getActualType() {
		return actualType;
	}
}