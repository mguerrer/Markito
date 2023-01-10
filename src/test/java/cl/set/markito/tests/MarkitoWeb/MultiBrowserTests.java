package cl.set.markito.tests.MarkitoWeb;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import cl.set.markito.MarkitoWeb;
import cl.set.markito.browsers.*;
import cl.set.markito.devices.*;

public class MultiBrowserTests extends MarkitoWeb {
    @ParameterizedTest
    @MethodSource("webScenarios")
    void MultiBrowserTest(Browser browser, Device device) throws Exception {

        setDriver( OpenBrowserSessionInDevice( browser, device) );
        get("https://www.google.cl");
        close();

    }

    private WebDriver OpenBrowserSessionInDevice(Browser browser, Device device) throws Exception {
        WebDriver driver;
        printf(ANSI_YELLOW+"Creating Markito WEB session on  " + browser.getName() + " on device " +  device.getName() + " " + device.getOS());
        try {
            if ( device.equals(LOCAL_COMPUTER_DEVICE)) {
                driver = new ChromeDriver();
            } else {
                driver = new ChromeDriver();
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
