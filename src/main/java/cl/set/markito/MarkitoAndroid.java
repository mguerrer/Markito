// Markito Android main class.
// Marcos Guerrero
// 20-10-2020
package cl.set.markito;

import java.net.URL;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.remote.DesiredCapabilities;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;


public class MarkitoAndroid extends MarkitoWebdriver {
    //public  AndroidDriver<AndroidElement> driver = null;

    /**
     * Default constructor indicates to webdriver that platform is ANDROID.
     */
    public MarkitoAndroid() {
        platform = ANDROID;
    }
    /**
     * Initialise the remote Webdriver using an Appium remote URL and desired
     * capabilities defined above
     */
    public void OpenAndroidDriver(URL UrlAppiumServer, DesiredCapabilities caps) {
        printf("Opening Android Session in %s with desired %s", UrlAppiumServer, caps);
        try {
            this.driver = new AndroidDriver<AndroidElement>(UrlAppiumServer, caps);
            if ( this.driver == null )
            {
                println("Markito: ERROR on getting Android session, value is null.\n");
                System.exit(-1);                
            }
        } catch (Exception e) {
            printf("Markito: ERROR on getting Android session. \nStack: %s", this.driver);
            System.exit(-1);
        }
        js = (JavascriptExecutor) this.driver;
    }
    /**
     * Close current webdriver session and collects possible garbage.
     */
    public void CloseAndroidDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
            println("Markito is destroyed.");
        }
    }
    /** 
     * Find an element in DOM using By locator.  When debug mode is ON highlights the element to help visual debug.
     * @param by
     * @return WebElement
     */
    public AndroidElement FindElement(By by) {
        AndroidElement element = (AndroidElement) ((AndroidDriver<WebElement>) driver).findElement(by);
        HighLightElement(element);
        return element;
    }    
    /** 
    * Find elements in DOM using By locator.  When debug mode is ON highlights the element to help visual debug.
    * @param by
    * @return WebElement
    */
    public List <WebElement> FindElements(By by) {
        AndroidDriver<WebElement> AndroidDriver = ((AndroidDriver<WebElement>) driver);
        List<WebElement> WebElements = AndroidDriver.findElements(by);
        WebElements.forEach(element -> 
        { 
            HighLightElement(element);
        } ); // Highlights on debug mode.
        return WebElements;
    }
}