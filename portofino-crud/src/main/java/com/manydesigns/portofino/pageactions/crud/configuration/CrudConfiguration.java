package com.manydesigns.portofino.pageactions.crud.configuration;

import com.manydesigns.elements.annotations.CssClass;
import com.manydesigns.elements.annotations.LabelI18N;
import com.manydesigns.elements.annotations.Multiline;
import com.manydesigns.elements.util.BootstrapSizes;
import com.manydesigns.portofino.di.Inject;
import com.manydesigns.portofino.dispatcher.ConfigurationWithDefaults;
import com.manydesigns.portofino.dispatcher.PageActionConfiguration;
import com.manydesigns.portofino.model.database.Database;
import com.manydesigns.portofino.model.database.DatabaseLogic;
import com.manydesigns.portofino.model.database.Table;
import com.manydesigns.portofino.modules.DatabaseModule;
import com.manydesigns.portofino.persistence.Persistence;
import com.manydesigns.portofino.persistence.QueryUtils;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "configuration")
@XmlAccessorType(value = XmlAccessType.NONE)
public class CrudConfiguration implements PageActionConfiguration, ConfigurationWithDefaults {
	protected final List<CrudProperty> properties;
	protected final List<SelectionProviderReference> selectionProviders;

	protected String name;
	protected String database;
	protected String query;
	protected String searchTitle;
	protected String createTitle;
	protected String readTitle;
	protected String editTitle;
	protected String variable;

	protected boolean largeResultSet;

	protected Integer rowsPerPage;

	@Inject(DatabaseModule.PERSISTENCE)
	public Persistence persistence;

	protected Table actualTable;
	protected Database actualDatabase;

	public CrudConfiguration() {
		properties = new ArrayList<CrudProperty>();
		selectionProviders = new ArrayList<SelectionProviderReference>();
	}

	public CrudConfiguration(String name, String database, String query, String searchTitle, String createTitle, String readTitle, String editTitle) {
		this();
		this.name = name;
		this.database = database;
		this.query = query;
		this.searchTitle = searchTitle;
		this.createTitle = createTitle;
		this.readTitle = readTitle;
		this.editTitle = editTitle;
	}

	public void init() {
		actualDatabase = DatabaseLogic.findDatabaseByName(persistence.getModel(), database);
		if (actualDatabase != null) {
			actualTable = QueryUtils.getTableFromQueryString(actualDatabase, query);
		}
		for (CrudProperty property : properties) {
			property.init(persistence.getModel());
		}
		// TODO gestire table == null
		for (SelectionProviderReference ref : selectionProviders) {
			ref.init(getActualTable());
		}
	}

	public void setupDefaults() {
		rowsPerPage = 10;
	}

	@XmlElementWrapper(name = "properties")
	@XmlElement(name = "property", type = CrudProperty.class)
	public List<CrudProperty> getProperties() {
		return properties;
	}

	@LabelI18N("name")
	@XmlAttribute(required = true)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlAttribute(required = true)
	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}

	public Database getActualDatabase() {
		return actualDatabase;
	}

	public void setActualDatabase(Database actualDatabase) {
		this.actualDatabase = actualDatabase;
	}

	@Multiline
	@CssClass(BootstrapSizes.FILL_ROW)
	@XmlAttribute(required = true)
	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	@CssClass(BootstrapSizes.FILL_ROW)
	@XmlAttribute(required = false)
	public String getSearchTitle() {
		return searchTitle;
	}

	public void setSearchTitle(String searchTitle) {
		this.searchTitle = searchTitle;
	}

	@CssClass(BootstrapSizes.FILL_ROW)
	@XmlAttribute(required = false)
	public String getCreateTitle() {
		return createTitle;
	}

	public void setCreateTitle(String createTitle) {
		this.createTitle = createTitle;
	}

	@CssClass(BootstrapSizes.FILL_ROW)
	@XmlAttribute(required = false)
	public String getReadTitle() {
		return readTitle;
	}

	public void setReadTitle(String readTitle) {
		this.readTitle = readTitle;
	}

	@CssClass(BootstrapSizes.FILL_ROW)
	@XmlAttribute(required = false)
	public String getEditTitle() {
		return editTitle;
	}

	public void setEditTitle(String editTitle) {
		this.editTitle = editTitle;
	}

	@XmlAttribute(required = false)
	public String getVariable() {
		return variable;
	}

	public void setVariable(String variable) {
		this.variable = variable;
	}

	public String getActualVariable() {
		return variable != null ? variable : name;
	}

	@XmlElementWrapper(name = "selectionProviders")
	@XmlElements({ @XmlElement(name = "selectionProvider", type = SelectionProviderReference.class) })
	public List<SelectionProviderReference> getSelectionProviders() {
		return selectionProviders;
	}

	@XmlAttribute(required = true)
	public boolean isLargeResultSet() {
		return largeResultSet;
	}

	public void setLargeResultSet(boolean largeResultSet) {
		this.largeResultSet = largeResultSet;
	}

	public Table getActualTable() {
		return actualTable;
	}

	@CssClass(BootstrapSizes.COL_SM_1)
	@XmlAttribute(required = false)
	public Integer getRowsPerPage() {
		return rowsPerPage;
	}

	public void setRowsPerPage(Integer rowsPerPage) {
		this.rowsPerPage = rowsPerPage;
	}
}