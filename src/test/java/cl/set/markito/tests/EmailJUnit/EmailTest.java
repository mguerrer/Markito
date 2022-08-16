package cl.set.markito.tests.EmailJUnit;
import java.io.IOException;
import javax.mail.Message;
import javax.mail.MessagingException;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import cl.set.markito.MarkitoEmail;

public class EmailTest {
	@Test
    public void GetMguerrerEmailsTest() throws MessagingException, IOException {

		String host = "imap.gmail.com";
		String mailStoreType = "imap";
		String username = "myuser@gmail.com";
		String password = "password";
		MarkitoEmail emailClient=null;
		Message[] messages = null;
		// Arrange
		// Creates connection
		try {
			emailClient = new MarkitoEmail(host, mailStoreType, username, password);
		} catch (Exception e) {
			Assert.isTrue(false, e.getMessage());
		}
		// Acts
		try {
			messages = emailClient.getUnreadEmailList(host, mailStoreType, username, password, false);
		} catch (Exception e) {
			Assert.isTrue(false, e.getMessage());
		}
		for (Message message : messages) {
			System.out.println("From:"+emailClient.getFrom(message));
			System.out.println("Subject:"+emailClient.getSubject(message));
			System.out.println("Content:"+emailClient.getContent(message));
			
		}
		emailClient.closeEmailSession();
    }
    
}
