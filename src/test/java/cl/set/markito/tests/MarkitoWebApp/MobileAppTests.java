package cl.set.markito.tests.MarkitoWebApp;

import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import cl.set.markito.framework.MarkitoWebApp;
import cl.set.markito.framework.browsers.*;
import cl.set.markito.framework.devices.*;
import io.appium.java_client.appmanagement.ApplicationState;
/**
 * This is an internal test class to verify implementation.  It is not intended to test Selenium or Appium.
 */
public class MobileAppTests extends MarkitoWebApp {
    /**
     * A Android and iOS test, using Appium clients to manage apps. 
     * @param browser
     * @param device
     * @throws Exception
     */
    @ParameterizedTest
    @MethodSource("webScenarios")
    @DisplayName("Manage Wikipedia App Test")
    void manageWikipediaApp(Browser browser, Device device) throws Exception {
        // Arrange
        setBrowserstackProjectInformation("Markito", "Markito "+getMarkitoVersion(), 
                                            "Manage Wikipedia App Test-"+browser.getName()+"-"+device.getName());
        String wikipediaAppInBs="";

        if ( device.isAndroid()) {
            wikipediaAppInBs = "bs://c700ce60cf13ae8ed97705a55b8e022f13c5827c";
        } else if ( device.isIOS() ) {
            wikipediaAppInBs = "bs://444bd0308813ae0dc236f8cd461c02d3afa7901d";
            getBrowserStack().SetAppUnderTesting( wikipediaAppInBs);

        } else Assertions.assertTrue(false, "OS not supported.");
        getBrowserStack().SetAppUnderTesting(wikipediaAppInBs);
        // Act: Open session and manage app
        setDriver(openBrowserSessionInDevice(browser, device)); // Open web session on device
        Thread.sleep(10000);
        if ( device.isAndroid()){ // Not working on iOS
            Assertions.assertTrue( isAppInstalled("org.wikipedia.alpha"), ANSI_RED+"ERROR: App is not installed.");
            ApplicationState wikipediaState = queryAppState("org.wikipedia.alpha");
            println(ANSI_YELLOW+"Wikipedia state is "+wikipediaState);
        }

        launchApp();

        // Get screenshot as InstallAppTest-20230111T171104751078.png on TestResults folder.
        getScreenSnapshotWithDate("ManageWikipediaTest-"+device.getName()+"-"+device.getPlatform()); 

        // Assert 

    }
    @AfterEach
    void tearDown() throws Exception {
        closeWebSessionInDevice();
    }

     /**
     * Provides test scenarios for mobile app testing.
     */
    private static Stream<Arguments> webScenarios() {
        return Stream.of(

            Arguments.of(SAFARI_BROWSER, IPHONE11PRO_DEVICE),
            Arguments.of(FIREFOX_BROWSER, GOOGLEPIXEL3_DEVICE)

        );
    }
}
