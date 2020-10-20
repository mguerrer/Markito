package cl.set.markito;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;

public class ExecuteJS extends Markito {
    @Test
    public void HelloWorldTest() {
        OpenDriver();

        Get("http://demo.guru99.com/V4/");			
        		
        WebElement button = FindElement(By.name("btnLogin"));			
        		
        //Login to Guru99 		
        SendKeys(By.name("uid"),"mngr34926");					
        SendKeys(By.name("password"),"amUpenu");					
        		
        //Perform Click on LOGIN button using JavascriptExecutor		
        ExecuteJsScript("arguments[0].click();", button);

        String msg = GetTextOfAlert();
        ClickOKOnAlert();
        System.out.println(msg);                                

        CloseDriver();
    }
}
