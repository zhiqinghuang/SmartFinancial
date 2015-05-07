package com.manydesigns.portofino.pageactions.crud;

import com.manydesigns.elements.options.DisplayMode;
import com.manydesigns.elements.options.SearchDisplayMode;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface SelectionProviderSupport {
	void setup();

	List<CrudSelectionProvider> getCrudSelectionProviders();

	Map<List<String>, Collection<String>> getAvailableSelectionProviderNames();

	void disableSelectionProvider(List<String> properties);

	void configureSelectionProvider(List<String> properties, String name, DisplayMode displayMode, SearchDisplayMode searchDisplayMode, String createNewHref, String createNewText);
}