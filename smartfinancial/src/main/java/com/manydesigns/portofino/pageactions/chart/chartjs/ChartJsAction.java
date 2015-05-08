package com.manydesigns.portofino.pageactions.chart.chartjs;

import com.manydesigns.elements.ElementsThreadLocals;
import com.manydesigns.elements.fields.Field;
import com.manydesigns.elements.forms.Form;
import com.manydesigns.elements.forms.FormBuilder;
import com.manydesigns.elements.messages.SessionMessages;
import com.manydesigns.elements.options.DefaultSelectionProvider;
import com.manydesigns.elements.options.SelectionProvider;
import com.manydesigns.elements.util.RandomUtil;
import com.manydesigns.portofino.buttons.annotations.Button;
import com.manydesigns.portofino.di.Inject;
import com.manydesigns.portofino.logic.SelectionProviderLogic;
import com.manydesigns.portofino.model.database.Database;
import com.manydesigns.portofino.modules.DatabaseModule;
import com.manydesigns.portofino.pageactions.AbstractPageAction;
import com.manydesigns.portofino.pageactions.PageActionName;
import com.manydesigns.portofino.pageactions.annotations.ConfigurationClass;
import com.manydesigns.portofino.pageactions.annotations.ScriptTemplate;
import com.manydesigns.portofino.pageactions.chart.chartjs.configuration.ChartJsConfiguration;
import com.manydesigns.portofino.persistence.Persistence;
import com.manydesigns.portofino.persistence.QueryUtils;
import com.manydesigns.portofino.security.AccessLevel;
import com.manydesigns.portofino.security.RequiresPermissions;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ErrorResolution;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import org.apache.commons.lang.ObjectUtils;
import org.hibernate.Session;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@RequiresPermissions(level = AccessLevel.VIEW)
@ScriptTemplate("script_template.groovy")
@ConfigurationClass(ChartJsConfiguration.class)
@PageActionName("Chart (with Chart.js)")
public class ChartJsAction extends AbstractPageAction {
	@Inject(DatabaseModule.PERSISTENCE)
	public Persistence persistence;

	public String chartId;
	public String chartData;
	public ChartJsConfiguration chartConfiguration;
	public Form form;
	public int width = 470;
	public int height = 354;

	public static final Logger logger = LoggerFactory.getLogger(ChartJsAction.class);

	@DefaultHandler
	public Resolution execute() {
		if (chartConfiguration == null) {
			return forwardToPageActionNotConfigured();
		}
		List<Object[]> result = loadChartData();
		if (result == null) {
			return forwardTo("/m/chart/chartjs/no-data-available.jsp");
		}
		if (chartConfiguration.getActualType().kind == 1) {
			JSONArray data = new JSONArray();
			if (!fillData1D(result, data)) {
				return forwardToPageActionNotConfigured();
			}
			chartData = data.toString();
		} else if (chartConfiguration.getActualType().kind == 2) {
			JSONObject data = new JSONObject();
			if (!fillData2D(result, data)) {
				return forwardToPageActionNotConfigured();
			}
			chartData = data.toString();
		} else {
			logger.error("Unsupported chart type: " + chartConfiguration.getActualType());
			return forwardToPageActionNotConfigured();
		}
		return forwardTo("/m/chart/chartjs/display.jsp");
	}

	protected List<Object[]> loadChartData() {
		try {
			chartId = RandomUtil.createRandomId();
			String query = chartConfiguration.getQuery();
			Session session = persistence.getSession(chartConfiguration.getDatabase());
			return QueryUtils.runSql(session, query);
		} catch (Exception e) {
			logger.error("Error executing query", e);
			return null;
		}
	}

	protected boolean fillData1D(List<Object[]> result, JSONArray data) {
		for (Object[] current : result) {
			if (current.length < 2) {
				SessionMessages.addErrorMessage("The query returned the wrong number of parameters (" + current.length + ") - 2 are required.");
				return false;
			}
			JSONObject datum = new JSONObject();
			datum.put("label", ObjectUtils.toString(current[0]));
			datum.put("value", current[1]);
			data.put(datum);
		}
		return true;
	}

	protected boolean fillData2D(List<Object[]> result, JSONObject data) {
		data.put("labels", new JSONArray());
		data.put("datasets", new JSONArray());
		Set<String> labels = new LinkedHashSet<String>();
		Map<String, JSONObject> datasets = new LinkedHashMap<String, JSONObject>();
		int labelIndex = 0;
		for (Object[] current : result) {
			if (current.length < 3) {
				SessionMessages.addErrorMessage("The query returned the wrong number of parameters (" + current.length + ") - 3 are required.");
				return false;
			}
			String datasetName = ObjectUtils.toString(current[0]);
			JSONObject dataset = datasets.get(datasetName);
			if (dataset == null) {
				dataset = new JSONObject();
				dataset.put("label", datasetName);
				JSONArray dataValues = new JSONArray();
				dataset.put("data", dataValues);
				for (int i = 0; i < labelIndex; i++) {
					dataValues.put(0);
				}
				datasets.put(datasetName, dataset);
			}
			String label = ObjectUtils.toString(current[1]);
			if (!labels.contains(label)) {
				labels.add(label);
				data.getJSONArray("labels").put(label);
				labelIndex++;
				for (JSONObject ds : datasets.values()) {
					ds.getJSONArray("data").put(0);
				}
			}
			int index = indexOf(label, labels);
			dataset.getJSONArray("data").put(index, current[2]);
		}
		JSONArray jsonDatasets = data.getJSONArray("datasets");
		for (JSONObject dataset : datasets.values()) {
			jsonDatasets.put(dataset);
		}
		return true;
	}

