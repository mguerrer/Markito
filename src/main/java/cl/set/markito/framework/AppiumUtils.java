package cl.set.markito.framework;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;

import java.util.function.Function;
/**
 * Sample code
 public static void main(String[] args) {
    DesiredCapabilities caps = new DesiredCapabilities();
    caps.setCapability("platformName", "Android");
    caps.setCapability("platformVersion", "11.0");
    caps.setCapability("deviceName", "Android Emulator");
    caps.setCapability("app", "/path/to/app.apk");

    AndroidDriver<MobileElement> driver = new AndroidDriver<>(new URL("http://localhost:4723/wd/hub"), caps);
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

    WebElement element = AppiumUtils.waitForElement(driver, wait, By.id("some_element_id"), WebElement click);
}

 */
public class AppiumUtils {
    public static <T> T waitForElement(WebDriver driver, Wait<WebDriver> wait, By locator, Function<WebElement, T> action) {
        wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        WebElement element = driver.findElement(locator);
        wait.until(ExpectedConditions.elementToBeClickable(element));
        return action.apply(element);
    }
}
