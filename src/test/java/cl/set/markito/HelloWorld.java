package cl.set.markito;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;

public class HelloWorld extends Markito {
    @Test
    public void HelloWorldTest() {
        OpenDriver();
        Get("http://www.google.com");
        SendKeys( By.name("q"), "Hello world!!");
        Click( By.name("btnK"));
        CloseDriver();
    }
}
