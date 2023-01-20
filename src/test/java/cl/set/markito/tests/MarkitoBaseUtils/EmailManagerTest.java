package cl.set.markito.tests.MarkitoBaseUtils;
import javax.mail.Message;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import cl.set.markito.MarkitoBaseUtils;
import cl.set.markito.utils.GmailManager;

public class EmailManagerTest extends MarkitoBaseUtils{
	@Disabled
	@Test
    public void GetMyEmailsTest() throws Exception {

		String host = "imap.gmail.com";
		String mailStoreType = "imap";
		String username = "myuser@gmail.com";
		String password = "password";
		GmailManager emailClient=null;
		Message[] messages = null;
		// Arrange
		// Creates connection
		try {
			emailClient = new GmailManager(host, mailStoreType, username, password);
		} catch (Exception e) {
			Assert.isTrue(false, e.getMessage());
		}
		if (emailClient == null) {
			throw new Exception("ERROR: Can not get email client.");
		}
		// Acts
		try {
			messages = emailClient.getUnreadEmailList(host, mailStoreType, username, password, false);
		} catch (Exception e) {
			Assert.isTrue(false, e.getMessage());
		}
		if ( messages != null) {
			for (Message message : messages) {
				println("From:"+emailClient.getFrom(message));
				println("Subject:"+emailClient.getSubject(message));
				println("Content:"+emailClient.getContent(message));
			}
		} else {
			println("No messages found...");
		}

		emailClient.closeEmailSession();
    }
    
}
