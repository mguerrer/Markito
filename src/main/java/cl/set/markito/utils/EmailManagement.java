package cl.set.markito.utils;

import java.io.IOException;
import javax.mail.Message;
import javax.mail.MessagingException;

/**
 * A class to handle emails.
 * Marcos Guerrero - 26-10-2021
 */
public interface EmailManagement {
	
	/**
	 * Get in an ArrayList all unread messages in Inbox folder.   It can set messages as read.
	 * @param host: email server url
	 * @param storeType: Tested with imap.
	 * @param user
	 * @param password
	 * @param markEmailAsRead: true if each msg is set as READ false otherwise.
	 * @return null on error, or an array of read messages.
	 */
	public Message[] getUnreadEmailList(String host, String storeType, String user, String password, boolean markEmailAsRead);
	/**
	 * Close the Email session.
	 * @throws MessagingException
	 */
	public void closeEmailSession() throws MessagingException;
	/**
	 * Gets in an string the Subject field of message.
	 * @param message
	 * @return From in string
		 * @throws MessagingException
	 */
	public String getSubject(Message message) throws MessagingException;
	/**
	 * Gets in an string the From field of message.
	 * @param message
	 * @return From in string
	 * @throws MessagingException
	 */
	public String getFrom(Message message) throws MessagingException;
	/**
	 * Gets in an string the Content field of message.
	 * @param message
	 * @return From in string
	 * @throws MessagingException
	 * @throws IOException
	 */
	public String getContent(Message message) throws IOException, MessagingException;
}
