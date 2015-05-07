package com.manydesigns.portofino.pageactions.crud;

import com.manydesigns.elements.annotations.Label;
import com.manydesigns.elements.annotations.Updatable;

public class CrudPropertyEdit {
	@Label("")
	public boolean enabled;

	@Updatable(false)
	public String name;

	public String label;
	public boolean insertable;
	public boolean updatable;
	public boolean inSummary;
	public boolean searchable;

}