/** Markito Mobile main class.
* Marcos Guerrero
* 16-12-2020
**/
package cl.set.markito;

import java.io.File;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileDriver;
import io.appium.java_client.MobileElement;

public abstract class MarkitoMobile extends MarkitoBaseUtils {
    public MobileDriver<MobileElement> driver = null;
    public long timeOutInSeconds = 60;

    
    /**
     * Default constructor indicates to webdriver that platform is Mobile.
     */
    public MarkitoMobile() {
        printf(ANSI_YELLOW + "Markito MobileDriver has born.\n");
    }

    public MobileDriver<MobileElement> getDriver() {
        return driver;
    }

    public void setDriver(MobileDriver<MobileElement> driver) {
        this.driver = driver;
    }
    @SuppressWarnings("unchecked")
    public void setDriver(WebDriver driver) {
        this.driver = (MobileDriver<MobileElement>) driver;
    }

    public long getTimeOutInSeconds() {
        return timeOutInSeconds;
    }

    public void setTimeOutInSeconds(long timeOutInSeconds) {
        this.timeOutInSeconds = timeOutInSeconds;
    }


    /**
     * Launch Mobile App selected in capabilities.
     */
    public void LaunchApp() {
        printf(ANSI_YELLOW + "Mobile LaunchApp...");
        try {
            driver.launchApp();
            printf( ANSI_YELLOW + "done!\n");
        } catch (Exception e) {
            printf(ANSI_RED + "failed! %s\n", e.getMessage());
            throw new WebDriverException(e.getMessage());
        }
    }

    /**
     * Close Mobile App selected in capabilities.
     */
    public void CloseApp() {
        printf(ANSI_YELLOW + "Mobile CloseApp...");
        try {
            driver.closeApp();
            printf( ANSI_YELLOW + "done!\n");
        } catch (Exception e) {
            printf(ANSI_RED + "failed! %s\n", e.getMessage());
            throw new WebDriverException(e.getMessage());
        }
    }

    /**
     * Simulates typing Keys over an editable element located By.
     * 
     * @param by:   Element locator.
     * @param keys: Array of keys to be sent.
     */
    public void SendKeys(By by, String keys) {
        printf(ANSI_YELLOW + "Mobile SendKeys %s ...", by);
        try {
            MobileElement element = driver.findElement(by);
            element.sendKeys(keys);
            println("done!");
        } catch (Exception e) {
            printf(ANSI_RED + "failed! %s\n", e.getMessage());
            throw new WebDriverException(e.getMessage());
        }
    }

    /**
     * Initialise the remote Webdriver using an Appium remote URL and desired
     * capabilities defined above
     */
    public void OpenMobileDriver(URL UrlAppiumServer, MutableCapabilities caps) {
        printf("Opening Mobile Session in %s with desired %s", UrlAppiumServer, caps);
        try {
            driver = new AppiumDriver<MobileElement>(UrlAppiumServer, caps);
            if (driver == null) {
                println(ANSI_RED + "\nMarkito: ERROR on getting Mobile session, value is null.\n");
                System.exit(-1);
            }
        } catch (Exception e) {
            printf(ANSI_RED + "\nMarkito: ERROR on getting Mobile session. Is there an Appium server at %s?\nStack: %s\n", UrlAppiumServer, e.getMessage());
            System.exit(-1);
        }
    }

