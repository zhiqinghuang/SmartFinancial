package com.manydesigns.portofino.pageactions.m2m.configuration;

import com.manydesigns.elements.options.DisplayMode;
import com.manydesigns.portofino.model.database.DatabaseLogic;
import com.manydesigns.portofino.model.database.ForeignKey;
import com.manydesigns.portofino.model.database.ModelSelectionProvider;
import com.manydesigns.portofino.model.database.Table;
import org.apache.commons.lang.StringUtils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.NONE)
public class SelectionProviderReference {
	protected String foreignKeyName;
	protected boolean enabled = true;
	protected String displayModeName;
	protected String selectionProviderName;

	protected ForeignKey foreignKey;
	private DisplayMode displayMode;
	private ModelSelectionProvider selectionProvider;

	public void init(Table table) {
		if (displayModeName != null) {
			displayMode = DisplayMode.valueOf(displayModeName);
		} else {
			displayMode = DisplayMode.DROPDOWN;
		}

		if (!StringUtils.isEmpty(foreignKeyName)) {
			foreignKey = DatabaseLogic.findForeignKeyByName(table, foreignKeyName);
		} else if (!StringUtils.isEmpty(selectionProviderName)) {
			selectionProvider = DatabaseLogic.findSelectionProviderByName(table, selectionProviderName);
		} else {
			throw new Error("foreignKey and selectionProvider are both null");
		}
	}

	// **************************************************************************
	// Getters/setters
	// **************************************************************************

	@XmlAttribute(name = "fk")
	public String getForeignKeyName() {
		return foreignKeyName;
	}

	public void setForeignKeyName(String foreignKeyName) {
		this.foreignKeyName = foreignKeyName;
	}

	@XmlAttribute(name = "selectionProvider")
	public String getSelectionProviderName() {
		return selectionProviderName;
	}

	public void setSelectionProviderName(String selectionProviderName) {
		this.selectionProviderName = selectionProviderName;
	}

	public ForeignKey getForeignKey() {
		return foreignKey;
	}

	@XmlAttribute
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@XmlAttribute(name = "displayMode")
	public String getDisplayModeName() {
		return displayModeName;
	}

	public void setDisplayModeName(String displayModeName) {
		this.displayModeName = displayModeName;
	}

	public DisplayMode getDisplayMode() {
		return displayMode;
	}

	public void setDisplayMode(DisplayMode displayMode) {
		this.displayMode = displayMode;
		displayModeName = displayMode.name();
	}

	public ModelSelectionProvider getSelectionProvider() {
		return selectionProvider;
	}

	public ModelSelectionProvider getActualSelectionProvider() {
		return foreignKey != null ? foreignKey : selectionProvider;
	}
}