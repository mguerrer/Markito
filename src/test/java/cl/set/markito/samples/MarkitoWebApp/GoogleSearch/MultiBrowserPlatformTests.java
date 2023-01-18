package cl.set.markito.samples.MarkitoWebApp.GoogleSearch;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.*;

import cl.set.markito.framework.MarkitoWebApp;
import cl.set.markito.framework.browsers.*;
import cl.set.markito.framework.devices.*;
import cl.set.markito.samples.MarkitoWebApp.GoogleSearch.pages.GoogleSearchHomePage;
import cl.set.markito.samples.MarkitoWebApp.GoogleSearch.pages.GoogleSearchResultsPage;

/**
 * Tests to demonstrate Markito capability to run tests for web app on different scenarios.
 */
public class MultiBrowserPlatformTests extends MarkitoWebApp {
    /**
     * Very simple "Hello world" style of Google's search on your local computer using Chrome."
     */
    @Test
    public void HelloWorldTest() throws Exception {
        // Arrange
        setBrowserstackProjectInformation("Markito", "Markito "+getMarkitoVersion(), 
                                            "Google Search Hello world-Chrome"+"-"+LOCAL_COMPUTER_DEVICE.getName());
        setAutomaticDriverDownload(true); // Adds automatic driver download on local machine
        setDriver(openBrowserSessionInDevice(CHROME_BROWSER, LOCAL_COMPUTER_DEVICE)); // Open web session on device

        // Act
        get("http://www.google.com");
        maximize();
        
        // Search "Hello world!!" string
        sendKeys( By.name("q"), "Hello world!!");
        click( By.name("btnK"));

        // Show all texts found in first page.
        List<WebElement>results = findElements(By.tagName("h3"));
        for (WebElement webElement : results) {
            println(webElement.getText());
        }
        closeWebSessionInDevice();
    }
    /**
     * A multi-browser and multi platform test (on BrowserStack) for Google search, using Selenium and Appium clients together. 
     * @param browser
     * @param device
     * @throws Exception
     */
    @ParameterizedTest
    @MethodSource("webScenarios")
    void GoogleSearchTest(Browser browser, Device device) throws Exception {
        // Arrange
        setBrowserstackProjectInformation("Markito", "Markito "+getMarkitoVersion(), 
                                            "Google Search-"+browser.getName()+"-"+device.getName());
        setAutomaticDriverDownload(true); // Adds automatic driver download on local machine
        setDriver(openBrowserSessionInDevice(browser, device)); // Open web session on device
        GoogleSearchHomePage searchPage = new GoogleSearchHomePage( getDriver());
        get("https://www.google.cl");
        if (isIOS() || isAndroid()) {
            rotate(ScreenOrientation.LANDSCAPE);
            //rotate(new DeviceRotation(0, 0, 270));
        } else
            maximize();

        // Act: This search is not working on IOS https://discuss.appium.io/t/sendkeys-and-click-function-does-not-work-for-ios-simulator/5896
        sendKeys( searchPage.queryTextBox, "Markito" + Keys.ENTER );


        // Get screenshot as GoogleSearchTest-20230111T171104751078.png on TestResults folder.
        getScreenSnapshotWithDate("GoogleSearchTest"); 

        // Print found titles
        GoogleSearchResultsPage resultsPage = new GoogleSearchResultsPage( getDriver());
        for (WebElement result : resultsPage.queryResults) {
            getText( result );
        }
        // Assert 
        if ( !isIOS())
            Assertions.assertTrue( resultsPage.queryResults.size() > 0);
    }
    @AfterEach
    void tearDown() throws Exception {
        closeWebSessionInDevice();
    }

     /**
     * Provides test scenarios for multi-browser and multi-platform testing.
     */
    private static Stream<Arguments> webScenarios() {
        return Stream.of(
            Arguments.of(CHROME_BROWSER, LOCAL_COMPUTER_DEVICE),
            //Arguments.of(FIREFOX_BROWSER, LOCAL_COMPUTER_DEVICE),
            Arguments.of(IE_BROWSER, LOCAL_COMPUTER_DEVICE),
            Arguments.of(EDGE_BROWSER, LOCAL_COMPUTER_DEVICE),

            Arguments.of(CHROME_BROWSER, WINDOWS10_COMPUTER_DEVICE),
            Arguments.of(FIREFOX_BROWSER, WINDOWS10_COMPUTER_DEVICE),
            Arguments.of(IE_BROWSER, WINDOWS10_COMPUTER_DEVICE),
            Arguments.of(EDGE_BROWSER, WINDOWS10_COMPUTER_DEVICE),

            Arguments.of(CHROME_BROWSER, WINDOWS11_COMPUTER_DEVICE),
            Arguments.of(FIREFOX_BROWSER, WINDOWS11_COMPUTER_DEVICE),
            Arguments.of(EDGE_BROWSER, WINDOWS11_COMPUTER_DEVICE),

            Arguments.of(CHROME_BROWSER, MAC_VENTURA_COMPUTER_DEVICE),
            Arguments.of(FIREFOX_BROWSER, MAC_VENTURA_COMPUTER_DEVICE),
            Arguments.of(SAFARI_BROWSER, MAC_VENTURA_COMPUTER_DEVICE),
            Arguments.of(EDGE_BROWSER, MAC_VENTURA_COMPUTER_DEVICE),

            Arguments.of(SAFARI_BROWSER, IPHONE11PRO_DEVICE),
            Arguments.of(CHROME_BROWSER, GOOGLEPIXEL3_DEVICE),
            Arguments.of(FIREFOX_BROWSER, GOOGLEPIXEL3_DEVICE)

        );
    }
}
