// Markito iOS main class.
// Marcos Guerrero
// 20-10-2020
package cl.set.markito;

import java.net.URL;

import org.openqa.selenium.remote.DesiredCapabilities;

import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSElement;


public class MarkitoiOS extends MarkitoWebdriver{
    //public  IOSDriver<IOSElement> driver = null;

    /**
     * Default constructor indicates to Markito Webdriver that platform is ANDROID.
     */
    public MarkitoiOS() {
        platform = ANDROID;
    }
    /**
     * Initialise the remote Webdriver using an Appium remote URL and desired
     * capabilities defined above
     */
    public void OpeniOSDriver(URL UrlAppiumServer, DesiredCapabilities caps) {
        printf("Opening iOS Session in %s with desired %s", UrlAppiumServer, caps);
        try {
            driver = new IOSDriver<IOSElement>( UrlAppiumServer, caps);
            if ( this.driver == null )
            {
                println("Markito: ERROR on getting Android session, value is null.");
                System.exit(-1);                
            }
        } catch (Exception e) {
            printf("Markito: ERROR on getting Android session. \nStack: %s", this.driver);
            System.exit(-1);
        }        
    }
    /**
     * Close current webdriver session and collects possible garbage.
     */
    public void CloseiOSDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
            println("Markito is destroyed.");
        }
    }
}