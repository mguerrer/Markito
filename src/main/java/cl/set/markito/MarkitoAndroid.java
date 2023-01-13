/** Markito Android main class.
* Marcos Guerrero
* 16-12-2020
**/
package cl.set.markito;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.html5.Location;
import org.openqa.selenium.interactions.touch.TouchActions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;


public class MarkitoAndroid extends MarkitoWeb {
    public AndroidDriver<AndroidElement> adriver = null;
    public long timeOutInSeconds = 60;

    /**
     * Launch ANDROID App selected in capabilities.
     */
    public void LaunchApp() {
        printf(ANSI_YELLOW + "Android LaunchApp...");
        try {
            AndroidDriver<AndroidElement> adriver = (AndroidDriver<AndroidElement>) driver;
            adriver.launchApp();
            printf(ANSI_YELLOW + "done!\n");
        } catch (Exception e) {
            printf(ANSI_RED + "failed! %s\n", e.getMessage());
            throw new WebDriverException(e.getMessage());
        }
    }

    /**
     * Reset ANDROID App selected in capabilities.
     */
    public void ResetApp() {
        printf(ANSI_YELLOW + "Android ResetApp...");
        try {
            AndroidDriver<AndroidElement> adriver = (AndroidDriver<AndroidElement>) driver;
            adriver.resetApp();
            adriver.installApp(ANSI_BLACK);
            printf(ANSI_YELLOW + "done!\n");
        } catch (Exception e) {
            printf(ANSI_RED + "failed! %s\n", e.getMessage());
            throw new WebDriverException(e.getMessage());
        }
    }

    /**
     * Close ANDROID App selected in capabilities.
     */
    public void CloseApp() {
        printf(ANSI_YELLOW + "Android CloseApp...");
        try {
            AndroidDriver<AndroidElement> adriver = (AndroidDriver<AndroidElement>) driver;
            adriver.closeApp();
            printf(ANSI_YELLOW + "done!\n");
        } catch (Exception e) {
            printf(ANSI_RED + "failed! %s\n", e.getMessage());
            throw new WebDriverException(e.getMessage());
        }
    }

    /**
     * Default constructor indicates to webdriver that platform is ANDROID.
     */
    public MarkitoAndroid() {
        printf(ANSI_YELLOW + "Markito AndroidDriver has born.\n");

    }

