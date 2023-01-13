package cl.set.markito.tests.MarkitoWeb;

import java.util.List;
import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.*;

import cl.set.markito.framework.MarkitoWebDriver;
import cl.set.markito.framework.browsers.*;
import cl.set.markito.framework.devices.*;

public class MultiBrowserTests extends MarkitoWebDriver {
    
    @ParameterizedTest
    @MethodSource("webScenarios")
    void GoogleSearchTest(Browser browser, Device device) throws Exception {
        
        // Arrange
        setAutomaticDriverDownload(true);
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
    }
    @AfterEach
    void tearDown() throws Exception {
        closeWebSessionInDevice();
    }

     /**
     * Provides test scenarios for multi-browser and multi-platform testing.
     * 
     * @return
     */
    private static Stream<Arguments> webScenarios() {
        return Stream.of(
            Arguments.of(CHROME_BROWSER, LOCAL_COMPUTER_DEVICE),
            //Arguments.of(FIREFOX_BROWSER, LOCAL_COMPUTER_DEVICE),
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
