package cl.set.markito;

import java.util.ArrayList;
import java.util.Properties;

import javax.mail.Flags;
import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.FlagTerm;

/**
 * A class to handle emails.
 * Marcos Guerrero - 26-10-2021
 */
public class MarkitoEmail {
	public class Email { // Represents the email basic object.
		public String subject="";
		public String from="";
		public String text="";
	}
	public ArrayList<Email> emailList = new ArrayList<Email>();
	public Session emailSession  = null;
	public Store store = null;

	public MarkitoEmail( String host, String storeType, String user, String password) {
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
			System.out.println("Connected to "+host+"..." );

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}

	}

	/**
	 * Get in an ArrayList all unread messages in Inbox folder.   It can set messages as read.
	 * @param host: email server url
	 * @param storeType: Tested with imap.
	 * @param user
	 * @param password
	 * @param markEmailAsRead: true if each msg is set as READ false otherwise.
	 * @return
	 */
	public ArrayList<Email> getUnreadEmailList(String host, String storeType, String user, String password, boolean markEmailAsRead) {
		try {
			// create the inbox object and open it
			Folder inbox = store.getFolder("Inbox");
			inbox.open(Folder.READ_WRITE);

			// retrieve the messages from the folder in an array and print it
			Message[] messages = inbox.search(new FlagTerm(new Flags(Flag.SEEN), false));
			System.out.println("messages.length---" + messages.length);

			for (int i = 0, n = messages.length; i < n; i++) {
				Message message = messages[i];
				if (markEmailAsRead)
					message.setFlag(Flag.SEEN, true);
				System.out.println("---------------------------------");
				System.out.println("Email Number " + (i + 1));
				System.out.println("Subject: " + message.getSubject());
				System.out.println("From: " + message.getFrom()[0]);
				System.out.println("Text: " + message.getContent().toString());
				Email email = new Email();
				email.from = message.getFrom()[0].toString();
				email.subject = message.getSubject().toString();
				email.text = message.getContent().toString();
				emailList.add(i, email);
			}

			inbox.close(false);
			store.close();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return emailList;
	}



}
