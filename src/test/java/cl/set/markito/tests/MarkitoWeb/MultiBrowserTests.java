package cl.set.markito.tests.MarkitoWeb;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import cl.set.markito.MarkitoWeb;
import cl.set.markito.browsers.*;
import cl.set.markito.cloud.BrowserStack;
import cl.set.markito.devices.*;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.github.bonigarcia.wdm.WebDriverManager;

public class MultiBrowserTests extends MarkitoWeb {
    @ParameterizedTest
    @MethodSource("webScenarios")
    void GoogleSearchTest(Browser browser, Device device) throws Exception {
        // Arrange
        setDriver(openBrowserSessionInDevice(browser, device));
        get("https://www.google.cl");
        // Act
        By queryTextBox = By.name("q");
        sendKeys(queryTextBox, "Markito" + Keys.ENTER);
        getScreenSnapshotWithDate("GoogleSearchTest");

        List<WebElement> results = findElements(By.tagName("h3"));
        for (WebElement result : results) {
            println(result.getText());
        }
        // Assert
        Assert.assertTrue("FAILED", results.size() > 0);
        closeWebSessionInDevice();

    }

    private WebDriver openBrowserSessionInDevice(Browser browser, Device device) throws Exception {
        WebDriver driver;
        BrowserStack bs = new BrowserStack();

        printf(ANSI_YELLOW + "Creating Markito WEB session on browser " + browser.getName() + " on device "
                + device.getName() + " " + device.getPlatform());
        try {
            if (device.getProviderURL().contains("browserstack")) {
                bs.setBsCredentials(bs.getBsUsername(), bs.getBsPassword());
                bs.setDesiredWebTechnicalCapabilities( browser.getName(), device.getPlatform(), device.getPlatform_version());
                bs.setProjectInformation("Multibrowser/MultiPlatform tests.", "MultiTests", "MultiBrowserTest");
            } else if (!device.getProviderURL().equals("")) {
                throw new Exception(ANSI_RED + "ERROR: Provider " + device.getProviderURL()
                        + " not supported for device " + device.getName());
            }
            if (device.equals(LOCAL_COMPUTER_DEVICE)) {
                driver = setLocalWebDrivers(browser);
            } else {
                driver = setRemoteWebDrivers(device, bs.getCapabilities());
            }
        } catch (Exception e) {
            //bs.LogCapabilities((DesiredCapabilities) bs.getCapabilities());
            println("\n"+ANSI_RED + "ERROR on creating session." + e.getMessage());
            throw new Exception(e.getMessage());
        }
        return driver;
    }

    private WebDriver setRemoteWebDrivers(Device device, MutableCapabilities caps)
            throws MalformedURLException, Exception {
        WebDriver driver;
        switch (device.getPlatform()) {
            case "Android":
                driver = new AndroidDriver<MobileElement>(new URL(device.getProviderURL()), caps);
                break;
            case "iOS":
                driver = new IOSDriver<MobileElement>(new URL(device.getProviderURL()), caps);
                break;
            case "OS X":
            case "Windows":
                driver = new RemoteWebDriver(new URL(device.getProviderURL()), caps);
                break;
            default:
                throw new Exception(
                        ANSI_RED + "\nERROR on getting " + device.getPlatform() + " session, unknown platfom.");
        }
        return driver;
    }

    private WebDriver setLocalWebDrivers(Browser browser) {
        WebDriver driver = null;
        switch (browser.getName()) {
            case "Chrome":
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver();
                break;
            case "Edge":
                WebDriverManager.edgedriver().setup();
                driver = new EdgeDriver();
                break;
            case "Firefox":
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver();
                break;
            case "IE":
                WebDriverManager.iedriver().setup();
                driver = new InternetExplorerDriver();
                break;
        }
        return driver;
    }

    /**
     * Provides test scenarios for multi-browser and multi-platform testing.
     * 
     * @return
     */
    private static Stream<Arguments> webScenarios() {
        return Stream.of(
            Arguments.of(CHROME_BROWSER, LOCAL_COMPUTER_DEVICE),
            Arguments.of(FIREFOX_BROWSER, LOCAL_COMPUTER_DEVICE),
            Arguments.of(IE_BROWSER, LOCAL_COMPUTER_DEVICE),
            Arguments.of(EDGE_BROWSER, LOCAL_COMPUTER_DEVICE),

            Arguments.of(CHROME_BROWSER, WINDOWS10_COMPUTER_DEVICE),
            Arguments.of(FIREFOX_BROWSER, WINDOWS10_COMPUTER_DEVICE),
            // TODO: Arguments.of(IE_BROWSER, WINDOWS10_COMPUTER_DEVICE),
            Arguments.of(EDGE_BROWSER, WINDOWS10_COMPUTER_DEVICE),

            Arguments.of(CHROME_BROWSER, WINDOWS11_COMPUTER_DEVICE),
            Arguments.of(FIREFOX_BROWSER, WINDOWS11_COMPUTER_DEVICE),
            Arguments.of(IE_BROWSER, WINDOWS11_COMPUTER_DEVICE),
            Arguments.of(EDGE_BROWSER, WINDOWS11_COMPUTER_DEVICE),

            Arguments.of(CHROME_BROWSER, MAC_VENTURA_COMPUTER_DEVICE),
            Arguments.of(FIREFOX_BROWSER, MAC_VENTURA_COMPUTER_DEVICE),
            Arguments.of(SAFARI_BROWSER, MAC_VENTURA_COMPUTER_DEVICE),
            Arguments.of(EDGE_BROWSER, MAC_VENTURA_COMPUTER_DEVICE)

            /*Arguments.of(CHROME_BROWSER, IPHONE11PRO_DEVICE),
            Arguments.of(FIREFOX_BROWSER, IPHONE11PRO_DEVICE),
            Arguments.of(SAFARI_BROWSER, IPHONE11PRO_DEVICE),
            Arguments.of(EDGE_BROWSER, IPHONE11PRO_DEVICE),
            Arguments.of(CHROME_BROWSER, GOOGLEPIXEL3_DEVICE),
            Arguments.of(FIREFOX_BROWSER, GOOGLEPIXEL3_DEVICE),
            Arguments.of(EDGE_BROWSER, GOOGLEPIXEL3_DEVICE)*/
         
        );
    }
}
