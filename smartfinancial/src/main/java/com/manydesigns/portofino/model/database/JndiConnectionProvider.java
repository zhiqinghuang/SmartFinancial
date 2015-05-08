package com.manydesigns.portofino.model.database;

import com.manydesigns.elements.annotations.Required;
import org.apache.commons.dbutils.DbUtils;

import javax.naming.InitialContext;
import javax.sql.DataSource;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import java.sql.Connection;
import java.text.MessageFormat;

@XmlAccessorType(XmlAccessType.NONE)
public class JndiConnectionProvider extends ConnectionProvider {
	private String jndiResource;

	public JndiConnectionProvider() {
		super();
	}

	public String getDescription() {
		return MessageFormat.format("JNDI data source: {0}", jndiResource);
	}

	public Connection acquireConnection() throws Exception {
		DataSource ds = getDataSource();
		return ds.getConnection();
	}

	public DataSource getDataSource() throws Exception {
		InitialContext ic = new InitialContext();
		return (DataSource) ic.lookup(jndiResource);
	}

	public void releaseConnection(Connection conn) {
		DbUtils.closeQuietly(conn);
	}

	@XmlAttribute(required = true)
	@Required
	public String getJndiResource() {
		return jndiResource;
	}

	public void setJndiResource(String jndiResource) {
		this.jndiResource = jndiResource;
	}
}