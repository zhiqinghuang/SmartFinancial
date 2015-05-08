package com.manydesigns.portofino.pageactions.crud;

import com.manydesigns.elements.annotations.InSummary;
import com.manydesigns.elements.annotations.Required;
import com.manydesigns.elements.annotations.Select;
import com.manydesigns.elements.annotations.Updatable;
import com.manydesigns.elements.options.DisplayMode;
import com.manydesigns.elements.options.SearchDisplayMode;

public class CrudSelectionProviderEdit {
    @Updatable(false)
    public String columns;

    @Select(nullOption = false)
    public String selectionProvider;

    @Select(nullOption = false)
    @Required
    public DisplayMode displayMode;

    @Select(nullOption = false)
    @Required
    public SearchDisplayMode searchDisplayMode;

    @InSummary(false) //Per non includere in TableForm
    public String[] fieldNames;

    public String createNewHref;

    public String createNewText;
}