// Markito iOS main class.
// Marcos Guerrero
// 20-10-2020
package cl.set.markito;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSElement;

@Deprecated
public class MarkitoiOS extends MarkitoBaseUtils{
    public  IOSDriver<IOSElement> driver = null;
    public long timeOutInSeconds = 60;

    /**
     * Default constructor indicates to Markito Webdriver that platform is ANDROID.
     */
    public MarkitoiOS() {
    }
    /**
     * Initialise the remote Webdriver using an Appium remote URL and desired
     * capabilities defined above
     */
    public void OpeniOSDriver(URL UrlAppiumServer, DesiredCapabilities caps) {
        printf("Opening iOS Session in appium URL %s in device %s...", UrlAppiumServer, caps.getCapability("device"));
        try {
            driver = new IOSDriver<IOSElement>( UrlAppiumServer, caps);
            if ( this.driver == null )
            {
                println(ANSI_RED+"\nERROR on getting iOS session, value is null.");
                System.exit(-1);                
            }
        } catch (Exception e) {
            printf(ANSI_RED+"\nERROR on getting iOS session. \nStack: %s", e.getMessage());
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
    /**
     * Simulates typing Keys over an editable element located By.
     * 
     * @param locator:   Element locator.
     * @param keys: Array of keys to be sent.
     */
    //@Override
    public void SendKeys(By locator, String keys) {
        printf(ANSI_YELLOW + "Android SendKeys %s ...", locator);
        try {
            IOSElement element = FindElement(locator);
            new WebDriverWait(driver, timeOutInSeconds).ignoring(StaleElementReferenceException.class)
                    .ignoring(WebDriverException.class).until(ExpectedConditions.elementToBeClickable(locator));
            element.sendKeys(keys);
            println("done!");
        } catch (Exception e) {
            printf(ANSI_RED + "failed! %s\n", e.getMessage());
            throw new WebDriverException(e.getMessage());
        }
    }
        /**
     * Find an element in DOM using By locator. When debug mode is ON highlights the
     * element to help visual debug.
     * 
     * @param by
     * @return AndroidElement
     */
    public IOSElement FindElement(By by) {
        printf(ANSI_YELLOW + "Android FindElement %s ...", by);
        try {
            IOSElement element = (IOSElement) driver.findElement(by);
            printf(ANSI_YELLOW + "found...");
            return element;
        } catch (Exception e) {
            printf(ANSI_RED + "ERROR on finding element %s. \nStack: %s", by, e.getMessage());
            LocalDateTime ldt = LocalDateTime.now();
            String date = ldt.toString();
            try {
                TakeScreenSnapshot("TestResults\\FindERROR-" + date.toString().replaceAll("\\W+", "") + ".png");
            } catch (Exception e2) {
                printf("ERROR al tomar snapshot. Stack:%s\n", e2.getMessage());
            }
            throw new WebDriverException(e.getMessage());
        }
    }

    /**
     * Find elements in DOM using By locator. When debug mode is ON highlights the
     * element to help visual debug.
     * 
     * @param by
     * @return WebElement
     */
    public List<IOSElement> FindElements(By by) {
        printf(ANSI_YELLOW + "Android FindElements %s ...", by);
        List<IOSElement> elements;
        try {
            elements = driver.findElements(by);
            printf(ANSI_YELLOW + "found...\n");
            return elements;
        } catch (Exception e) {
            printf(ANSI_RED + "ERROR on finding elements %s. \nStack: %s", by, e.getMessage());
            throw new WebDriverException(e.getMessage());
        }
    }
    
    /**
     * Takes an screen snapshot and saves to a file.
     * 
     * @param fileWithPath: Pathname of the file to be generated.
     */
    //@Override
    public void TakeScreenSnapshot(String fileWithPath) {
        try {
            // Convert web driver object to TakeScreenshot
            TakesScreenshot scrShot = ((TakesScreenshot) driver);
            // Call getScreenshotAs method to create image file
            File SrcFile = scrShot.getScreenshotAs(OutputType.FILE);
            // Move image file to new destination
            File DestFile = new File(fileWithPath);
            // Copy file at destination
            FileUtils.copyFile(SrcFile, DestFile);
        } catch (IOException e) {
            System.out.println(ANSI_RED + "TakeScreenSnapshot:" + e.getMessage());
        }
    }
    /**
     * Click in a clickable element located using By.
     * 
     * @param locator
     */
    //@Override
    public void Click(By locator) {
        printf(ANSI_YELLOW + "Clicking (And) %s...", locator);
        try {
            driver.findElement(locator).click();
            printf(ANSI_YELLOW + "done.\n", locator);
        } catch (Exception e) {
            printf(ANSI_RED + "failed!!! %s\n", e.getMessage());
            throw new WebDriverException(e.getMessage());
        }
    }

    /**
     * GetText of an element located by.
     * 
     * @param by
     */
    //@Override
    public String GetText(By by) {
        printf(ANSI_YELLOW + "GetText in object %s\n", by);
        try {
            WebElement element = FindElement(by);
            return element.getText();
        } catch (Exception e) {
            printf(ANSI_RED + "failed!!! %s\n", e.getMessage());
            throw new WebDriverException(e.getMessage());
        }
    }

}