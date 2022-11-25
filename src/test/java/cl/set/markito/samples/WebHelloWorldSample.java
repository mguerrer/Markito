package cl.set.markito.samples;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;

import cl.set.markito.MarkitoWeb;

public class WebHelloWorldSample extends MarkitoWeb {
    @Test
    public void HelloWorldTest() throws Exception {
        OpenChromeDriver(false);
        Get("http://www.google.com");
        // Search "Hello world!!" string
        SendKeys( By.name("q"), "Hello world!!");
        Click( By.name("btnK"));

        // Show all texts found in first page.
        List<WebElement>results = FindElements(By.tagName("h3"));
        for (WebElement webElement : results) {
            println(webElement.getText());
        }
        CloseWebDriver();
    }
}
