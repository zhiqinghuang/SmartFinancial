package com.manydesigns.mail.pop3;

import java.util.Set;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;

public class POP3SimpleClient extends POP3Client {
	public POP3SimpleClient(String host, String provider, int port, String username, String password) {
		super(host, provider, port, username, password);
	}

	public Set<String> read() {
		emails.clear();
		Folder inbox = null;
		Store store = null;
		try {
			Session session = Session.getDefaultInstance(pop3Props, null);
			store = session.getStore(protocol);
			store.connect(host, username, password);

			inbox = store.getFolder("INBOX");
			if (inbox == null) {
				logger.warn("No INBOX");
				return null;
			}
			inbox.open(Folder.READ_ONLY);

			Message[] messages = inbox.getMessages();
			for (Message message : messages) {
				extractEmail(message);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (inbox != null) {
				try {
					inbox.close(false);
				} catch (MessagingException e) {
					logger.warn("Cannot close INBOX", e);
				}
			}
			if (store != null) {
				try {
					store.close();
				} catch (MessagingException e) {
					logger.warn("Cannot close Store", e);
				}
			}
		}
		return emails;
	}
}