package cl.set.markito;
import java.io.IOException;
import javax.mail.Message;
import javax.mail.MessagingException;
import org.junit.jupiter.api.Test;

public class EmailTest {
	@Test
    public void GetMguerrerEmailsTest() throws MessagingException, IOException {

		String host = "imap.gmail.com";
		String mailStoreType = "imap";
		String username = "myuser@gmail.com";
		String password = "password";
		MarkitoEmail emailClient = new MarkitoEmail(host, mailStoreType, username, password);

		Message[] messages = emailClient.getUnreadEmailList(host, mailStoreType, username, password, false);
		for (Message message : messages) {
			System.out.println("From:"+emailClient.getFrom(message));
			System.out.println("Subject:"+emailClient.getSubject(message));
			System.out.println("COntent:"+emailClient.getContent(message));
			
		}
		emailClient.closeEmailSession();
    }
    
}
