package cl.set.markito;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import cl.set.markito.MarkitoEmail.Email;

public class EmailTest {
	@Test
    public void GetMguerrerEmailsTest() {

		String host = "imap.gmail.com";
		String mailStoreType = "imap";
		String username = "myuser@gmail.com";
		String password = "password";
		MarkitoEmail emailClient = new MarkitoEmail(host, mailStoreType, username, password);

		ArrayList<Email> emailList = emailClient.getUnreadEmailList(host, mailStoreType, username, password, false);
    }
    
}
