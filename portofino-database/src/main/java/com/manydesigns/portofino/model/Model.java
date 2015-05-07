package com.manydesigns.portofino.model;

import com.manydesigns.portofino.model.database.Database;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class Model {
	public static final String JAXB_MODEL_PACKAGES = "com.manydesigns.portofino.model";

	protected final ArrayList<Database> databases;

	public static final Logger logger = LoggerFactory.getLogger(Model.class);

	public Model() {
		this.databases = new ArrayList<Database>();
	}

	public void init() {
		for (Database database : databases) {
			init(database);
		}
	}

	public void init(ModelObject rootObject) {
		new ResetVisitor().visit(rootObject);
		new InitVisitor(this).visit(rootObject);
		new LinkVisitor(this).visit(rootObject);
	}

	@XmlElementWrapper(name = "databases")
	@XmlElement(name = "database", type = com.manydesigns.portofino.model.database.Database.class)
	public List<Database> getDatabases() {
		return databases;
	}
}