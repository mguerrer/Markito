package cl.set.markito.tests.MarkitoWeb;

import java.net.URL;
import java.util.List;
import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import cl.set.markito.MarkitoWeb;
import cl.set.markito.browsers.*;
import cl.set.markito.cloud.BrowserStack;
import cl.set.markito.devices.*;
import io.github.bonigarcia.wdm.WebDriverManager;

public class MultiBrowserTests extends MarkitoWeb {
    @ParameterizedTest
    @MethodSource("webScenarios")
    void MultiBrowserTest(Browser browser, Device device) throws Exception {

        setDriver( OpenBrowserSessionInDevice( browser, device) );
        get("https://www.google.cl");
        By query = By.name("q");
        sendKeys(query, "Markito"+Keys.ENTER);
        List<WebElement> results = findElements( By.tagName("h3"));
        for ( WebElement result : results) {
            println( result.getText());
        }
        quit();
        Assert.assertTrue("FAILED", results.size()>0);
    }

    private WebDriver OpenBrowserSessionInDevice(Browser browser, Device device) throws Exception {
        WebDriver driver;
        printf(ANSI_YELLOW+"Creating Markito WEB session on browser " + browser.getName() + " on device " +  device.getName() + " " + device.getOS());
        try {
            if ( device.equals(LOCAL_COMPUTER_DEVICE)) {
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver();
            } else {
                if ( device.getProviderURL().contains("browserstack")) {
                    BrowserStack bs = new BrowserStack();
                    bs.SetBsCredentials( bs.getBsUsername(), bs.getBsPassword());
                    bs.SetDeviceAndOsVersion(device.getName(), device.getOS(), device.getOS_version());
                    bs.setDesiredTechnicalCapabilities(null, browser.getName(), device.getName(), device.getOS(), device.getOS_version());
                    bs.SetProjectInformation("Multibrowser/MultiPlatform tests.", "MultiTests", "MultiBrowserTest");
                    driver = new RemoteWebDriver( new URL(device.getProviderURL()), bs.getCapabilities());

                } else {
                    throw new Exception(ANSI_RED+"ERROR: Provider "+ device.getProviderURL() +" not supported for device "+device.getName());
                }
            }
            println(ANSI_YELLOW+" done.");
        } catch (Exception e) {
            println( ANSI_RED+"ERROR on creating session."+ e.getMessage());
            throw new Exception(e.getMessage());
        }
        return driver;
    }

    /**
     * Provides test scenarios for multi-browser and multi-platform testing.
     * @return
     */
    private static Stream<Arguments> webScenarios() {
        return Stream.of(
                Arguments.of(CHROME_BROWSER, LOCAL_COMPUTER_DEVICE),
                Arguments.of(FIREFOX_BROWSER, LOCAL_COMPUTER_DEVICE),
                Arguments.of(IE_BROWSER, LOCAL_COMPUTER_DEVICE),
                Arguments.of(EDGE_BROWSER,  LOCAL_COMPUTER_DEVICE),
                Arguments.of(CHROME_BROWSER, IPHONE11PRO_DEVICE),
                Arguments.of(FIREFOX_BROWSER, IPHONE11PRO_DEVICE),
                Arguments.of(SAFARI_BROWSER, IPHONE11PRO_DEVICE),
                Arguments.of(EDGE_BROWSER,  IPHONE11PRO_DEVICE),
                Arguments.of(CHROME_BROWSER, GOOGLEPIXEL3_DEVICE),
                Arguments.of(FIREFOX_BROWSER, GOOGLEPIXEL3_DEVICE),
                Arguments.of(EDGE_BROWSER, GOOGLEPIXEL3_DEVICE)
            );
    }
}
