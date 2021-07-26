package cl.set.markito.tests;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import cl.set.markito.MarkitoWeb;

public class Multibrowser extends MarkitoWeb {
    @Test
    public void Chrome() {
        OpenChromeDriver(false);
    }
    @Test
    public void Firefox() {
        OpenFirefoxDriver(false);
    } 
    @Test
    public void InternetExplorer() {
        OpenInternetExplorerDriver();
    } 
    @Test
    public void InternetExplorerWithUrl() {
        OpenInternetExplorerDriver("http://www.google.com/");
    } 
    @AfterEach
    public void HelloWorldTest() {
        Get("http://the-internet.herokuapp.com/");
        WaitForPageToLoad();
        By welcomeToTheInternetMsg = By.xpath("//h1");
        printf(ANSI_GREEN+"Message is [%s]\n", GetText(welcomeToTheInternetMsg));
        CloseWebDriver();
    }
}
