package cl.set.markito.samples;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.remote.DesiredCapabilities;

import cl.set.markito.framework.cloud.BrowserStack;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSElement;

public class BrowserStackIOS extends BrowserStack {

    private String userName = getBsUsername();
    private String accessKey = getBsPassword();
    // TODO: Update Browserstack app url for apps/LocalSample.ipa
    private static String app_url = "bs://78f0f26662f5b73438f2b6fca581d0b8366ada20";

    @Test
    public void TestLocalSampleOnBsIOS() throws MalformedURLException, InterruptedException {
        DesiredCapabilities caps = new DesiredCapabilities();

        userName = getBsUsername();
        accessKey = getBsPassword();
        setBsCredentials(userName, accessKey);

        caps.setCapability("device", "iPhone 11 Pro");
        caps.setCapability("os_version", "15");
        caps.setCapability("project", "TestLocalSampleOnBsIOS");
        caps.setCapability("build", "1.0");
        caps.setCapability("name", "Bstack-[Java] Sample Test");
        caps.setCapability("app", app_url);

        IOSDriver<IOSElement> driver = new IOSDriver<IOSElement>(
                new URL("https://" + userName + ":" + accessKey + "@hub-cloud.browserstack.com/wd/hub"), caps);

        /*
         * IOSElement textButton = (IOSElement) new WebDriverWait(driver, 30).until(
         * ExpectedConditions.elementToBeClickable(MobileBy.
         * AccessibilityId("Text Button")));
         * textButton.click();
         * IOSElement textInput = (IOSElement) new WebDriverWait(driver, 30).until(
         * ExpectedConditions.elementToBeClickable(MobileBy.AccessibilityId("Text Input"
         * )));
         * textInput.sendKeys("hello@browserstack.com\n");
         * 
         * Thread.sleep(5000);
         * 
         * IOSElement textOutput = (IOSElement) new WebDriverWait(driver, 30).until(
         * ExpectedConditions.elementToBeClickable(MobileBy.
         * AccessibilityId("Text Output")));
         * 
         * if (textOutput != null &&
         * textOutput.getText().equals("hello@browserstack.com"))
         * assert (true);
         * else
         * assert (false);
         */

        // The driver.quit statement is required, otherwise the test continues to
        // execute, leading to a timeout.
        driver.quit();
    }
}
