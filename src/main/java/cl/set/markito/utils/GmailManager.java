package cl.set.markito.utils;

import java.io.IOException;
import java.util.Properties;

import javax.mail.Flags;
import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.FlagTerm;

/**
 * A class to handle emails.
 * Marcos Guerrero - 26-10-2021
 */
public class GmailManager extends DebugManager implements EmailManagement {
	public Session emailSession = null;
	public Store store = null;
	public Folder inbox = null;
	public Message[] messages = null;

	public GmailManager(String host, String storeType, String user, String password) throws Exception {
		Properties properties = new Properties();
		properties.put("mail.imap.host", host);
		properties.put("mail.imap.port", "993");
		properties.put("mail.imap.starttls.enable", "true");
		properties.put("mail.imap.ssl.trust", host);
		emailSession = Session.getDefaultInstance(properties);
		// create the imap store object and connect to the imap server
		try {
			store = emailSession.getStore("imaps");
			store.connect(host, user, password);
			println("Connected to " + host + "...");
		} catch (Exception e) {
			println("Error on connecting to " + host + "...");
			throw new Exception(e.getMessage());
		}

	}

	/**
	 * Get in an ArrayList all unread messages in Inbox folder. It can set messages
	 * as read.
	 * 
	 * @param host:            email server url
	 * @param storeType:       Tested with imap.
	 * @param user
	 * @param password
	 * @param markEmailAsRead: true if each msg is set as READ false otherwise.
	 * @return null on error, or an array of read messages.
	 */

	public Message[] getUnreadEmailList(String host, String storeType, String user, String password,
			boolean markEmailAsRead) {
		try {
			// create the inbox object and open it
			inbox = store.getFolder("Inbox");
			inbox.open(Folder.READ_WRITE);

			// retrieve the messages from the folder in an array and print it
			messages = inbox.search(new FlagTerm(new Flags(Flag.SEEN), false));
			System.out.println("messages.length---" + messages.length);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return messages;
	}

	/**
	 * Close the Email session.
	 * 
	 * @throws MessagingException
	 */
	public void closeEmailSession() throws MessagingException {
		inbox.close(false);
		store.close();
	}

	/**
	 * Gets in an string the Subject field of message.
	 * 
	 * @param message
	 * @return From in string
	 * @throws MessagingException
	 */
	public String getSubject(Message message) throws MessagingException {
		if (message != null)
			return message.getSubject();
		else {
			System.out.println("ERROR: getSubject: message parameter is null");
			return null;
		}
	}

	/**
	 * Gets in an string the From field of message.
	 * 
	 * @param message
	 * @return From in string
	 * @throws MessagingException
	 */
	public String getFrom(Message message) throws MessagingException {
		if (message != null)
			return message.getFrom()[0].toString();
		else {
			System.out.println("ERROR: getFrom: message parameter is null");
			return null;
		}
	}

	/**
	 * Gets in an string the Content field of message.
	 * 
	 * @param message
	 * @return From in string
	 * @throws MessagingException
	 * @throws IOException
	 */
	public String getContent(Message message) throws IOException, MessagingException {
		if (message != null)
			return message.getContent().toString();
		else {
			System.out.println("ERROR: getCOntent: message parameter is null");
			return null;
		}
	}
}
