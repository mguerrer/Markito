package cl.set.markito.tests.AndroidAndIOS;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import cl.set.markito.BrowserStack;

import org.junit.jupiter.api.Assertions;
import io.appium.java_client.MobileDriver;
import io.appium.java_client.MobileElement;


public class SampleAppTest extends BrowserStack{
    @DisplayName("Click on home page text button and read test message.")
    @ParameterizedTest
    @ValueSource(strings = { "iOS", "android"})
    void SampleAppTestOnMobile( String platform ){
        // Arrange
        MobileDriver<MobileElement> driver;
        testSetup(platform);
        try {
            driver = openBrowserStackMobileSession();
            Assertions.assertNotEquals( null, driver, "Can not open Appium server."  );

            HomePage home = new HomePage(driver);
    
            // Act
            home.clickTestBrowserStackLocalButton();
            String msg = home.getTestResultMessage();
            driver.quit();    
            // Assert
            if ( platform.contains("iOS")){
                Assertions.assertTrue(msg.contains("Error in connecting bs-local"));
            } else {
                Assertions.assertTrue(msg.contains("Unable to connect."));
            }
    

        } catch (Exception e) {
            e.printStackTrace();
        }
        

    }

    private void testSetup(String platform) {
        switch (platform) {
            case "android":
                setDesiredTechnicalCapabilities( "bs://080efa5ba6f9faed39f0532610da865b5618cffa", null,
                                                "Google Pixel 3", 
                                                 platform, "9.0");
            break;
            case "iOS":
                setDesiredTechnicalCapabilities( "bs://bdf179936842c3e5113e8f39aae3ccb5a20c85f9", null,
                                                "iPhone 11 Pro", 
                                                platform, "15");
            break;
        }
        SetProjectInformation("Test BrowserStack Sample App", platform, "Run on "+platform);
    }

}
