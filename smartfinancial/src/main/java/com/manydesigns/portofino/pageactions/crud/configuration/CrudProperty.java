package com.manydesigns.portofino.pageactions.crud.configuration;

import com.manydesigns.portofino.model.Annotated;
import com.manydesigns.portofino.model.Annotation;
import com.manydesigns.portofino.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(value = XmlAccessType.NONE)
public class CrudProperty implements Annotated {
	protected String name;
	protected String label;
	protected boolean searchable;
	protected boolean inSummary;
	protected boolean enabled;
	protected boolean insertable;
	protected boolean updatable;

	protected final List<Annotation> annotations = new ArrayList<Annotation>();

	public static final Logger logger = LoggerFactory.getLogger(CrudProperty.class);

	public CrudProperty() {
	}

	public void init(Model model) {
		assert name != null;
		for (Annotation annotation : annotations) {
			annotation.reset();
			annotation.init(model);
			annotation.link(model);
		}
	}

	@XmlAttribute(required = true)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlAttribute(required = false)
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	@XmlAttribute(required = true)
	public boolean isSearchable() {
		return searchable;
	}

	public void setSearchable(boolean searchable) {
		this.searchable = searchable;
	}

	@XmlAttribute(required = true)
	public boolean isInSummary() {
		return inSummary;
	}

	public void setInSummary(boolean inSummary) {
		this.inSummary = inSummary;
	}

	@XmlAttribute(required = true)
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@XmlAttribute(required = true)
	public boolean isInsertable() {
		return insertable;
	}

	public void setInsertable(boolean insertable) {
		this.insertable = insertable;
	}

	@XmlAttribute(required = true)
	public boolean isUpdatable() {
		return updatable;
	}

	public void setUpdatable(boolean updatable) {
		this.updatable = updatable;
	}

	@XmlElementWrapper(name = "annotations")
	@XmlElement(name = "annotation", type = Annotation.class)
	public List<Annotation> getAnnotations() {
		return annotations;
	}
}