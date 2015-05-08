package com.manydesigns.portofino.model.database;

import com.manydesigns.portofino.model.Model;
import com.manydesigns.portofino.model.ModelObjectVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(value = XmlAccessType.NONE)
public class DatabaseSelectionProvider implements ModelSelectionProvider {
	protected final List<Reference> references;

	protected String name;
	protected String toDatabase;
	protected String sql;
	protected String hql;

	protected Table fromTable;

	public static final Logger logger = LoggerFactory.getLogger(DatabaseSelectionProvider.class);

	public DatabaseSelectionProvider() {
		references = new ArrayList<Reference>();
	}

	public DatabaseSelectionProvider(Table fromTable) {
		this();
		this.fromTable = fromTable;
	}

	public void afterUnmarshal(Unmarshaller u, Object parent) {
		fromTable = (Table) parent;
	}

	public void reset() {
	}

	public void init(Model model) {
		assert name != null;
		assert toDatabase != null;
	}

	public void link(Model model) {
	}

	public void visitChildren(ModelObjectVisitor visitor) {
		for (Reference reference : references) {
			visitor.visit(reference);
		}
	}

	public String getQualifiedName() {
		return name;
	}

	@XmlElementWrapper(name = "references")
	@XmlElement(name = "reference", type = Reference.class)
	public List<Reference> getReferences() {
		return references;
	}

	@XmlAttribute(required = true)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlAttribute(required = true)
	public String getToDatabase() {
		return toDatabase;
	}

	public void setToDatabase(String toDatabase) {
		this.toDatabase = toDatabase;
	}

	@XmlAttribute(required = false)
	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	@XmlAttribute(required = false)
	public String getHql() {
		return hql;
	}

	public void setHql(String hql) {
		this.hql = hql;
	}

	public Table getFromTable() {
		return fromTable;
	}

	public void setFromTable(Table fromTable) {
		this.fromTable = fromTable;
	}

	public Table getToTable() {
		return null;
	}

}
