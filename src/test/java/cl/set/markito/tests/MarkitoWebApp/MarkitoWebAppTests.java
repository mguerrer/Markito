/**
 * Tests different methods.
 */

package cl.set.markito.tests.MarkitoWebApp;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.support.PageFactory;

import cl.set.markito.framework.MarkitoWebApp;
import cl.set.markito.framework.devices.Device;
import cl.set.markito.samples.MarkitoWebApp.TheInternet.HomePage;

/**
 * This is an internal test class to verify implementation.  It is not intended to test Selenium or Appium.
 */
@TestInstance(Lifecycle.PER_CLASS)
@DisplayName("Test The Internet web page")
public class MarkitoWebAppTests extends MarkitoWebApp {
    HomePage homePage = null;
    String hostUrl = "http://the-internet.herokuapp.com";
    Device device = LOCAL_COMPUTER_DEVICE;

    @BeforeAll
    public void beforeAll() throws Exception {

    }

    public void setup(String testName) throws Exception {
        // Arrange overall session
        setBrowserstackProjectInformation("Markito", "Markito " + getMarkitoVersion(),
                "The Internet-" + testName + CHROME_BROWSER.getName() + "-" +  device.getName());
        setAutomaticDriverDownload(true); // Adds automatic driver download on local machine
        setDriver(openBrowserSessionInDevice(CHROME_BROWSER, device)); // Open web session on device
        // get URL
        // Open Home Page
        homePage = new HomePage(getDriver(), hostUrl);
        PageFactory.initElements(getDriver(), homePage);
        maximize();
        println("\n"+ANSI_GREEN+testName);
    }

    @AfterEach
    public void tearDown() throws Exception {
        closeWebSessionInDevice();
    }

    @Test
    @DisplayName("Test Windows management interface")
    public void WindowsManagementTest(TestInfo testInfo) throws Exception {
        setup(testInfo.getDisplayName());
        setWindowSize( new Dimension(300,300));
        setPosition(new Point(100, 100));
        Point currentPosition = getPosition();
        maximize(); // Tests zooming
        setZoomLevelOfCurrentPage(30);
        setZoomLevelOfCurrentPage(100);
        setZoomLevelOfCurrentPage(200);
        setZoomLevelOfCurrentPage(30);
        
        setPosition( currentPosition);

        Assertions.assertEquals( currentPosition,  getPosition());
        fullscreen();
    }
    @Test
    @DisplayName("Test Web Elements operations")
    public void WebElementTest(TestInfo testInfo) throws Exception {
        setup(testInfo.getDisplayName());
        get( "http://the-internet.herokuapp.com/inputs");
        waitForPageToLoad();
        WebElement input = findElement(By.tagName("input"));
        input.sendKeys("99");
        input.click();
        String value = input.getText();
        println("Value= "+value);
        Dimension dimension = input.getSize();
        println("Dimension= "+dimension);
        input.clear();
    }
    @Test
    @Disabled("Pending for Selenium 4 and DevTools")
    @DisplayName("Test physical location")
    public void LocationTest(TestInfo testInfo) throws Exception {
        ClearConsole();
        SetDebugModeON();
        setup(testInfo.getDisplayName());
        get( "https://www.amazon.com/");
        WebElement delivery = findElement(By.cssSelector("#glow-ingress-line2"));
        setDeviceLocation( -33.0081 , -71.5197, 0);

        println("Current location="+ getText(delivery));
        setDeviceLocation( 51.507359 , -0.136439, 0);
        get( "https://www.amazon.com/");
        delivery = findElement(By.cssSelector("#glow-ingress-line2"));
        println("London location="+getText(delivery));
    }

}
