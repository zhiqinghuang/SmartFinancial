package com.manydesigns.mail.pop3;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.BodyPart;
import javax.mail.Multipart;
import javax.mail.Part;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class POP3Client {
	protected static final String DELIVERY_STATUS_NOTIFICATION = "delivery status notification";
	protected final String host;
	protected final String protocol;
	protected final int port;
	protected final String username;
	protected final String password;
	protected final Properties pop3Props;
	public static final Logger logger = LoggerFactory.getLogger(POP3Client.class);

	protected Set<String> emails;

	public String getHost() {
		return host;
	}

	public String getProtocol() {
		return protocol;
	}

	public int getPort() {
		return port;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public Properties getPop3Props() {
		return pop3Props;
	}

	public POP3Client(String host, String protocol, int port, String username, String password) {
		this.host = host;
		this.protocol = protocol;
		this.port = port;
		this.username = username;
		this.password = password;
		pop3Props = new Properties();
		pop3Props.setProperty("mail.pop3.port", "" + port);
		String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
		pop3Props.setProperty("mail.pop3.socketFactory.class", SSL_FACTORY);
		pop3Props.setProperty("mail.pop3.socketFactory.fallback", "false");
		pop3Props.setProperty("mail.pop3.socketFactory.port", "" + port);
		this.emails = new HashSet<String>();
	}

	protected Set<String> extractEmail(Part p) throws Exception {

		Pattern pattern = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$");
		String ct = "";
		if (p.isMimeType("multipart/*")) {
			Multipart mp = (Multipart) p.getContent();

			for (int x = 0; x < mp.getCount(); x++) {
				BodyPart bodyPart = mp.getBodyPart(x);

				String disposition = bodyPart.getDisposition();

				if (disposition != null && (disposition.equals(BodyPart.ATTACHMENT))) {
					// attachment do nothing
				} else {
					if (bodyPart.getContent() instanceof String)
						ct = ct + " " + bodyPart.getContent();
				}
			}
		} else {
			ct = ct + p.getContent();
		}
		StringTokenizer st = new StringTokenizer(ct, "\n,; ");
		while (st.hasMoreTokens()) {
			String line = st.nextToken();
			Matcher m = pattern.matcher(line);

			if (m.find()) {
				String email = line.substring(m.start(), m.end());

				if (!email.contains(getUsername()))
					emails.add(email);
			}

		}
		return emails;
	}

	public Set<String> getEmails() {
		return emails;
	}

	abstract Set<String> read();
}