    /**
     * Close current webdriver session and collects possible garbage.
     */
    public void CloseMobileDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
            println(ANSI_YELLOW + "Markito is destroyed.");
        }
    }

    /**
     * Find an element in DOM using By locator. When debug mode is ON highlights the
     * element to help visual debug.
     * 
     * @param by
     * @return MobileElement
     */
    public MobileElement FindElement(By by) {
        printf(ANSI_YELLOW + "Mobile FindElement %s ...", by);
        try {
            MobileElement element = (MobileElement) driver.findElement(by);
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
    public List<MobileElement> FindElements(By by) {
        printf(ANSI_YELLOW + "Mobile FindElements %s ...", by);
        List<MobileElement> elements;
        try {
            elements = driver.findElements(by);
            printf(ANSI_YELLOW + "found...");
            return elements;
        } catch (Exception e) {
            printf(ANSI_RED + "ERROR on finding elements %s. \nStack: %s", by, e.getMessage());
            throw new WebDriverException(e.getMessage());
        }
    }

    /**
     * Establish a unified timeout parameter for implicit waits, page loads and
     * javascripts.
     * 
     * @param timeOutInSeconds: Number of seconds to wait before produce
     *                          interruption.
     */
    public void SetTimeouts(long timeOutInSeconds) {
        this.timeOutInSeconds = timeOutInSeconds;
        driver.manage().timeouts().implicitlyWait(timeOutInSeconds, TimeUnit.SECONDS); // Timeouts de waitFor*
        printf(ANSI_YELLOW + "SetTimeouts in %d seconds.\n", timeOutInSeconds);
    }

    /**
     * Takes an screen snapshot and saves to a file.
     * @param fileWithPath: Pathname of the file to be generated.
     */
    public void TakeScreenSnapshot(String fileWithPath) throws Exception {
        // Convert web driver object to TakeScreenshot
        TakesScreenshot scrShot = ((TakesScreenshot) driver);
        // Call getScreenshotAs method to create image file
        File SrcFile = scrShot.getScreenshotAs(OutputType.FILE);
        // Move image file to new destination
        File DestFile = new File(fileWithPath);
        // Copy file at destination
        FileUtils.copyFile(SrcFile, DestFile);
    }

    /**
     * Click in a clickable element located using By.
     * 
     * @param locator
     */
    public void Click(By locator) {
        printf(ANSI_YELLOW + "Clicking %s...", locator);
        try {
            List<MobileElement> elements = driver.findElements(locator);
            if ( (elements!=null) && (elements.size()>1) ){
                printf(ANSI_YELLOW_BACKGROUND+"WARNING: There is more than one element found by %s.\n", locator);
            }
            if ( elements != null) {
                elements.get(0).click();
                printf(ANSI_YELLOW + "done.\n", locator);
            } else {
                throw new WebDriverException("ERROR: Cannot find element "+locator);
               
            }

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
    public String GetText(By by) {
        printf(ANSI_YELLOW + "GetText in object %s\n", by);
        try {
            String text = new WebDriverWait(driver, timeOutInSeconds).until(ExpectedConditions.elementToBeClickable(by))
                    .getText();
            return text;
        } catch (Exception e) {
            printf(ANSI_RED + "failed!!! %s\n", e.getMessage());
            throw new WebDriverException(e.getMessage());
        }
    }

    /**
     * Waits for an element to be visible.
     * 
     * @param by
     */
    public void WaitForElementVisible(By by) {
        printf(ANSI_YELLOW + "Waiting for element %s...", by.toString());
        try {
            new WebDriverWait(driver, timeOutInSeconds).ignoring(StaleElementReferenceException.class)
                    .ignoring(WebDriverException.class).until(ExpectedConditions.visibilityOfElementLocated(by));
            printf(ANSI_YELLOW + "visible!!!\n");
        } catch (Exception e) {
            printf(ANSI_RED + "failed!!! %s\n", e.getMessage());
            throw new WebDriverException(e.getMessage());
        }
    }    
    /**
    * Waits for an element to be visible and not fail if not found on timeout, instead it will return true or false.
    * @param locator
    * @param timeout max time to wait for element to be visible.
    */
    public boolean WaitForElementVisible(By locator, long timeout) {
       printf( ANSI_YELLOW+"Waiting for element %s on %d seconds...", locator.toString(), timeout);
       try {
           new WebDriverWait(driver, timeout).ignoring(StaleElementReferenceException.class)
                   .ignoring(WebDriverException.class).until(ExpectedConditions.visibilityOfElementLocated(locator));
           printf( ANSI_YELLOW+"visible!!!\n");
           return true;
       } catch (Exception e) {
           printf( ANSI_RED+"failed!!! %s\n", e.getMessage());
           return false;
       }
   }
    /**
     * Get contexts for Hybrid Apps.
     * @return ContextNames: Obtained contexts from Mobile driver.
     */
    public Set<String> GetContextHandles(){ 
        Set<String> contextNames = driver.getContextHandles();
        for (String contextName : contextNames) {
            println(ANSI_YELLOW+contextName); //prints out something like NATIVE_APP \n WEBVIEW_1
        }
        return contextNames;
    }
    /**
     * Establish the context to find elements on hybrid apps.
     * @param ContextName
     */
    public void SetContextHandle( String ContextName ){ 
        println(ANSI_YELLOW+"SetContextHandle "+ContextName); //prints out something like NATIVE_APP \n WEBVIEW_1
        driver.context(ContextName);
    }
    
/*
    @Override
    public void get(String url) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public String getCurrentUrl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getTitle() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getPageSource() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void close() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void quit() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Set<String> getWindowHandles() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getWindowHandle() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TargetLocator switchTo() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Navigation navigate() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Options manage() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Response execute(String driverCommand, Map<String, ?> parameters) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Response execute(String driverCommand) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public WebDriver context(String name) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Set<String> getContextHandles() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getContext() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void rotate(ScreenOrientation orientation) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public ScreenOrientation getOrientation() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void rotate(DeviceRotation rotation) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public DeviceRotation rotation() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public MobileElement findElement(String by, String using) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<MobileElement> findElements(String by, String using) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Location location() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setLocation(Location location) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public List<MobileElement> findElements(By by) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public MobileElement findElement(By by) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public MobileElement findElementByClassName(String className) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<MobileElement> findElementsByClassName(String className) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public MobileElement findElementByCssSelector(String cssSelector) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<MobileElement> findElementsByCssSelector(String cssSelector) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public MobileElement findElementById(String id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<MobileElement> findElementsById(String id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public MobileElement findElementByLinkText(String linkText) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<MobileElement> findElementsByLinkText(String linkText) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public MobileElement findElementByPartialLinkText(String partialLinkText) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<MobileElement> findElementsByPartialLinkText(String partialLinkText) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public MobileElement findElementByName(String name) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<MobileElement> findElementsByName(String name) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public MobileElement findElementByTagName(String tagName) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<MobileElement> findElementsByTagName(String tagName) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public MobileElement findElementByXPath(String xPath) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<MobileElement> findElementsByXPath(String xPath) {
        // TODO Auto-generated method stub
        return null;
    }
    */
}