package com.manydesigns.portofino.pageactions.m2m.configuration;

import com.manydesigns.elements.annotations.CssClass;
import com.manydesigns.elements.annotations.Multiline;
import com.manydesigns.elements.annotations.Required;
import com.manydesigns.elements.util.BootstrapSizes;
import com.manydesigns.portofino.di.Inject;
import com.manydesigns.portofino.dispatcher.PageActionConfiguration;
import com.manydesigns.portofino.model.database.*;
import com.manydesigns.portofino.modules.DatabaseModule;
import com.manydesigns.portofino.persistence.Persistence;
import com.manydesigns.portofino.persistence.QueryUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "configuration")
@XmlAccessorType(XmlAccessType.NONE)
public class ManyToManyConfiguration implements PageActionConfiguration {
	protected String oneExpression;
	protected String onePropertyName;
	protected SelectionProviderReference oneSelectionProvider;

	protected SelectionProviderReference manySelectionProvider;

	protected String database;
	protected String query;
	protected String viewType;

	protected String actualOnePropertyName;
	protected Database actualOneDatabase;
	protected Database actualManyDatabase;
	protected Database actualRelationDatabase;
	protected Table actualRelationTable;
	protected Table actualManyTable;
	protected ViewType actualViewType;

	@Inject(DatabaseModule.PERSISTENCE)
	public Persistence persistence;

	public static final Logger logger = LoggerFactory.getLogger(ManyToManyConfiguration.class);

	public ManyToManyConfiguration() {
	}

	public void init() {
		try {
			actualViewType = ViewType.valueOf(viewType);
		} catch (Exception e) {
			actualViewType = ViewType.CHECKBOXES;
			if (!StringUtils.isEmpty(viewType)) {
				logger.warn("Invalid viewType: " + viewType);
			}
		}

		if (database != null && query != null) {
			actualRelationDatabase = DatabaseLogic.findDatabaseByName(persistence.getModel(), database);
			if (actualRelationDatabase != null) {
				actualRelationTable = QueryUtils.getTableFromQueryString(actualRelationDatabase, query);

				if (actualRelationTable != null) {
					if (manySelectionProvider != null) {
						manySelectionProvider.init(actualRelationTable);
						if (manySelectionProvider.getActualSelectionProvider() != null) {
							ModelSelectionProvider actualSelectionProvider = manySelectionProvider.getActualSelectionProvider();
							String manyDatabaseName = actualSelectionProvider.getToDatabase();
							actualManyDatabase = DatabaseLogic.findDatabaseByName(persistence.getModel(), manyDatabaseName);
							actualManyTable = actualSelectionProvider.getToTable();
							if (actualManyTable == null && actualSelectionProvider instanceof DatabaseSelectionProvider) {
								logger.debug("Trying to determine the many table from the selection provider query");
								String hql = ((DatabaseSelectionProvider) actualSelectionProvider).getHql();
								if (hql != null) {
									actualManyTable = QueryUtils.getTableFromQueryString(actualManyDatabase, hql);
								}
							}
							if (actualManyTable == null) {
								logger.error("Invalid selection provider: only foreign keys or HQL selection providers that select a single entity are supported");
							}
						} else {
							logger.error("Many-side selection provider not found");
						}
					} else {
						logger.error("Many-side selection provider is required");
					}

					if (oneSelectionProvider != null) {
						oneSelectionProvider.init(actualRelationTable);
						String oneDatabaseName = oneSelectionProvider.getActualSelectionProvider().getToDatabase();
						actualOneDatabase = DatabaseLogic.findDatabaseByName(persistence.getModel(), oneDatabaseName);
					}
				} else {
					logger.error("Table not found");
				}
			} else {
				logger.error("Relation database " + database + " not found");
			}

			if (StringUtils.isBlank(oneExpression) && getOneSelectionProvider() != null) {
				// TODO chiave multipla
				try {
					actualOnePropertyName = getOneSelectionProvider().getActualSelectionProvider().getReferences().get(0).getActualFromColumn().getActualPropertyName();
				} catch (Throwable t) {
					logger.error("Couldn't determine one property name", t);
				}
			} else {
				actualOnePropertyName = onePropertyName;
			}
		}
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
	@Multiline
	@CssClass(BootstrapSizes.FILL_ROW)
	@XmlAttribute(required = true)
	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	@Required
	@XmlAttribute(required = true)
	public String getViewType() {
		return viewType;
	}

	public void setViewType(String viewType) {
		this.viewType = viewType;
	}

	@CssClass(BootstrapSizes.COL_SM_6)
	@XmlAttribute(required = false)
	public String getOneExpression() {
		return oneExpression;
	}

	public void setOneExpression(String oneExpression) {
		this.oneExpression = oneExpression;
	}

	@XmlAttribute(required = false)
	@CssClass(BootstrapSizes.COL_SM_6)
	public String getOnePropertyName() {
		return onePropertyName;
	}

	public void setOnePropertyName(String onePropertyName) {
		this.onePropertyName = onePropertyName;
	}

	@XmlElement(name = "one", required = false)
	public SelectionProviderReference getOneSelectionProvider() {
		return oneSelectionProvider;
	}

	public void setOneSelectionProvider(SelectionProviderReference oneSelectionProvider) {
		this.oneSelectionProvider = oneSelectionProvider;
	}

	@Required
	@XmlElement(name = "many", required = true)
	public SelectionProviderReference getManySelectionProvider() {
		return manySelectionProvider;
	}

	public void setManySelectionProvider(SelectionProviderReference manySelectionProvider) {
		this.manySelectionProvider = manySelectionProvider;
	}

	public Database getActualOneDatabase() {
		return actualOneDatabase;
	}

	public Database getActualManyDatabase() {
		return actualManyDatabase;
	}

	public ViewType getActualViewType() {
		return actualViewType;
	}

	public Database getActualRelationDatabase() {
		return actualRelationDatabase;
	}

	public Table getActualRelationTable() {
		return actualRelationTable;
	}

	public Table getActualManyTable() {
		return actualManyTable;
	}

	public String getActualOnePropertyName() {
		return actualOnePropertyName;
	}
}