    /**
     * Simulates typing Keys over an editable element located By.
     * 
     * @param locator:   Element locator.
     * @param keys: Array of keys to be sent.
     */
    @Override
    public void sendKeys(By locator, String keys) {
        printf(ANSI_YELLOW + "Android SendKeys %s ...", locator);
        try {
            AndroidElement element = findElement(locator);
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
     * Initialise the remote Webdriver using an Appium remote URL and desired
     * capabilities defined above
     */
    public void OpenAndroidDriver(URL UrlAppiumServer, MutableCapabilities caps) {
        printf("Opening Android Session in appium server at %s on device %s", UrlAppiumServer, caps.getCapability("device"));
        try {
            driver = new AndroidDriver<AndroidElement>(UrlAppiumServer, caps);
            if (driver == null) {
                println(ANSI_RED + "\nMarkito: ERROR on getting Android session, value is null.\n");
                System.exit(-1);
            }
            js = (JavascriptExecutor) driver;
        } catch (Exception e) {
            printf(ANSI_RED
                    + "\nMarkito: ERROR on getting Android session. Is there an Appium server at %s?\nStack: %s\n",
                    UrlAppiumServer, e.getMessage());
            System.exit(-1);
        }
    }

    /**
     * Close current webdriver session and collects possible garbage.
     */
    public void CloseAndroidDriver() {
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
     * @return AndroidElement
     */
    public AndroidElement findElement(By by) {
        printf(ANSI_YELLOW + "Android FindElement %s ...", by);
        try {
            AndroidElement element = (AndroidElement) driver.findElement(by);
            printf(ANSI_YELLOW + "found...");
            return element;
        } catch (Exception e) {
            printf(ANSI_RED + "ERROR on finding element %s. \nStack: %s", by, e.getMessage());
            LocalDateTime ldt = LocalDateTime.now();
            String date = ldt.toString();
            try {
                takeScreenSnapshot("TestResults\\FindERROR-" + date.toString().replaceAll("\\W+", "") + ".png");
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
    public List<WebElement> findElements(By by) {
        printf(ANSI_YELLOW + "Android FindElements %s ...", by);
        List<WebElement> elements;
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
     * Establish a unified timeout parameter for implicit waits, page loads and
     * javascripts.
     * 
     * @param timeOutInSeconds: Number of seconds to wait before produce
     *                          interruption.
     */
    @Override
    public void setTimeouts(long timeOutInSeconds) {
        this.timeOutInSeconds = timeOutInSeconds;
        driver.manage().timeouts().implicitlyWait(timeOutInSeconds, TimeUnit.SECONDS); // Timeouts de waitFor*
        printf(ANSI_YELLOW + "SetTimeouts in %d seconds.\n", timeOutInSeconds);
    }

    /**
     * Takes an screen snapshot and saves to a file.
     * 
     * @param fileWithPath: Pathname of the file to be generated.
     */
    @Override
    public void takeScreenSnapshot(String fileWithPath) {
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
    @Override
    public void click(By locator) {
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
    @Override
    public String getText(By by) {
        printf(ANSI_YELLOW + "GetText in object %s\n", by);
        try {
            WebElement element = findElement(by);
            return element.getText();
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
    @Override
    public void waitForElementVisible(By by) {
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
     * Waits for an element to be visible.
     * 
     * @param by
     */
    public void WaitForElementClickable(By by) {
        printf(ANSI_YELLOW + "Waiting for element clickeable %s...", by.toString());
        try {
            new WebDriverWait(driver, timeOutInSeconds).ignoring(StaleElementReferenceException.class)
                    .ignoring(WebDriverException.class).until(ExpectedConditions.elementToBeClickable(by));
            printf(ANSI_YELLOW + "visible!!!\n");
        } catch (Exception e) {
            printf(ANSI_RED + "failed!!! %s\n", e.getMessage());
            throw new WebDriverException(e.getMessage());
        }
    }

    /**
     * Get contexts for Hybrid Apps.
     * 
     * @return ContextNames: Obtained contexts from Android driver.
     */
    public Set<String> GetContextHandles() {
        adriver = (AndroidDriver<AndroidElement>) driver;
        Set<String> contextNames = adriver.getContextHandles();
        for (String contextName : contextNames) {
            println(ANSI_YELLOW + contextName); // prints out something like NATIVE_APP \n WEBVIEW_1
        }
        return contextNames;
    }

    public void SetContextHandle(String ContextName) {
        println(ANSI_YELLOW + "SetContextHandle " + ContextName); // prints out something like NATIVE_APP \n WEBVIEW_1
        adriver = (AndroidDriver<AndroidElement>) driver;
        adriver.context(ContextName);
        driver = adriver;
    }

    public void Tap(By locator) {
        printf(ANSI_YELLOW + "Tapping element %s...", locator.toString());
        try {
            new WebDriverWait(driver, timeOutInSeconds).ignoring(StaleElementReferenceException.class)
                    .ignoring(WebDriverException.class).until(ExpectedConditions.visibilityOfElementLocated(locator));
            adriver = (AndroidDriver<AndroidElement>) driver;
            TouchActions action = new TouchActions( adriver);
            action.singleTap(adriver.findElement(locator));
            action.perform();
            printf(ANSI_YELLOW + "tapped!!!\n");
        } catch (Exception e) {
            printf(ANSI_RED + "failed!!! %s\n", e.getMessage());
            throw new WebDriverException(e.getMessage());
        }

    }
    @Override
    public void setLocation( double x, double y, double z) {
        printf(ANSI_YELLOW + "Set location to %f, %f, %f...", x,y,z);
        try {
            adriver = (AndroidDriver<AndroidElement>) driver;
            adriver.setLocation(new Location(x, y, z));

            printf(ANSI_YELLOW + "done!!!\n");
        } catch (Exception e) {
            printf(ANSI_RED + "failed!!! %s\n", e.getMessage());
            throw new WebDriverException(e.getMessage());
        }
    }
}