package cl.set.markito.samples;


import java.net.URL;
import java.net.MalformedURLException;

import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;

import io.appium.java_client.MobileBy;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSElement;

public class BrowserStackIOS {

  public static String userName = "marcos150";
  public static String accessKey = "uGotcDU7y8nn9V8tnJcS";

  public static void main(String args[]) throws MalformedURLException, InterruptedException {
    DesiredCapabilities caps = new DesiredCapabilities();

    caps.setCapability("device", "iPhone 11 Pro");
    caps.setCapability("os_version", "15");
    caps.setCapability("project", "My First Project");
    caps.setCapability("build", "My First Build");
    caps.setCapability("name", "Bstack-[Java] Sample Test");
    //caps.setCapability("app", "<app_url>");

    IOSDriver<IOSElement> driver = new IOSDriver<IOSElement>(new URL("https://"+userName+":"+accessKey+"@hub-cloud.browserstack.com/wd/hub"), caps);

    IOSElement textButton = (IOSElement) new WebDriverWait(driver, 30).until(
        ExpectedConditions.elementToBeClickable(MobileBy.AccessibilityId("Text Button")));
    textButton.click();
    IOSElement textInput = (IOSElement) new WebDriverWait(driver, 30).until(
        ExpectedConditions.elementToBeClickable(MobileBy.AccessibilityId("Text Input")));
    textInput.sendKeys("hello@browserstack.com\n");

    Thread.sleep(5000);

    IOSElement textOutput = (IOSElement) new WebDriverWait(driver, 30).until(
        ExpectedConditions.elementToBeClickable(MobileBy.AccessibilityId("Text Output")));

    if(textOutput != null && textOutput.getText().equals("hello@browserstack.com"))
        assert(true);
    else
        assert(false);

    // The driver.quit statement is required, otherwise the test continues to execute, leading to a timeout.
    driver.quit();
  }
}
