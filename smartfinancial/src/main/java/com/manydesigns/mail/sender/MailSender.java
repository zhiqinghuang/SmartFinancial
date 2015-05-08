package com.manydesigns.mail.sender;

import java.util.Set;

public interface MailSender {
	int runOnce(Set<String> idsToMarkAsSent);

	String getServer();

	void setServer(String server);

	int getPort();

	void setPort(int port);

	boolean isSsl();

	void setSsl(boolean ssl);

	boolean isTls();

	void setTls(boolean tls);

	String getLogin();

	void setLogin(String login);

	String getPassword();

	void setPassword(String password);
}