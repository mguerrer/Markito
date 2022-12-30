package cl.set.markito.tests.AndroidAndIOS;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;

import java.util.logging.Level;

import org.junit.jupiter.api.Assertions;


public class SampleWebTest extends BrowserStack{
    @DisplayName("Click on home page text button and read test message.")
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

            LogEntries les = driver.manage().logs().get(LogType.PERFORMANCE);
            for (LogEntry le : les) {
              println(le.getMessage());
            }
    
            driver.quit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void testSetup(String platform, String browser) {
        switch (platform) {
            case "android":
                setDesiredTechnicalCapabilities( null, browser, 
                                                "Google Pixel 3", 
                                                 platform, "9.0");
            break;
            case "iOS":
                setDesiredTechnicalCapabilities( null, browser,
                                                "iPhone 11 Pro", 
                                                platform, "15");
            break;
        }
        SetProjectInformation("Test BrowserStack WEB App", platform, "Web test Run on "+platform + " and browser "+browser);
        LoggingPreferences logPrefs = new LoggingPreferences();
        logPrefs.enable(LogType.PERFORMANCE, Level.ALL);
        caps.setCapability(CapabilityType.LOGGING_PREFS, logPrefs);
    }

}
