package com.manydesigns.portofino.model.database;

import com.manydesigns.elements.text.OgnlTextFormat;
import com.manydesigns.portofino.database.platforms.DatabasePlatformsRegistry;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import java.sql.Connection;
import java.sql.DriverManager;
import java.text.MessageFormat;

@XmlAccessorType(XmlAccessType.NONE)
public class JdbcConnectionProvider extends ConnectionProvider {
	protected String driver;
	protected String url;
	protected String username;
	protected String password;

	protected Configuration configuration;
	protected String keyPrefix;

	protected String actualUrl;
	protected String actualUsername;
	protected String actualPassword;

	public JdbcConnectionProvider() {
		super();
	}

	@Override
	public void init(DatabasePlatformsRegistry databasePlatformsRegistry) {
		keyPrefix = "portofino.database." + getDatabase().getDatabaseName() + ".";
		configuration = databasePlatformsRegistry.getPortofinoConfiguration();
		if (url == null || url.equals(keyPrefix + "url")) {
			actualUrl = configuration.getString(keyPrefix + "url");
			if (actualUrl == null) {
				throw new RuntimeException("Invalid connection provider for database " + getDatabase().getDatabaseName() + " - no URL specified");
			}
		} else {
			actualUrl = url;
		}
		actualUrl = OgnlTextFormat.format(actualUrl, null);
		if (username == null || username.equals(keyPrefix + "username")) {
			actualUsername = configuration.getString(keyPrefix + "username");
			if (actualUsername == null) {
				throw new RuntimeException("Invalid connection provider for database " + getDatabase().getDatabaseName() + " - no username specified");
			}
		} else {
			actualUsername = username;
		}
		if (password == null || password.equals(keyPrefix + "password")) {
			actualPassword = configuration.getString(keyPrefix + "password");
		} else {
			actualPassword = password;
		}
		super.init(databasePlatformsRegistry);
	}

	public String getDescription() {
		return MessageFormat.format("JDBC connection to URL: {0}", actualUrl);
	}

	public Connection acquireConnection() throws Exception {
		Class.forName(driver);
		return DriverManager.getConnection(actualUrl, actualUsername, actualPassword);
	}

	public void releaseConnection(Connection conn) {
		DbUtils.closeQuietly(conn);
	}

	@XmlAttribute(required = true)
	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	@XmlAttribute(required = true)
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@XmlAttribute(required = false)
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@XmlAttribute(required = false)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getActualUrl() {
		return actualUrl;
	}

	public void setActualUrl(String url) {
		if (this.url == null || this.url.equals(keyPrefix + "url")) {
			configuration.setProperty(keyPrefix + "url", url);
		} else {
			this.url = url;
		}
		actualUrl = url;
	}

	public String getActualUsername() {
		return actualUsername;
	}

	public void setActualUsername(String username) {
		if (this.username == null || this.username.equals(keyPrefix + "username")) {
			configuration.setProperty(keyPrefix + "username", username);
		} else {
			this.username = username;
		}
		actualUsername = username;
	}

	public String getActualPassword() {
		return actualPassword;
	}

	public void setActualPassword(String password) {
		if (this.password == null || this.password.equals(keyPrefix + "url")) {
			configuration.setProperty(keyPrefix + "password", password);
		} else {
			this.password = password;
		}
		actualPassword = password;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("driver", driver).append("url", actualUrl).append("username", actualUsername).append("password", actualPassword).toString();
	}
}