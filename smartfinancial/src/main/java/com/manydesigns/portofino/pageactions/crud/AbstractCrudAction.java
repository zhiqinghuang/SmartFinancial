package com.manydesigns.portofino.pageactions.crud;

import com.manydesigns.elements.ElementsThreadLocals;
import com.manydesigns.elements.FormElement;
import com.manydesigns.elements.Mode;
import com.manydesigns.elements.annotations.*;
import com.manydesigns.elements.blobs.Blob;
import com.manydesigns.elements.blobs.BlobManager;
import com.manydesigns.elements.blobs.BlobUtils;
import com.manydesigns.elements.fields.FileBlobField;
import com.manydesigns.elements.fields.Field;
import com.manydesigns.elements.fields.SelectField;
import com.manydesigns.elements.fields.TextField;
import com.manydesigns.elements.forms.FieldSet;
import com.manydesigns.elements.forms.*;
import com.manydesigns.elements.messages.SessionMessages;
import com.manydesigns.elements.options.DefaultSelectionProvider;
import com.manydesigns.elements.options.DisplayMode;
import com.manydesigns.elements.options.SearchDisplayMode;
import com.manydesigns.elements.options.SelectionProvider;
import com.manydesigns.elements.reflection.ClassAccessor;
import com.manydesigns.elements.reflection.PropertyAccessor;
import com.manydesigns.elements.servlet.MutableHttpServletRequest;
import com.manydesigns.elements.text.OgnlTextFormat;
import com.manydesigns.elements.util.MimeTypes;
import com.manydesigns.elements.util.Util;
import com.manydesigns.elements.xml.XhtmlBuffer;
import com.manydesigns.portofino.PortofinoProperties;
import com.manydesigns.portofino.buttons.GuardType;
import com.manydesigns.portofino.buttons.annotations.Button;
import com.manydesigns.portofino.buttons.annotations.Buttons;
import com.manydesigns.portofino.buttons.annotations.Guard;
import com.manydesigns.portofino.di.Inject;
import com.manydesigns.portofino.dispatcher.PageInstance;
import com.manydesigns.portofino.modules.BaseModule;
import com.manydesigns.portofino.pageactions.AbstractPageAction;
import com.manydesigns.portofino.pageactions.PageActionLogic;
import com.manydesigns.portofino.pageactions.crud.configuration.CrudConfiguration;
import com.manydesigns.portofino.pageactions.crud.configuration.CrudProperty;
import com.manydesigns.portofino.pageactions.crud.reflection.CrudAccessor;
import com.manydesigns.portofino.security.AccessLevel;
import com.manydesigns.portofino.security.RequiresPermissions;
import com.manydesigns.portofino.util.PkHelper;
import com.manydesigns.portofino.util.ShortNameUtils;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.util.UrlBuilder;
import ognl.OgnlContext;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.json.JSONException;
import org.json.JSONStringer;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractCrudAction<T> extends AbstractPageAction {
	public final static String SEARCH_STRING_PARAM = "searchString";
	public final static String prefix = "";
	public final static String searchPrefix = prefix + "search_";

	public static final String PERMISSION_CREATE = "crud-create", PERMISSION_EDIT = "crud-edit", PERMISSION_DELETE = "crud-delete";

	public static final Logger logger = LoggerFactory.getLogger(AbstractCrudAction.class);

	public String[] pk;
	public String propertyName;
	public String[] selection;
	public String searchString;
	public String successReturnUrl;
	public Integer firstResult;
	public Integer maxResults;
	public String sortProperty;
	public String sortDirection;
	public boolean searchVisible;

	protected String popupCloseCallback;

	public SearchForm searchForm;
	public TableForm tableForm;
	public Form form;

	protected SelectionProviderSupport selectionProviderSupport;

	protected String relName;
	protected int selectionProviderIndex;
	protected String selectFieldMode;
	protected String labelSearch;

	public ClassAccessor classAccessor;
	public PkHelper pkHelper;

	public T object;
	public List<? extends T> objects;

	@Inject(BaseModule.DEFAULT_BLOB_MANAGER)
	protected BlobManager blobManager;

	@Inject(BaseModule.TEMPORARY_BLOB_MANAGER)
	protected BlobManager temporaryBlobManager;

	public CrudConfiguration crudConfiguration;
	public Form crudConfigurationForm;
	public TableForm propertiesTableForm;
	public CrudPropertyEdit[] propertyEdits;
	public TableForm selectionProvidersForm;
	public CrudSelectionProviderEdit[] selectionProviderEdits;

	protected ResultSetNavigation resultSetNavigation;

	public abstract void loadObjects();

	protected abstract T loadObjectByPrimaryKey(Serializable pkObject);

	protected abstract void doSave(T object);

	protected abstract void doUpdate(T object);

	protected abstract void doDelete(T object);

	@DefaultHandler
	public Resolution execute() {
		if (object == null) {
			return doSearch();
		} else {
			return read();
		}
	}

	protected void loadObject(String... identifier) {
		Serializable pkObject = pkHelper.getPrimaryKey(identifier);
		object = loadObjectByPrimaryKey(pkObject);
	}

	@Buttons({ @Button(list = "crud-search-form", key = "search", order = 1, type = Button.TYPE_PRIMARY), @Button(list = "crud-search-form-default-button", key = "search") })
	public Resolution search() {
		searchVisible = true;
		searchString = null;
		sortProperty = null;
		sortDirection = null;
		firstResult = null;
		maxResults = null;
		return doSearch();
	}

	protected Resolution doSearch() {
		if (!isConfigured()) {
			logger.debug("Crud not correctly configured");
			return forwardToPageActionNotConfigured();
		}

		try {
			executeSearch();
			if (PageActionLogic.isEmbedded(this)) {
				return getEmbeddedSearchView();
			} else {
				returnUrl = new UrlBuilder(context.getLocale(), Util.getAbsoluteUrl(context.getActionPath()), false).toString();
				returnUrl = appendSearchStringParamIfNecessary(returnUrl);
				return getSearchView();
			}
		} catch (Exception e) {
			logger.warn("Crud not correctly configured", e);
			return forwardToPageActionNotConfigured();
		}
	}

	public Resolution getSearchResultsPage() {
		if (!isConfigured()) {
			logger.debug("Crud not correctly configured");
			return new ErrorResolution(500, "Crud not correctly configured");
		}

		try {
			executeSearch();
			context.getRequest().setAttribute("actionBean", this);
			return getSearchResultsPageView();
		} catch (Exception e) {
			logger.warn("Crud not correctly configured", e);
			return new ErrorResolution(500, "Crud not correctly configured");
		}
	}

	protected void executeSearch() {
		setupSearchForm();
		if (maxResults == null) {
			// Load only the first page if the crud is paginated
			maxResults = getCrudConfiguration().getRowsPerPage();
		}
		loadObjects();
		setupTableForm(Mode.VIEW);
		BlobUtils.loadBlobs(tableForm, getBlobManager(), false);
	}

	public Resolution jsonSearchData() throws JSONException {
		setupSearchForm();
		loadObjects();

		long totalRecords = getTotalSearchRecords();

		setupTableForm(Mode.VIEW);
		BlobUtils.loadBlobs(tableForm, getBlobManager(), false);
		JSONStringer js = new JSONStringer();
		js.object().key("recordsReturned").value(objects.size()).key("totalRecords").value(totalRecords).key("startIndex").value(firstResult == null ? 0 : firstResult).key("Result").array();
		for (TableForm.Row row : tableForm.getRows()) {
			js.object().key("__rowKey").value(row.getKey());
			fieldsToJson(js, row);
			js.endObject();
		}
		js.endArray();
		js.endObject();
		String jsonText = js.toString();
		return new StreamingResolution(MimeTypes.APPLICATION_JSON_UTF8, jsonText);
	}

	public abstract long getTotalSearchRecords();

	@Button(list = "crud-search-form", key = "reset.search", order = 2)
	public Resolution resetSearch() {
		return new RedirectResolution(context.getActionPath()).addParameter("searchVisible", true);
	}

	public Resolution read() {
		if (!crudConfiguration.isLargeResultSet()) {
			setupSearchForm(); // serve per la navigazione del result set
			loadObjects();
			setupPagination();
		}

		setupForm(Mode.VIEW);
		form.readFromObject(object);
		BlobUtils.loadBlobs(form, getBlobManager(), false);
		refreshBlobDownloadHref();

		returnUrl = new UrlBuilder(Locale.getDefault(), Util.getAbsoluteUrl(context.getActionPath()), false).toString();

		returnUrl = appendSearchStringParamIfNecessary(returnUrl);

		if (PageActionLogic.isEmbedded(this)) {
			return getEmbeddedReadView();
		} else {
			return getReadView();
		}
	}

	public Resolution jsonReadData() throws JSONException {
		if (object == null) {
			throw new IllegalStateException("Object not loaded. Are you including the primary key in the URL?");
		}

		setupForm(Mode.VIEW);
		form.readFromObject(object);
		BlobUtils.loadBlobs(form, getBlobManager(), false);
		refreshBlobDownloadHref();
		JSONStringer js = new JSONStringer();
		js.object();
		List<Field> fields = new ArrayList<Field>();
		collectVisibleFields(form, fields);
		fieldsToJson(js, fields);
		js.endObject();
		String jsonText = js.toString();
		return new StreamingResolution(MimeTypes.APPLICATION_JSON_UTF8, jsonText);
	}

	protected void writeFormToObject() {
		form.writeToObject(object);
		for (TextField textField : getEditableRichTextFields()) {
			PropertyAccessor propertyAccessor = textField.getPropertyAccessor();
			String stringValue = textField.getStringValue();
			String cleanText;
			try {
				Whitelist whitelist = getWhitelist();
				cleanText = Jsoup.clean(stringValue, whitelist);
			} catch (Throwable t) {
				logger.error("Could not clean HTML, falling back to escaped text", t);
				cleanText = StringEscapeUtils.escapeHtml(stringValue);
			}
			propertyAccessor.set(object, cleanText);
		}
	}

	protected Whitelist getWhitelist() {
		return Whitelist.basic();
	}

	@Button(list = "crud-search", key = "create.new", order = 1, type = Button.TYPE_SUCCESS, icon = Button.ICON_PLUS + Button.ICON_WHITE, group = "crud")
	@RequiresPermissions(permissions = PERMISSION_CREATE)
	public Resolution create() {
		setupForm(Mode.CREATE);
		object = (T) classAccessor.newInstance();
		createSetup(object);
		form.readFromObject(object);

		return getCreateView();
	}

	@Button(list = "crud-create", key = "save", order = 1, type = Button.TYPE_PRIMARY)
	@RequiresPermissions(permissions = PERMISSION_CREATE)
	public Resolution save() {
		setupForm(Mode.CREATE);
		object = (T) classAccessor.newInstance();
		createSetup(object);
		form.readFromObject(object);
		form.readFromRequest(context.getRequest());
		BlobUtils.loadBlobs(form, getTemporaryBlobManager(), false);
		if (form.validate()) {
			writeFormToObject();
			if (createValidate(object)) {
				try {
					doSave(object);
					createPostProcess(object);
					commitTransaction();
				} catch (Throwable e) {
					String rootCauseMessage = ExceptionUtils.getRootCauseMessage(e);
					logger.warn(rootCauseMessage, e);
					SessionMessages.addErrorMessage(rootCauseMessage);
					saveTemporaryBlobs();
					return getCreateView();
				}
				// The object on the database was persisted. Now we can save the
				// blobs.
				try {
					BlobUtils.loadBlobs(form, getTemporaryBlobManager(), true);
					BlobUtils.saveBlobs(form, getBlobManager());
				} catch (IOException e) {
					String rootCauseMessage = ExceptionUtils.getRootCauseMessage(e);
					logger.error("Could not persist blobs!", e);
					SessionMessages.addErrorMessage(rootCauseMessage);
				}
				if (isPopup()) {
					popupCloseCallback += "(true)";
					return new ForwardResolution("/m/crud/popup/close.jsp");
				} else {
					addSuccessfulSaveInfoMessage();
					return getSuccessfulSaveView();
				}
			}
		} else {
			saveTemporaryBlobs();
		}
		return getCreateView();
	}

	protected void saveTemporaryBlobs() {
		try {
			BlobUtils.saveBlobs(form, getTemporaryBlobManager());
		} catch (IOException e1) {
			logger.warn("Could not save temporary blobs", e1);
		}
	}

	@Buttons({ @Button(list = "crud-read", key = "edit", order = 1, icon = Button.ICON_EDIT + Button.ICON_WHITE, group = "crud", type = Button.TYPE_SUCCESS), @Button(list = "crud-read-default-button", key = "search") })
	@RequiresPermissions(permissions = PERMISSION_EDIT)
	public Resolution edit() {
		setupForm(Mode.EDIT);
		editSetup(object);
		form.readFromObject(object);
		BlobUtils.loadBlobs(form, getBlobManager(), false);
		return getEditView();
	}

	@Button(list = "crud-edit", key = "update", order = 1, type = Button.TYPE_PRIMARY)
	@RequiresPermissions(permissions = PERMISSION_EDIT)
	public Resolution update() {
		setupForm(Mode.EDIT);
		editSetup(object);
		form.readFromObject(object);
		List<Blob> blobsBefore = getBlobsFromForm();
		form.readFromRequest(context.getRequest());
		BlobUtils.loadBlobs(form, getBlobManager(), false);
		BlobUtils.loadBlobs(form, getTemporaryBlobManager(), false);
		if (form.validate()) {
			writeFormToObject();
			if (editValidate(object)) {
				try {
					doUpdate(object);
					editPostProcess(object);
					commitTransaction();
				} catch (Throwable e) {
					String rootCauseMessage = ExceptionUtils.getRootCauseMessage(e);
					logger.warn(rootCauseMessage, e);
					SessionMessages.addErrorMessage(rootCauseMessage);
					saveTemporaryBlobs();
					return getEditView();
				}
				try {
					List<Blob> blobsAfter = getBlobsFromForm();
					deleteOldBlobs(blobsBefore, blobsAfter);
					BlobUtils.loadBlobs(form, getTemporaryBlobManager(), true);
					persistNewBlobs(blobsBefore, blobsAfter);
				} catch (IOException e) {
					String rootCauseMessage = ExceptionUtils.getRootCauseMessage(e);
					logger.error("Could not persist blobs!", e);
					SessionMessages.addErrorMessage(rootCauseMessage);
				}

				SessionMessages.addInfoMessage(ElementsThreadLocals.getText("object.updated.successfully"));
				return getSuccessfulUpdateView();
			}
		} else {
			saveTemporaryBlobs();
		}
		return getEditView();
	}

	protected void persistNewBlobs(List<Blob> blobsBefore, List<Blob> blobsAfter) throws IOException {
		for (FileBlobField field : getBlobFields()) {
			Blob blob = field.getValue();
			if (blobsAfter.contains(blob) && !blobsBefore.contains(blob)) {
				getBlobManager().save(blob);
			}
		}
	}

	protected void deleteOldBlobs(List<Blob> blobsBefore, List<Blob> blobsAfter) {
		List<Blob> toDelete = new ArrayList<Blob>(blobsBefore);
		toDelete.removeAll(blobsAfter);
		for (Blob blob : toDelete) {
			try {
				getBlobManager().delete(blob);
			} catch (IOException e) {
				logger.warn("Could not delete blob: " + blob.getCode(), e);
			}
		}
	}

	public boolean isBulkOperationsEnabled() {
		return (objects != null && !objects.isEmpty()) || "bulkEdit".equals(context.getEventName()) || "bulkDelete".equals(context.getEventName());
	}

	@Button(list = "crud-search", key = "edit", order = 2, icon = Button.ICON_EDIT, group = "crud")
	@Guard(test = "isBulkOperationsEnabled()", type = GuardType.VISIBLE)
	@RequiresPermissions(permissions = PERMISSION_EDIT)
	public Resolution bulkEdit() {
		if (selection == null || selection.length == 0) {
			SessionMessages.addWarningMessage(ElementsThreadLocals.getText("no.object.was.selected"));
			return new RedirectResolution(returnUrl, false);
		}

		if (selection.length == 1) {
			pk = selection[0].split("/");
			String url = context.getActionPath() + "/" + getPkForUrl(pk);
			url = appendSearchStringParamIfNecessary(url);
			return new RedirectResolution(url).addParameter("returnUrl", returnUrl).addParameter("edit");
		}

		setupForm(Mode.BULK_EDIT);
		disableBlobFields();
		return getBulkEditView();
	}

	@Button(list = "crud-bulk-edit", key = "update", order = 1, type = Button.TYPE_PRIMARY)
	@RequiresPermissions(permissions = PERMISSION_EDIT)
	public Resolution bulkUpdate() {
		int updated = 0;
		setupForm(Mode.BULK_EDIT);
		disableBlobFields();
		form.readFromRequest(context.getRequest());
		if (form.validate()) {
			for (String current : selection) {
				loadObject(current.split("/"));
				editSetup(object);
				writeFormToObject();
				if (editValidate(object)) {
					doUpdate(object);
					editPostProcess(object);
					updated++;
				}
			}
			try {
				commitTransaction();
			} catch (Throwable e) {
				String rootCauseMessage = ExceptionUtils.getRootCauseMessage(e);
				logger.warn(rootCauseMessage, e);
				SessionMessages.addErrorMessage(rootCauseMessage);
				return getBulkEditView();
			}
			SessionMessages.addInfoMessage(ElementsThreadLocals.getText("update.of._.objects.successful", updated));
			return getSuccessfulUpdateView();
		} else {
			return getBulkEditView();
		}
	}

	@Button(list = "crud-read", key = "delete", order = 2, icon = Button.ICON_TRASH, group = "crud")
	@RequiresPermissions(permissions = PERMISSION_DELETE)
	public Resolution delete() {
		if (deleteValidate(object)) {
			doDelete(object);
			try {
				deletePostProcess(object);
				commitTransaction();
				deleteBlobs(object);
				SessionMessages.addInfoMessage(ElementsThreadLocals.getText("object.deleted.successfully"));

				// invalidate the pk on this crud
				pk = null;
			} catch (Exception e) {
				String rootCauseMessage = ExceptionUtils.getRootCauseMessage(e);
				logger.debug(rootCauseMessage, e);
				SessionMessages.addErrorMessage(rootCauseMessage);
			}
		}
		return getSuccessfulDeleteView();
	}

	@Button(list = "crud-search", key = "delete", order = 3, icon = Button.ICON_TRASH, group = "crud")
	@Guard(test = "isBulkOperationsEnabled()", type = GuardType.VISIBLE)
	@RequiresPermissions(permissions = PERMISSION_DELETE)
	public Resolution bulkDelete() {
		int deleted = 0;
		if (selection == null || selection.length == 0) {
			SessionMessages.addWarningMessage(ElementsThreadLocals.getText("no.object.was.selected"));
			return new RedirectResolution(appendSearchStringParamIfNecessary(context.getActionPath())); // TODO
																										// why
																										// is
																										// this
																										// different
																										// from
																										// bulkEdit?
		}
		List<T> objects = new ArrayList<T>(selection.length);
		for (String current : selection) {
			String[] pkArr = current.split("/");
			Serializable pkObject = pkHelper.getPrimaryKey(pkArr);
			T obj = loadObjectByPrimaryKey(pkObject);
			if (deleteValidate(obj)) {
				doDelete(obj);
				deletePostProcess(obj);
				objects.add(obj);
				deleted++;
			}
		}
		try {
			commitTransaction();
			for (T obj : objects) {
				deleteBlobs(obj);
			}
			SessionMessages.addInfoMessage(ElementsThreadLocals.getText("_.objects.deleted.successfully", deleted));
		} catch (Exception e) {
			logger.warn(ExceptionUtils.getRootCauseMessage(e), e);
			SessionMessages.addErrorMessage(ExceptionUtils.getRootCauseMessage(e));
		}

		return getSuccessfulDeleteView();
	}

	protected void createSetup(T object) {
	}

	protected boolean createValidate(T object) {
		return true;
	}

	protected void createPostProcess(T object) {
	}

	protected void commitTransaction() {
	}

	protected void editSetup(T object) {
	}

	protected boolean editValidate(T object) {
		return true;
	}

	protected void editPostProcess(T object) {
	}

	protected boolean deleteValidate(T object) {
		return true;
	}

	protected void deletePostProcess(T object) {
	}

	protected Resolution getBulkEditView() {
		return new ForwardResolution("/m/crud/bulk-edit.jsp");
	}

	protected Resolution getCreateView() { // TODO spezzare in popup/non-popup?
		if (isPopup()) {
			return new ForwardResolution("/m/crud/popup/create.jsp");
		} else {
			return new ForwardResolution("/m/crud/create.jsp");
		}
	}

	protected Resolution getSuccessfulSaveView() {
		if (StringUtils.isEmpty(returnUrl)) {
			return new RedirectResolution(context.getActionPath());
		} else {
			return new RedirectResolution(returnUrl, false);
		}
	}

	protected Resolution getSuccessfulUpdateView() {
		return new RedirectResolution(appendSearchStringParamIfNecessary(context.getActionPath()));
	}

	protected Resolution getSuccessfulDeleteView() {
		return new RedirectResolution(appendSearchStringParamIfNecessary(context.getActionPath()));
	}

	protected void addSuccessfulSaveInfoMessage() {
		XhtmlBuffer buffer = new XhtmlBuffer();

		pk = pkHelper.generatePkStringArray(object);
		String readUrl = context.getActionPath() + "/" + getPkForUrl(pk);
		String prettyName = ShortNameUtils.getName(getClassAccessor(), object);
		XhtmlBuffer linkToObjectBuffer = new XhtmlBuffer();
		linkToObjectBuffer.writeAnchor(Util.getAbsoluteUrl(readUrl), prettyName);
		buffer.writeNoHtmlEscape(ElementsThreadLocals.getText("object._.saved", linkToObjectBuffer));

		String createUrl = Util.getAbsoluteUrl(context.getActionPath());
		if (!createUrl.contains("?")) {
			createUrl += "?";
		} else {
			createUrl += "&";
		}
		createUrl += "create=";
		createUrl = appendSearchStringParamIfNecessary(createUrl);
		buffer.write(" ");
		buffer.writeAnchor(createUrl, ElementsThreadLocals.getText("create.another.object"));

		SessionMessages.addInfoMessage(buffer);
	}

	protected Resolution getEditView() {
		return new ForwardResolution("/m/crud/edit.jsp");
	}

	protected Resolution getReadView() {
		return forwardTo("/m/crud/read.jsp");
	}

	protected Resolution getEmbeddedReadView() {
		return new ForwardResolution("/m/crud/read.jsp");
	}

	protected Resolution getSearchView() {
		return forwardTo("/m/crud/search.jsp");
	}

	protected Resolution getEmbeddedSearchView() {
		return new ForwardResolution("/m/crud/search.jsp");
	}

	protected Resolution getSearchResultsPageView() {
		return new ForwardResolution("/m/crud/datatable.jsp");
	}

	public Resolution preparePage() {
		this.crudConfiguration = (CrudConfiguration) pageInstance.getConfiguration();

		if (crudConfiguration == null) {
			logger.warn("Crud is not configured: " + pageInstance.getPath());
			return null;
		}

		ClassAccessor innerAccessor = prepare(pageInstance);
		if (innerAccessor == null) {
			return null;
		}
		classAccessor = new CrudAccessor(crudConfiguration, innerAccessor);
		pkHelper = new PkHelper(classAccessor);

		List<String> parameters = pageInstance.getParameters();
		if (!parameters.isEmpty()) {
			String encoding = getUrlEncoding();
			pk = parameters.toArray(new String[parameters.size()]);
			try {
				for (int i = 0; i < pk.length; i++) {
					pk[i] = URLDecoder.decode(pk[i], encoding);
				}
			} catch (UnsupportedEncodingException e) {
				throw new Error(e);
			}
			OgnlContext ognlContext = ElementsThreadLocals.getOgnlContext();

			Serializable pkObject;
			try {
				pkObject = pkHelper.getPrimaryKey(pk);
			} catch (Exception e) {
				logger.warn("Invalid primary key", e);
				return notInUseCase(context, parameters);
			}
			object = loadObjectByPrimaryKey(pkObject);
			if (object != null) {
				ognlContext.put(crudConfiguration.getActualVariable(), object);
				String title = getReadTitle();
				pageInstance.setTitle(title);
				pageInstance.setDescription(title);
			} else {
				return notInUseCase(context, parameters);
			}
		} else {
			String title = crudConfiguration.getSearchTitle();
			pageInstance.setTitle(title);
			pageInstance.setDescription(title);
		}
		return null;
	}

	protected Resolution notInUseCase(ActionBeanContext context, List<String> parameters) {
		logger.info("Not in use case: " + crudConfiguration.getName());
		String msg = ElementsThreadLocals.getText("object.not.found._", StringUtils.join(parameters, "/"));
		SessionMessages.addWarningMessage(msg);
		return new ForwardResolution("/m/pageactions/redirect-to-last-working-page.jsp");
	}

	protected abstract ClassAccessor prepare(PageInstance pageInstance);

	public boolean isConfigured() {
		return (classAccessor != null);
	}

	protected void setupPagination() {
		resultSetNavigation = new ResultSetNavigation();
		int position = objects.indexOf(object);
		int size = objects.size();
		resultSetNavigation.setPosition(position);
		resultSetNavigation.setSize(size);
		String baseUrl = calculateBaseSearchUrl();
		if (position >= 0) {
			if (position > 0) {
				resultSetNavigation.setFirstUrl(generateObjectUrl(baseUrl, 0));
				resultSetNavigation.setPreviousUrl(generateObjectUrl(baseUrl, position - 1));
			}
			if (position < size - 1) {
				resultSetNavigation.setLastUrl(generateObjectUrl(baseUrl, size - 1));
				resultSetNavigation.setNextUrl(generateObjectUrl(baseUrl, position + 1));
			}
		}
	}

	protected String calculateBaseSearchUrl() {
		assert pk != null; // Ha senso solo in modalita' read/detail
		String baseUrl = Util.getAbsoluteUrl(context.getActionPath());
		for (int i = 0; i < pk.length; i++) {
			int lastSlashIndex = baseUrl.lastIndexOf('/');
			baseUrl = baseUrl.substring(0, lastSlashIndex);
		}
		return baseUrl;
	}

	protected String generateObjectUrl(String baseUrl, int index) {
		Object o = objects.get(index);
		return generateObjectUrl(baseUrl, o);
	}

	protected String generateObjectUrl(String baseUrl, Object o) {
		String[] objPk = pkHelper.generatePkStringArray(o);
		String url = baseUrl + "/" + getPkForUrl(objPk);
		return new UrlBuilder(Locale.getDefault(), appendSearchStringParamIfNecessary(url), false).toString();
	}

	protected void setupSearchForm() {
		SearchFormBuilder searchFormBuilder = createSearchFormBuilder();
		searchForm = buildSearchForm(configureSearchFormBuilder(searchFormBuilder));

		if (!PageActionLogic.isEmbedded(this)) {
			logger.debug("Search form not embedded, no risk of clashes - reading parameters from request");
			readSearchFormFromRequest();
		}
	}

	protected void readSearchFormFromRequest() {
		if (StringUtils.isBlank(searchString)) {
			searchForm.readFromRequest(context.getRequest());
			searchString = searchForm.toSearchString(getUrlEncoding());
			if (searchString.length() == 0) {
				searchString = null;
			} else {
				searchVisible = true;
			}
		} else {
			MutableHttpServletRequest dummyRequest = new MutableHttpServletRequest();
			String[] parts = searchString.split(",");
			Pattern pattern = Pattern.compile("(.*)=(.*)");
			for (String part : parts) {
				Matcher matcher = pattern.matcher(part);
				if (matcher.matches()) {
					String key = matcher.group(1);
					String value = matcher.group(2);
					logger.debug("Matched part: {}={}", key, value);
					dummyRequest.addParameter(key, value);
				} else {
					logger.debug("Could not match part: {}", part);
				}
			}
			searchForm.readFromRequest(dummyRequest);
			searchVisible = true;
		}
	}

	protected SearchFormBuilder createSearchFormBuilder() {
		return new SearchFormBuilder(classAccessor);
	}

	protected SearchFormBuilder configureSearchFormBuilder(SearchFormBuilder searchFormBuilder) {
		// setup option providers
		for (CrudSelectionProvider current : selectionProviderSupport.getCrudSelectionProviders()) {
			SelectionProvider selectionProvider = current.getSelectionProvider();
			if (selectionProvider == null) {
				continue;
			}
			String[] fieldNames = current.getFieldNames();
			searchFormBuilder.configSelectionProvider(selectionProvider, fieldNames);
		}
		return searchFormBuilder.configPrefix(searchPrefix);
	}

	protected SearchForm buildSearchForm(SearchFormBuilder searchFormBuilder) {
		return searchFormBuilder.build();
	}

	protected void setupTableForm(Mode mode) {
		int nRows;
		if (objects == null) {
			nRows = 0;
		} else {
			nRows = objects.size();
		}
		TableFormBuilder tableFormBuilder = createTableFormBuilder();
		configureTableFormBuilder(tableFormBuilder, mode, nRows);
		tableForm = buildTableForm(tableFormBuilder);

		if (objects != null) {
			tableForm.readFromObject(objects);
			refreshTableBlobDownloadHref();
		}
	}

	protected void configureTableFormSelectionProviders(TableFormBuilder tableFormBuilder) {
		// setup option providers
		for (CrudSelectionProvider current : selectionProviderSupport.getCrudSelectionProviders()) {
			SelectionProvider selectionProvider = current.getSelectionProvider();
			if (selectionProvider == null) {
				continue;
			}
			String[] fieldNames = current.getFieldNames();
			tableFormBuilder.configSelectionProvider(selectionProvider, fieldNames);
		}
	}

	protected void configureDetailLink(TableFormBuilder tableFormBuilder) {
		boolean isShowingKey = false;
		for (PropertyAccessor property : classAccessor.getKeyProperties()) {
			if (tableFormBuilder.getPropertyAccessors().contains(property) && tableFormBuilder.isPropertyVisible(property)) {
				isShowingKey = true;
				break;
			}
		}

		String readLinkExpression = getReadLinkExpression();
		OgnlTextFormat hrefFormat = OgnlTextFormat.create(readLinkExpression);
		hrefFormat.setUrl(true);
		String encoding = getUrlEncoding();
		hrefFormat.setEncoding(encoding);

		if (isShowingKey) {
			logger.debug("TableForm: configuring detail links for primary key properties");
			for (PropertyAccessor property : classAccessor.getKeyProperties()) {
				tableFormBuilder.configHrefTextFormat(property.getName(), hrefFormat);
			}
		} else {
			logger.debug("TableForm: configuring detail link for the first visible property");
			for (PropertyAccessor property : classAccessor.getProperties()) {
				if (tableFormBuilder.getPropertyAccessors().contains(property) && tableFormBuilder.isPropertyVisible(property)) {
					tableFormBuilder.configHrefTextFormat(property.getName(), hrefFormat);
					break;
				}
			}
		}
	}

	protected void configureSortLinks(TableFormBuilder tableFormBuilder) {
		for (PropertyAccessor propertyAccessor : classAccessor.getProperties()) {
			String propName = propertyAccessor.getName();
			String sortDirection;
			if (propName.equals(sortProperty) && "asc".equals(this.sortDirection)) {
				sortDirection = "desc";
			} else {
				sortDirection = "asc";
			}

			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("sortProperty", propName);
			parameters.put("sortDirection", sortDirection);
			if (!PageActionLogic.isEmbedded(this)) {
				parameters.put(SEARCH_STRING_PARAM, searchString);
			}
			parameters.put(context.getEventName(), "");

			UrlBuilder urlBuilder = new UrlBuilder(Locale.getDefault(), Util.getAbsoluteUrl(context.getActionPath()), false).addParameters(parameters);

			XhtmlBuffer xb = new XhtmlBuffer();
			xb.openElement("a");
			xb.addAttribute("class", "sort-link");
			xb.addAttribute("href", urlBuilder.toString());
			xb.writeNoHtmlEscape("%{label}");
			if (propName.equals(sortProperty)) {
				xb.openElement("i");
				xb.addAttribute("class", "pull-right glyphicon glyphicon-chevron-" + ("desc".equals(sortDirection) ? "up" : "down"));
				xb.closeElement("i");
			}
			xb.closeElement("a");
			OgnlTextFormat hrefFormat = OgnlTextFormat.create(xb.toString());
			String encoding = getUrlEncoding();
			hrefFormat.setEncoding(encoding);
			tableFormBuilder.configHeaderTextFormat(propName, hrefFormat);
		}
	}

	public String getLinkToPage(int page) {
		int rowsPerPage = getCrudConfiguration().getRowsPerPage();
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("sortProperty", getSortProperty());
		parameters.put("sortDirection", getSortDirection());
		parameters.put("firstResult", page * rowsPerPage);
		parameters.put("maxResults", rowsPerPage);
		if (!PageActionLogic.isEmbedded(this)) {
			parameters.put(AbstractCrudAction.SEARCH_STRING_PARAM, getSearchString());
		}

		UrlBuilder urlBuilder = new UrlBuilder(Locale.getDefault(), Util.getAbsoluteUrl(context.getActionPath()), false).addParameters(parameters);
		return urlBuilder.toString();
	}

	protected TableForm buildTableForm(TableFormBuilder tableFormBuilder) {
		TableForm tableForm = tableFormBuilder.build();

		tableForm.setKeyGenerator(pkHelper.createPkGenerator());
		tableForm.setSelectable(true);
		tableForm.setCondensed(true);

		return tableForm;
	}

	protected TableFormBuilder createTableFormBuilder() {
		return new TableFormBuilder(classAccessor);
	}

	protected TableFormBuilder configureTableFormBuilder(TableFormBuilder tableFormBuilder, Mode mode, int nRows) {
		configureTableFormSelectionProviders(tableFormBuilder);
		tableFormBuilder.configPrefix(prefix).configNRows(nRows).configMode(mode);
		if (tableFormBuilder.getPropertyAccessors() == null) {
			tableFormBuilder.configReflectiveFields();
		}

		configureDetailLink(tableFormBuilder);
		configureSortLinks(tableFormBuilder);

		return tableFormBuilder;
	}

	protected void setupForm(Mode mode) {
		FormBuilder formBuilder = createFormBuilder();
		configureFormBuilder(formBuilder, mode);
		form = buildForm(formBuilder);
	}

	protected void configureFormSelectionProviders(FormBuilder formBuilder) {
		// setup option providers
		for (CrudSelectionProvider current : selectionProviderSupport.getCrudSelectionProviders()) {
			SelectionProvider selectionProvider = current.getSelectionProvider();
			if (selectionProvider == null) {
				continue;
			}
			String[] fieldNames = current.getFieldNames();
			if (object != null) {
				Object[] values = new Object[fieldNames.length];
				boolean valuesRead = true;
				for (int i = 0; i < fieldNames.length; i++) {
					String fieldName = fieldNames[i];
					try {
						PropertyAccessor propertyAccessor = classAccessor.getProperty(fieldName);
						values[i] = propertyAccessor.get(object);
					} catch (Exception e) {
						logger.error("Couldn't read property " + fieldName, e);
						valuesRead = false;
					}
				}
				if (valuesRead) {
					selectionProvider.ensureActive(values);
				}
			}
			formBuilder.configSelectionProvider(selectionProvider, fieldNames);
		}
	}

	protected Form buildForm(FormBuilder formBuilder) {
		return formBuilder.build();
	}

	protected void disableBlobFields() {
		// Disable blob fields: we don't support them.
		for (FieldSet fieldSet : form) {
			for (FormElement element : fieldSet) {
				if (element instanceof FileBlobField) {
					((FileBlobField) element).setInsertable(false);
					((FileBlobField) element).setUpdatable(false);
				}
			}
		}
	}

	protected FormBuilder createFormBuilder() {
		return new FormBuilder(classAccessor);
	}

	protected FormBuilder configureFormBuilder(FormBuilder formBuilder, Mode mode) {
		formBuilder.configPrefix(prefix).configMode(mode);
		configureFormSelectionProviders(formBuilder);
		return formBuilder;
	}

	public Resolution returnToSearch() throws Exception {
		if (pk != null) {
			return new RedirectResolution(appendSearchStringParamIfNecessary(calculateBaseSearchUrl()), false);
		} else {
			return new ErrorResolution(500);
		}
	}

	@Override
	@Buttons({ @Button(list = "crud-edit", key = "cancel", order = 99), @Button(list = "crud-create", key = "cancel", order = 99), @Button(list = "crud-bulk-edit", key = "cancel", order = 99), @Button(list = "configuration", key = "cancel", order = 99) })
	public Resolution cancel() {
		if (isPopup()) {
			popupCloseCallback += "(false)";
			return new ForwardResolution("/m/crud/popup/close.jsp");
		} else {
			return super.cancel();
		}
	}

	protected void refreshBlobDownloadHref() {
		for (FieldSet fieldSet : form) {
			for (Field field : fieldSet.fields()) {
				if (field instanceof FileBlobField) {
					FileBlobField fileBlobField = (FileBlobField) field;
					Blob blob = fileBlobField.getValue();
					if (blob != null) {
						String url = getBlobDownloadUrl(fileBlobField);
						field.setHref(url);
					}
				}
			}
		}
	}

	protected void refreshTableBlobDownloadHref() {
		Iterator<?> objIterator = objects.iterator();
		for (TableForm.Row row : tableForm.getRows()) {
			Iterator<Field> fieldIterator = row.iterator();
			Object obj = objIterator.next();
			String baseUrl = null;
			while (fieldIterator.hasNext()) {
				Field field = fieldIterator.next();
				if (field instanceof FileBlobField) {
					if (baseUrl == null) {
						String readLinkExpression = getReadLinkExpression();
						String encoding = getUrlEncoding();
						OgnlTextFormat hrefFormat = OgnlTextFormat.create(readLinkExpression);
						hrefFormat.setUrl(true);
						hrefFormat.setEncoding(encoding);
						baseUrl = hrefFormat.format(obj);
					}

					Blob blob = ((FileBlobField) field).getValue();
					if (blob != null) {
						UrlBuilder urlBuilder = new UrlBuilder(Locale.getDefault(), baseUrl, false).addParameter("downloadBlob", "").addParameter("propertyName", field.getPropertyAccessor().getName()).addParameter("code", blob.getCode());
						// although unused, the code parameter makes the url
						// change if the
						// blob changes. In this way we can ask the browser to
						// cache the url
						// indefinitely.

						field.setHref(urlBuilder.toString());
					}
				}
			}
		}
	}

	public String getBlobDownloadUrl(FileBlobField field) {
		UrlBuilder urlBuilder = new UrlBuilder(Locale.getDefault(), Util.getAbsoluteUrl(context.getActionPath()), false).addParameter("downloadBlob", "").addParameter("propertyName", field.getPropertyAccessor().getName()).addParameter("code", field.getValue().getCode());
		// The code parameter must be kept. See not in
		// refreshTableBlobDownloadHref
		return urlBuilder.toString();
	}

	public Resolution downloadBlob() throws IOException, NoSuchFieldException {
		PropertyAccessor propertyAccessor = classAccessor.getProperty(propertyName);
		String code = (String) propertyAccessor.get(object);
		if (StringUtils.isBlank(code)) {
			return new ErrorResolution(404, "No blob was found");
		}
		BlobManager blobManager = getBlobManager();
		Blob blob = new Blob(code);
		blobManager.loadMetadata(blob);
		long contentLength = blob.getSize();
		String contentType = blob.getContentType();
		String fileName = blob.getFilename();
		long lastModified = blob.getCreateTimestamp().getMillis();
		InputStream inputStream = blobManager.openStream(blob);
		return new StreamingResolution(contentType, inputStream).setFilename(fileName).setLength(contentLength).setLastModified(lastModified);
	}

	protected BlobManager getBlobManager() {
		return blobManager;
	}

	public BlobManager getTemporaryBlobManager() {
		return temporaryBlobManager;
	}

	protected void deleteBlobs(T object) {
		List<Blob> blobs = getBlobsFromObject(object);
		for (Blob blob : blobs) {
			try {
				blobManager.delete(blob);
			} catch (IOException e) {
				logger.warn("Could not delete blob: " + blob.getCode(), e);
			}
		}
	}

	protected List<Blob> getBlobsFromObject(T object) {
		List<Blob> blobs = new ArrayList<Blob>();
		for (PropertyAccessor property : classAccessor.getProperties()) {
			if (property.getAnnotation(FileBlob.class) != null) {
				String code = (String) property.get(object);
				if (!StringUtils.isBlank(code)) {
					blobs.add(new Blob(code));
				}
			}
		}
		return blobs;
	}

	protected List<Blob> getBlobsFromForm() {
		List<Blob> blobs = new ArrayList<Blob>();
		for (FileBlobField blobField : getBlobFields()) {
			if (blobField.getValue() != null) {
				blobs.add(blobField.getValue());
			}
		}
		return blobs;
	}

	protected List<FileBlobField> getBlobFields() {
		List<FileBlobField> blobFields = new ArrayList<FileBlobField>();
		for (FieldSet fieldSet : form) {
			for (FormElement field : fieldSet) {
				if (field instanceof FileBlobField) {
					blobFields.add((FileBlobField) field);
				}
			}
		}
		return blobFields;
	}

	@Button(list = "pageHeaderButtons", titleKey = "configure", order = 1, icon = Button.ICON_WRENCH)
	@RequiresPermissions(level = AccessLevel.DEVELOP)
	public Resolution configure() {
		prepareConfigurationForms();

		crudConfigurationForm.readFromObject(crudConfiguration);
		if (propertyEdits != null) {
			propertiesTableForm.readFromObject(propertyEdits);
		}

		if (selectionProviderEdits != null) {
			selectionProvidersForm.readFromObject(selectionProviderEdits);
		}

		return getConfigurationView();
	}

	protected abstract Resolution getConfigurationView();

	@Override
	protected void prepareConfigurationForms() {
		super.prepareConfigurationForms();

		setupPropertyEdits();

		if (propertyEdits != null) {
			TableFormBuilder tableFormBuilder = new TableFormBuilder(CrudPropertyEdit.class).configNRows(propertyEdits.length);
			propertiesTableForm = tableFormBuilder.build();
			propertiesTableForm.setCondensed(true);
		}

		if (selectionProviderSupport != null) {
			Map<List<String>, Collection<String>> selectionProviderNames = selectionProviderSupport.getAvailableSelectionProviderNames();
			if (!selectionProviderNames.isEmpty()) {
				setupSelectionProviderEdits();
				setupSelectionProvidersForm(selectionProviderNames);
			}
		}
	}

	protected void setupSelectionProvidersForm(Map<List<String>, Collection<String>> selectionProviderNames) {
		TableFormBuilder tableFormBuilder = new TableFormBuilder(CrudSelectionProviderEdit.class);
		tableFormBuilder.configNRows(selectionProviderNames.size());
		for (int i = 0; i < selectionProviderEdits.length; i++) {
			Collection<String> availableProviders = selectionProviderNames.get(Arrays.asList(selectionProviderEdits[i].fieldNames));
			if (availableProviders == null || availableProviders.size() == 0) {
				continue;
			}
			DefaultSelectionProvider selectionProvider = new DefaultSelectionProvider(selectionProviderEdits[i].columns);
			selectionProvider.appendRow(null, "None", true);
			for (String spName : availableProviders) {
				selectionProvider.appendRow(spName, spName, true);
			}
			tableFormBuilder.configSelectionProvider(i, selectionProvider, "selectionProvider");
		}
		selectionProvidersForm = tableFormBuilder.build();
		selectionProvidersForm.setCondensed(true);
	}

	protected void setupPropertyEdits() {
		if (classAccessor == null) {
			return;
		}
		PropertyAccessor[] propertyAccessors = classAccessor.getProperties();
		propertyEdits = new CrudPropertyEdit[propertyAccessors.length];
		for (int i = 0; i < propertyAccessors.length; i++) {
			CrudPropertyEdit edit = new CrudPropertyEdit();
			PropertyAccessor propertyAccessor = propertyAccessors[i];
			edit.name = propertyAccessor.getName();
			com.manydesigns.elements.annotations.Label labelAnn = propertyAccessor.getAnnotation(com.manydesigns.elements.annotations.Label.class);
			edit.label = labelAnn != null ? labelAnn.value() : null;
			Enabled enabledAnn = propertyAccessor.getAnnotation(Enabled.class);
			edit.enabled = enabledAnn != null && enabledAnn.value();
			InSummary inSummaryAnn = propertyAccessor.getAnnotation(InSummary.class);
			edit.inSummary = inSummaryAnn != null && inSummaryAnn.value();
			Insertable insertableAnn = propertyAccessor.getAnnotation(Insertable.class);
			edit.insertable = insertableAnn != null && insertableAnn.value();
			Updatable updatableAnn = propertyAccessor.getAnnotation(Updatable.class);
			edit.updatable = updatableAnn != null && updatableAnn.value();
			Searchable searchableAnn = propertyAccessor.getAnnotation(Searchable.class);
			edit.searchable = searchableAnn != null && searchableAnn.value();
			propertyEdits[i] = edit;
		}
	}

	protected void setupSelectionProviderEdits() {
		Map<List<String>, Collection<String>> availableSelectionProviders = selectionProviderSupport.getAvailableSelectionProviderNames();
		selectionProviderEdits = new CrudSelectionProviderEdit[availableSelectionProviders.size()];
		int i = 0;
		for (List<String> key : availableSelectionProviders.keySet()) {
			selectionProviderEdits[i] = new CrudSelectionProviderEdit();
			String[] fieldNames = key.toArray(new String[key.size()]);
			selectionProviderEdits[i].fieldNames = fieldNames;
			selectionProviderEdits[i].columns = StringUtils.join(fieldNames, ", ");
			for (CrudSelectionProvider cp : selectionProviderSupport.getCrudSelectionProviders()) {
				if (Arrays.equals(cp.fieldNames, fieldNames)) {
					SelectionProvider selectionProvider = cp.getSelectionProvider();
					if (selectionProvider != null) {
						selectionProviderEdits[i].selectionProvider = selectionProvider.getName();
						selectionProviderEdits[i].displayMode = selectionProvider.getDisplayMode();
						selectionProviderEdits[i].searchDisplayMode = selectionProvider.getSearchDisplayMode();
						selectionProviderEdits[i].createNewHref = cp.getCreateNewValueHref();
						selectionProviderEdits[i].createNewText = cp.getCreateNewValueText();
					} else {
						selectionProviderEdits[i].selectionProvider = null;
						selectionProviderEdits[i].displayMode = DisplayMode.DROPDOWN;
						selectionProviderEdits[i].searchDisplayMode = SearchDisplayMode.DROPDOWN;
					}
				}
			}
			i++;
		}
	}

	@Button(list = "configuration", key = "update.configuration", order = 1, type = Button.TYPE_PRIMARY)
	@RequiresPermissions(level = AccessLevel.DEVELOP)
	public Resolution updateConfiguration() {
		prepareConfigurationForms();

		crudConfigurationForm.readFromObject(crudConfiguration);

		readPageConfigurationFromRequest();

		crudConfigurationForm.readFromRequest(context.getRequest());

		boolean valid = crudConfigurationForm.validate();
		valid = validatePageConfiguration() && valid;

		if (propertiesTableForm != null) {
			propertiesTableForm.readFromObject(propertyEdits);
			propertiesTableForm.readFromRequest(context.getRequest());
			valid = propertiesTableForm.validate() && valid;
		}

		if (selectionProvidersForm != null) {
			selectionProvidersForm.readFromRequest(context.getRequest());
			valid = selectionProvidersForm.validate() && valid;
		}

		if (valid) {
			updatePageConfiguration();
			if (crudConfiguration == null) {
				crudConfiguration = new CrudConfiguration();
			}
			crudConfigurationForm.writeToObject(crudConfiguration);

			if (propertiesTableForm != null) {
				updateProperties();
			}

			if (selectionProviderSupport != null && !selectionProviderSupport.getAvailableSelectionProviderNames().isEmpty()) {
				updateSelectionProviders();
			}

			saveConfiguration(crudConfiguration);

			SessionMessages.addInfoMessage(ElementsThreadLocals.getText("configuration.updated.successfully"));
			return cancel();
		} else {
			SessionMessages.addErrorMessage(ElementsThreadLocals.getText("the.configuration.could.not.be.saved"));
			return getConfigurationView();
		}
	}

	protected void updateSelectionProviders() {
		selectionProvidersForm.writeToObject(selectionProviderEdits);
		crudConfiguration.getSelectionProviders().clear();
		for (CrudSelectionProviderEdit sp : selectionProviderEdits) {
			List<String> key = Arrays.asList(sp.fieldNames);
			if (sp.selectionProvider == null) {
				selectionProviderSupport.disableSelectionProvider(key);
			} else {
				selectionProviderSupport.configureSelectionProvider(key, sp.selectionProvider, sp.displayMode, sp.searchDisplayMode, StringUtils.trimToNull(sp.createNewHref), sp.createNewText);
			}
		}
	}

	protected void updateProperties() {
		propertiesTableForm.writeToObject(propertyEdits);

		List<CrudProperty> newProperties = new ArrayList<CrudProperty>();
		for (CrudPropertyEdit edit : propertyEdits) {
			CrudProperty crudProperty = findProperty(edit.name, crudConfiguration.getProperties());
			if (crudProperty == null) {
				crudProperty = new CrudProperty();
			}

			crudProperty.setName(edit.name);
			crudProperty.setLabel(edit.label);
			crudProperty.setInSummary(edit.inSummary);
			crudProperty.setSearchable(edit.searchable);
			crudProperty.setEnabled(edit.enabled);
			crudProperty.setInsertable(edit.insertable);
			crudProperty.setUpdatable(edit.updatable);

			newProperties.add(crudProperty);
		}
		crudConfiguration.getProperties().clear();
		crudConfiguration.getProperties().addAll(newProperties);
	}

	public boolean isRequiredFieldsPresent() {
		return form.isRequiredFieldsPresent();
	}

	public Resolution jsonSelectFieldOptions() {
		return jsonOptions(prefix, true);
	}

	public Resolution jsonSelectFieldSearchOptions() {
		return jsonOptions(searchPrefix, true);
	}

	public Resolution jsonAutocompleteOptions() {
		return jsonOptions(prefix, false);
	}

	public Resolution jsonAutocompleteSearchOptions() {
		return jsonOptions(searchPrefix, false);
	}

	protected Resolution jsonOptions(String prefix, boolean includeSelectPrompt) {
		CrudSelectionProvider crudSelectionProvider = null;
		for (CrudSelectionProvider current : selectionProviderSupport.getCrudSelectionProviders()) {
			SelectionProvider selectionProvider = current.getSelectionProvider();
			if (selectionProvider.getName().equals(relName)) {
				crudSelectionProvider = current;
				break;
			}
		}
		if (crudSelectionProvider == null) {
			return new ErrorResolution(500);
		}

		SelectionProvider selectionProvider = crudSelectionProvider.getSelectionProvider();
		String[] fieldNames = crudSelectionProvider.getFieldNames();

		Form form = buildForm(createFormBuilder().configFields(fieldNames).configSelectionProvider(selectionProvider, fieldNames).configPrefix(prefix).configMode(Mode.EDIT));

		FieldSet fieldSet = form.get(0);
		// Ensure the value is actually read from the request
		for (Field field : fieldSet.fields()) {
			field.setUpdatable(true);
		}
		form.readFromRequest(context.getRequest());

		SelectField targetField = (SelectField) fieldSet.get(selectionProviderIndex);
		targetField.setLabelSearch(labelSearch);

		String text = targetField.jsonSelectFieldOptions(includeSelectPrompt);
		logger.debug("jsonOptions: {}", text);
		return new StreamingResolution(MimeTypes.APPLICATION_JSON_UTF8, text);
	}

	protected String getUrlEncoding() {
		return portofinoConfiguration.getString(PortofinoProperties.URL_ENCODING, PortofinoProperties.URL_ENCODING_DEFAULT);
	}

	protected CrudProperty findProperty(String name, List<CrudProperty> properties) {
		for (CrudProperty p : properties) {
			if (p.getName().equals(name)) {
				return p;
			}
		}
		return null;
	}

	protected String getPkForUrl(String[] pk) {
		String encoding = getUrlEncoding();
		try {
			return pkHelper.getPkStringForUrl(pk, encoding);
		} catch (UnsupportedEncodingException e) {
			throw new Error(e);
		}
	}

	protected String getReadLinkExpression() {
		String actionPath = context.getActionPath();
		StringBuilder sb = new StringBuilder(actionPath);
		if (!actionPath.endsWith("/")) {
			sb.append("/");
		}
		boolean first = true;

		for (PropertyAccessor property : classAccessor.getKeyProperties()) {
			if (first) {
				first = false;
			} else {
				sb.append("/");
			}
			sb.append("%{");
			sb.append(property.getName());
			sb.append("}");
		}
		appendSearchStringParamIfNecessary(sb);
		return sb.toString();
	}

	protected String appendSearchStringParamIfNecessary(String s) {
		return appendSearchStringParamIfNecessary(new StringBuilder(s)).toString();
	}

	protected StringBuilder appendSearchStringParamIfNecessary(StringBuilder sb) {
		String searchStringParam = getEncodedSearchStringParam();
		if (searchStringParam != null) {
			if (sb.indexOf("?") == -1) {
				sb.append('?');
			} else {
				sb.append('&');
			}
			sb.append(searchStringParam);
		}
		return sb;
	}

	protected String getEncodedSearchStringParam() {
		if (StringUtils.isBlank(searchString)) {
			return null;
		}
		String encodedSearchString = "searchString=";
		try {
			String encoding = getUrlEncoding();
			String encoded = URLEncoder.encode(searchString, encoding);
			if (searchString.equals(URLDecoder.decode(encoded, encoding))) {
				encodedSearchString += encoded;
			} else {
				logger.warn("Could not encode search string \"" + StringEscapeUtils.escapeJava(searchString) + "\" with encoding " + encoding);
				return null;
			}
		} catch (UnsupportedEncodingException e) {
			throw new Error(e);
		}
		return encodedSearchString;
	}

	protected void fieldsToJson(JSONStringer js, Collection<Field> fields) throws JSONException {
		for (Field field : fields) {
			Object value = field.getValue();
			String displayValue = field.getDisplayValue();
			String href = field.getHref();
			js.key(field.getPropertyAccessor().getName());
			js.object().key("value").value(value).key("displayValue").value(displayValue).key("href").value(href).endObject();
		}
	}

	protected List<Field> collectVisibleFields(Form form, List<Field> fields) {
		for (FieldSet fieldSet : form) {
			collectVisibleFields(fieldSet, fields);
		}
		return fields;
	}

	protected List<Field> collectVisibleFields(FieldSet fieldSet, List<Field> fields) {
		for (FormElement element : fieldSet) {
			if (element instanceof Field) {
				Field field = (Field) element;
				if (field.isEnabled()) {
					fields.add(field);
				}
			} else if (element instanceof FieldSet) {
				collectVisibleFields((FieldSet) element, fields);
			}
		}
		return fields;
	}

	public String getReadTitle() {
		String title = crudConfiguration.getReadTitle();
		if (StringUtils.isEmpty(title)) {
			return ShortNameUtils.getName(getClassAccessor(), object);
		} else {
			OgnlTextFormat textFormat = OgnlTextFormat.create(title);
			return textFormat.format(this);
		}
	}

	public String getSearchTitle() {
		String title = crudConfiguration.getSearchTitle();
		if (StringUtils.isBlank(title)) {
			title = getPage().getTitle();
		}
		OgnlTextFormat textFormat = OgnlTextFormat.create(StringUtils.defaultString(title));
		return textFormat.format(this);
	}

	public String getEditTitle() {
		String title = crudConfiguration.getEditTitle();
		if (StringUtils.isEmpty(title)) {
			return ShortNameUtils.getName(getClassAccessor(), object);
		} else {
			OgnlTextFormat textFormat = OgnlTextFormat.create(StringUtils.defaultString(title));
			return textFormat.format(this);
		}
	}

	public String getCreateTitle() {
		String title = crudConfiguration.getCreateTitle();
		if (StringUtils.isBlank(title)) {
			title = getPage().getTitle();
		}
		OgnlTextFormat textFormat = OgnlTextFormat.create(StringUtils.defaultString(title));
		return textFormat.format(this);
	}

	public CrudConfiguration getCrudConfiguration() {
		return crudConfiguration;
	}

	public void setCrudConfiguration(CrudConfiguration crudConfiguration) {
		this.crudConfiguration = crudConfiguration;
	}

	public ClassAccessor getClassAccessor() {
		return classAccessor;
	}

	public void setClassAccessor(ClassAccessor classAccessor) {
		this.classAccessor = classAccessor;
	}

	public PkHelper getPkHelper() {
		return pkHelper;
	}

	public void setPkHelper(PkHelper pkHelper) {
		this.pkHelper = pkHelper;
	}

	public List<CrudSelectionProvider> getCrudSelectionProviders() {
		return selectionProviderSupport.getCrudSelectionProviders();
	}

	public String[] getSelection() {
		return selection;
	}

	public void setSelection(String[] selection) {
		this.selection = selection;
	}

	public String getSearchString() {
		return searchString;
	}

	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}

	public String getSuccessReturnUrl() {
		return successReturnUrl;
	}

	public void setSuccessReturnUrl(String successReturnUrl) {
		this.successReturnUrl = successReturnUrl;
	}

	public SearchForm getSearchForm() {
		return searchForm;
	}

	public void setSearchForm(SearchForm searchForm) {
		this.searchForm = searchForm;
	}

	public List<? extends T> getObjects() {
		return objects;
	}

	public void setObjects(List<? extends T> objects) {
		this.objects = objects;
	}

	public T getObject() {
		return object;
	}

	public void setObject(T object) {
		this.object = object;
	}

	public boolean isMultipartRequest() {
		return form != null && form.isMultipartRequest();
	}

	public List<TextField> getEditableRichTextFields() {
		List<TextField> richTextFields = new ArrayList<TextField>();
		for (FieldSet fieldSet : form) {
			for (FormElement field : fieldSet) {
				if (field instanceof TextField && ((TextField) field).isEnabled() && !form.getMode().isView(((TextField) field).isInsertable(), ((TextField) field).isUpdatable()) && ((TextField) field).isRichText()) {
					richTextFields.add(((TextField) field));
				}
			}
		}
		return richTextFields;
	}

	public boolean isFormWithRichTextFields() {
		return !getEditableRichTextFields().isEmpty();
	}

	public Form getCrudConfigurationForm() {
		return crudConfigurationForm;
	}

	public void setCrudConfigurationForm(Form crudConfigurationForm) {
		this.crudConfigurationForm = crudConfigurationForm;
	}

	public TableForm getPropertiesTableForm() {
		return propertiesTableForm;
	}

	public Form getForm() {
		return form;
	}

	public void setForm(Form form) {
		this.form = form;
	}

	public TableForm getTableForm() {
		return tableForm;
	}

	public void setTableForm(TableForm tableForm) {
		this.tableForm = tableForm;
	}

	public TableForm getSelectionProvidersForm() {
		return selectionProvidersForm;
	}

	public Integer getFirstResult() {
		return firstResult;
	}

	public void setFirstResult(Integer firstResult) {
		this.firstResult = firstResult;
	}

	public Integer getMaxResults() {
		return maxResults;
	}

	public void setMaxResults(Integer maxResults) {
		this.maxResults = maxResults;
	}

	public String getSortProperty() {
		return sortProperty;
	}

	public void setSortProperty(String sortProperty) {
		this.sortProperty = sortProperty;
	}

	public String getSortDirection() {
		return sortDirection;
	}

	public void setSortDirection(String sortDirection) {
		this.sortDirection = sortDirection;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public boolean isSearchVisible() {
		// If embedded, search is always closed by default
		return searchVisible && !PageActionLogic.isEmbedded(this);
	}

	public void setSearchVisible(boolean searchVisible) {
		this.searchVisible = searchVisible;
	}

	public String getRelName() {
		return relName;
	}

	public void setRelName(String relName) {
		this.relName = relName;
	}

	public int getSelectionProviderIndex() {
		return selectionProviderIndex;
	}

	public void setSelectionProviderIndex(int selectionProviderIndex) {
		this.selectionProviderIndex = selectionProviderIndex;
	}

	public String getSelectFieldMode() {
		return selectFieldMode;
	}

	public void setSelectFieldMode(String selectFieldMode) {
		this.selectFieldMode = selectFieldMode;
	}

	public String getLabelSearch() {
		return labelSearch;
	}

	public void setLabelSearch(String labelSearch) {
		this.labelSearch = labelSearch;
	}

	public boolean isPopup() {
		return !StringUtils.isEmpty(popupCloseCallback);
	}

	public String getPopupCloseCallback() {
		return popupCloseCallback;
	}

	public void setPopupCloseCallback(String popupCloseCallback) {
		this.popupCloseCallback = popupCloseCallback;
	}

	public ResultSetNavigation getResultSetNavigation() {
		return resultSetNavigation;
	}

	public void setResultSetNavigation(ResultSetNavigation resultSetNavigation) {
		this.resultSetNavigation = resultSetNavigation;
	}
}