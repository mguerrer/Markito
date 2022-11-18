package cl.set.markito.samples;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;

import cl.set.markito.MarkitoWeb;

public class WebHelloWorldSample extends MarkitoWeb {
    @Test
    public void HelloWorldTest() throws Exception {
        OpenChromeDriver(false);
        Get("http://www.google.com");
        SendKeys( By.name("q"), "Hello world!!");
        Click( By.name("btnK"));
        CloseWebDriver();
    }
}
