package com.manydesigns.mail.pop3;

import java.util.Set;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;

import com.sun.mail.pop3.POP3SSLStore;

public class POP3SSLClient extends POP3Client {
	public POP3SSLClient(String host, String protocol, int port, String username, String password) {
		super(host, protocol, port, username, password);
	}

	public Set<String> read() {
		emails.clear();
		URLName url = new URLName(protocol, host, port, "", username, password);
		Folder inbox = null;
		Store store = null;
		try {
			Session session = Session.getInstance(pop3Props, null);
			store = new POP3SSLStore(session, url);
			store.connect();

			inbox = store.getFolder("INBOX");
			if (inbox == null) {
				logger.warn("No INBOX");
				return null;
			}
			inbox.open(Folder.READ_ONLY);

			Message[] messages = inbox.getMessages();
			for (Message message : messages) {
				if (message.getSubject().toLowerCase().contains(DELIVERY_STATUS_NOTIFICATION)) {
					extractEmail(message);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (inbox != null) {
				try {
					inbox.close(false);
				} catch (MessagingException e) {
					logger.warn("cannot close INBOX", e);
				}
			}
			if (store != null) {
				try {
					store.close();
				} catch (MessagingException e) {
					logger.warn("cannot close Store", e);
				}
			}
		}
		return emails;
	}
}