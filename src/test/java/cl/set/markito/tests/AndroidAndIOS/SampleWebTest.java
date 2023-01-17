package cl.set.markito.tests.AndroidAndIOS;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import cl.set.markito.framework.cloud.BrowserStack;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;

/**
 * This test Browserstack's provided LocalSample.apk and  LocalSample.ipa.
 */
public class SampleWebTest extends BrowserStack{
    @Disabled
    @DisplayName("Click on home page text button and read message.")
    @ParameterizedTest
    @ValueSource(strings = { "iOS", "android"})
    void SampleAppTestOnMobile( String platform ){
        // Arrange
        WebDriver driver;
        testSetup(platform, "Chrome");
        try {
            driver = openBrowserStackWebSession();
            Assertions.assertNotEquals( null, driver, "Can not open Appium server."  );

            //Assertions.assertEquals(driver.isBrowser(), true);
            driver.get("https://www.google.com/");
            By query = By.name("q");
            driver.findElement(query).sendKeys("Markito");
    
            driver.quit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void testSetup(String platform, String browser) {
        switch (platform) {
            case "android":
                setDesiredWebTechnicalCapabilities( browser, GOOGLEPIXEL3_DEVICE.getName(), platform, "9.0");
            break;
            case "iOS":
                setDesiredWebTechnicalCapabilities( browser, IPHONE11PRO_DEVICE.getName(), platform, "15");
            break;
        }
        setProjectInformation("Test BrowserStack WEB App", platform, "Web test Run on "+platform + " and browser "+browser);
    }
}