	protected static int indexOf(String label, Set<String> labels) {
		int i = 0;
		for (String s : labels) {
			if (label.equals(s)) {
				return i;
			}
			i++;
		}
		return -1;
	}

	public static final String[][] CONFIGURATION_FIELDS = { { "name", "type", "database", "query" } };

	public static final String[] chartTypes1D;
	public static final String[] chartTypes2D;

	static {
		List<String> types1D = new ArrayList<String>();
		List<String> types2D = new ArrayList<String>();
		for (ChartJsConfiguration.Type type : ChartJsConfiguration.Type.values()) {
			if (type.kind == 1) {
				types1D.add(type.name());
			} else {
				types2D.add(type.name());
			}
		}
		chartTypes1D = types1D.toArray(new String[0]);
		chartTypes2D = types2D.toArray(new String[0]);
	}

	public final String[] chartTypeValues = new String[chartTypes1D.length + chartTypes2D.length + 2];
	public final String[] chartTypeLabels = new String[chartTypeValues.length];

	@Button(list = "pageHeaderButtons", titleKey = "configure", order = 1, icon = Button.ICON_WRENCH)
	@RequiresPermissions(level = AccessLevel.DEVELOP)
	public Resolution configure() {
		prepareConfigurationForms();
		return new ForwardResolution("/m/chart/chartjs/configure.jsp");
	}

	@Override
	protected void prepareConfigurationForms() {
		super.prepareConfigurationForms();

		String prefix = "com.manydesigns.portofino.chartjs.type.";
		chartTypeValues[0] = "--1D";
		chartTypeLabels[0] = "-- 1D charts --";
		for (int i = 0; i < chartTypes1D.length; i++) {
			chartTypeValues[i + 1] = chartTypes1D[i];
			chartTypeLabels[i + 1] = ElementsThreadLocals.getText(prefix + chartTypes1D[i], chartTypes1D[i]);
		}
		chartTypeValues[chartTypes1D.length + 1] = "--2D";
		chartTypeLabels[chartTypes1D.length + 1] = "-- 2D charts --";
		for (int i = 0; i < chartTypes2D.length; i++) {
			chartTypeValues[i + 2 + chartTypes1D.length] = chartTypes2D[i];
			chartTypeLabels[i + 2 + chartTypes1D.length] = ElementsThreadLocals.getText(prefix + chartTypes2D[i], chartTypes2D[i]);
		}

		SelectionProvider databaseSelectionProvider = SelectionProviderLogic.createSelectionProvider("database", persistence.getModel().getDatabases(), Database.class, null, new String[] { "databaseName" });
		DefaultSelectionProvider typeSelectionProvider = new DefaultSelectionProvider("type");
		for (int i = 0; i < chartTypeValues.length; i++) {
			typeSelectionProvider.appendRow(chartTypeValues[i], chartTypeLabels[i], true);
		}
		form = new FormBuilder(ChartJsConfiguration.class).configFields(CONFIGURATION_FIELDS).configFieldSetNames("Chart").configSelectionProvider(typeSelectionProvider, "type").configSelectionProvider(databaseSelectionProvider, "database").build();
		form.readFromObject(chartConfiguration);
	}

	@Button(list = "configuration", key = "update.configuration", order = 1, type = Button.TYPE_PRIMARY)
	@RequiresPermissions(level = AccessLevel.DEVELOP)
	public Resolution updateConfiguration() {
		prepareConfigurationForms();
		form.readFromObject(chartConfiguration);
		form.readFromRequest(context.getRequest());
		readPageConfigurationFromRequest();
		boolean valid = form.validate();
		valid = validatePageConfiguration() && valid;
		Field typeField = form.findFieldByPropertyName("type");
		String typeValue = typeField.getStringValue();
		boolean placeHolderValue = typeValue != null && typeValue.startsWith("--");
		if (placeHolderValue) {
			valid = false;
			String errorMessage = ElementsThreadLocals.getTextProvider().getText("elements.error.field.required");
			typeField.getErrors().add(errorMessage);
			SessionMessages.addErrorMessage("");
		}
		if (valid) {
			updatePageConfiguration();
			if (chartConfiguration == null) {
				chartConfiguration = new ChartJsConfiguration();
			}
			form.writeToObject(chartConfiguration);
			saveConfiguration(chartConfiguration);

			SessionMessages.addInfoMessage(ElementsThreadLocals.getText("configuration.updated.successfully"));
			return cancel();
		} else {
			return new ForwardResolution("/m/chart/chartjs/configure.jsp");
		}
	}

	public Resolution preparePage() {
		if (!pageInstance.getParameters().isEmpty()) {
			return new ErrorResolution(404);
		}
		chartConfiguration = (ChartJsConfiguration) pageInstance.getConfiguration();
		return null;
	}

	public ChartJsConfiguration getChartConfiguration() {
		return chartConfiguration;
	}

	public void setChartConfiguration(ChartJsConfiguration chartConfiguration) {
		this.chartConfiguration = chartConfiguration;
	}

	public Form getForm() {
		return form;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public String getChartId() {
		return chartId;
	}

	public void setChartId(String chartId) {
		this.chartId = chartId;
	}

	public String getChartData() {
		return chartData;
	}

	public void setChartData(String chartData) {
		this.chartData = chartData;
	}
}