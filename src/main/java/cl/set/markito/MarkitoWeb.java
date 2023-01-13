// Markito web main class.
// Marcos Guerrero
// 20-10-2020
package cl.set.markito;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;

import io.github.bonigarcia.wdm.WebDriverManager;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.html5.Location;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * This is the legacy class to drive web test up to version 0.7.
 */
public class MarkitoWeb extends MarkitoBaseUtils implements WebDriver {
    public WebDriver driver = null;
    public JavascriptExecutor js;
    public long timeOutInSeconds = 60;

    public MarkitoWeb() {
        printf(ANSI_YELLOW + "Markito WebDriver created.\n");
    }

    public MarkitoWeb(WebDriver driverObject) {
        driver = driverObject;
        js = (JavascriptExecutor) driver;
    }

    /***
     * Default chrome constructor that can use headless execution mode.
     * 
     * @param headless
     */
    public WebDriver openChromeDriver() {
        println(ANSI_WHITE + "Verifying/updating Chrome driver.");
        WebDriverManager.chromedriver().setup();
        println("Opening Chrome session all by default.\n");
        driver = new ChromeDriver();
        js = (JavascriptExecutor) driver;
        setTimeouts(30);
        return driver;
    }

    /***
     * Default firefox constructor that can use headless execution mode.
     * 
     * @param headless
     */
    public void openFirefoxDriver(boolean headless) {
        if (headless) {
            FirefoxOptions options = new FirefoxOptions();
            printf("Opening Firefox session with desired %s\n", options);
            options.addArguments("-headless");
            driver = new FirefoxDriver(options);
        } else {
            println("Opening Firefox session all by default.\n");
            driver = new FirefoxDriver();
        }
        js = (JavascriptExecutor) driver;
        setTimeouts(30);
    }

    /**
     * A simple implementation of getting a webdriver server session for Internet
     * Explorer.
     * 
     * @return the driver session.
     */
    public WebDriver openInternetExplorerDriver() {
        return openInternetExplorerDriver("");
    }

    /**
     * A simple implementation of getting a webdriver server session for Internet
     * Explorer using an initial URL.
     * This is handy to improve performance.
     * 
     * @return the driver session.
     */
    public WebDriver openInternetExplorerDriver(String initialURL) {
        InternetExplorerOptions options = new InternetExplorerOptions();
        options.ignoreZoomSettings();
        options.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
        if (!((initialURL == null) || initialURL.equals("")))
            options.withInitialBrowserUrl(initialURL); // Esto ahorra segundos al inicio.
        setTimeouts(30);
        return openInternetExplorerDriver(options);
    }

    /**
     * A simple implementation of getting a webdriver server session for Internet
     * Explorer
     * using a more general approach of using InternetExplorerOptions.
     * 
     * @return the driver session.
     */
    public WebDriver openInternetExplorerDriver(InternetExplorerOptions options) {

        try {
            driver = new InternetExplorerDriver(options);
        } catch (Exception e) {
            println(ANSI_RED + "ERROR: Fallo en abrir InternetExplorerDriver.   Stack:" + e.getMessage());
        }
        setTimeouts(30);

        return driver;
    }

    /**
     * This has to be used prior to OpenInternetExplorerDriver to set non standard
     * configurations.
     * Markito assumes that IEDriverServer.exe is in PATH, with this call you can
     * change that, also debug level.
     * 
     * @param pathToIEDriverServerExe: Pathname for IEDriverServer.exe file.
     * @param debugLevel:              This indicates granularity of logs. Can be
     *                                 FATAL, ERROR, WARN, INFO, DEBUG, and TRACE.
     *                                 Defaults to FATAL.
     * @param pathToIEDriverServerLog: Specifies the full path and file name of the
     *                                 log file.
     *                                 http://engineermehedizaman.blogspot.com/2015/03/internet-explorer-driver-for-selenium.html
     */
    public void setInternetExplorerEnvironmentVariables(String pathToIEDriverServerExe, String debugLevel,
            String pathToIEDriverServerLog) {
        System.setProperty("webdriver.ie.driver", pathToIEDriverServerExe);
        System.setProperty("webdriver.ie.driver.loglevel", debugLevel);
        System.setProperty("webdriver.ie.driver.logfile", pathToIEDriverServerLog);
    }

    /**
     * Close current webdriver session and collects possible garbage.
     * 
     * @throws Exception
     */
    public void closeWebSessionInDevice() throws Exception {
        // Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
        // String browserName = cap.getBrowserName().toLowerCase();
        try {
            if (driver != null) {

                driver.quit();
                driver = null;
                println(ANSI_YELLOW + "Markito is destroyed.");
            } else
                println(ANSI_YELLOW + "Markito is null.  Please check your teardown process.");
        } catch (Exception e) {
            println(ANSI_YELLOW + "ERROR: There was a problem closing Markito webdriver.");
            throw new Exception(e.getMessage());
        }

        // Kill Windows processes if they are running.
        /*
         * MarkitoBaseUtils.killProcessIfRunning(MarkitoBaseUtils.CHROME_EXE);
         * MarkitoBaseUtils.killProcessIfRunning(MarkitoBaseUtils.IE_EXE);
         * MarkitoBaseUtils.killProcessIfRunning(MarkitoBaseUtils.EDGE_EXE);
         * MarkitoBaseUtils.killProcessIfRunning(MarkitoBaseUtils.FIREFOX_EXE);
         * MarkitoBaseUtils.killProcessIfRunning(MarkitoBaseUtils.CHROMEDRIVER_EXE);
         * MarkitoBaseUtils.killProcessIfRunning(MarkitoBaseUtils.IE_DRIVER_EXE);
         * MarkitoBaseUtils.killProcessIfRunning(MarkitoBaseUtils.EDGEDRIVER_EXE);
         * MarkitoBaseUtils.killProcessIfRunning(MarkitoBaseUtils.FIREFOXDRIVER_EXE);
         */
    }

    /**
     * Find an element in DOM using By locator. When debug mode is ON highlights the
     * element to help visual debug.
     * 
     * @param by
     * @return WebElement
     */
    public WebElement findElement(By by) {
        printf(ANSI_YELLOW + "Finding element %s...", by);
        try {
            WebElement element = driver.findElement(by);
            highlightElement(element);
            printf(ANSI_YELLOW + "found.\n", by);
            return element;
        } catch (Exception e) {
            printf(ANSI_RED + "failed!!! %s\n", e.getMessage());
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
        printf(ANSI_YELLOW + "Finding elements %s...", by);
        try {
            List<WebElement> elements = driver.findElements(by);
            elements.forEach(element -> {
                highlightElement(element);
            }); // Highlights on debug mode.
            printf(ANSI_YELLOW + "found %d elements.\n", elements.size());
            return elements;
        } catch (Exception e) {
            printf(ANSI_RED + "failed!!! %s\n", e.getMessage());
            throw new WebDriverException(e.getMessage());
        }
    }

    /**
     * Establish a unified timeout parameter for implicit waits, page loads and
     * javascripts.
     * 
     * @param timeOutInSeconds
     */
    public void setTimeouts(long timeOutInSeconds) {
        this.timeOutInSeconds = timeOutInSeconds;
        try {
            driver.manage().timeouts().implicitlyWait(timeOutInSeconds, TimeUnit.SECONDS); // Timeouts de waitFor*
            driver.manage().timeouts().setScriptTimeout(timeOutInSeconds, TimeUnit.SECONDS); // Timeout de ejecución
                                                                                             // javascript
            driver.manage().timeouts().pageLoadTimeout(timeOutInSeconds, TimeUnit.SECONDS); // Timeout de espera de
                                                                                            // página
        } catch (Exception e) {
            printf(ANSI_YELLOW + "SetTimeouts in %d seconds. WARNING can not set pageLoadTimeout/setScriptTimeout.\n",
                    timeOutInSeconds);
        }
        // printf( ANSI_YELLOW+"SetTimeouts in %d seconds.\n", timeOutInSeconds);
    }

    /**
     * Get a unified timeout parameter for implicit waits, page loads and
     * javascripts.
     * 
     * @param timeOutInSeconds
     */
    public long getTimeouts() {
        return this.timeOutInSeconds;
    }

    /**
     * Creates a new window, selects it and returns the handler.
     * 
     * @param windowHandle
     */
    public String createWindow() {
        printf(ANSI_YELLOW + "CreateWindow...");
        try {
            Set<String> currentWindows = getWindowHandles();
            Set<String> newCurrentWindows = getWindowHandles();
            executeJsScript("window.open()");
            while (currentWindows.size() == newCurrentWindows.size()) {
                newCurrentWindows = getWindowHandles();
                printf("-");
            }
            for (String newWindow : newCurrentWindows) {
                if (!currentWindows.contains(newWindow)) {
                    driver.switchTo().window(newWindow);
                    printf(ANSI_YELLOW + "done...\n");
                    return newWindow;
                } else
                    printf(".");
            }
            throw new WebDriverException(ANSI_RED + "Can not create new window.");
        } catch (Exception e) {
            printf(ANSI_RED + "failed!!! %s\n", e.getMessage());
            throw new WebDriverException(e.getMessage());
        }
    }

    /**
     * Select the window referred by the window handle.
     * 
     * @param windowHandle
     */
    public void selectWindow(String windowHandle) {
        printf(ANSI_YELLOW + "SelectWindow...");
        try {
            driver.switchTo().window(windowHandle);
            printf(ANSI_YELLOW + "done... Window title %s\n", driver.getTitle());
        } catch (Exception e) {
            printf(ANSI_RED + "failed!!! %s\n", e.getMessage());
            throw new WebDriverException(e.getMessage());
        }
    }

    /**
     * Obtains the string handle be used to select the current window.
     */
    public String getWindowHandle() {
        printf(ANSI_YELLOW + "GetWindowHandle...");
        try {
            String ventanasPrevias = driver.getWindowHandle();
            printf(ANSI_YELLOW + "done...\n");
            return ventanasPrevias;
        } catch (Exception e) {
            printf(ANSI_RED + "failed!!! %s\n", e.getMessage());
            throw new WebDriverException(e.getMessage());
        }
    }

    /**
     * Finds in current webdriver windows handles a window with "title" and returns
     * the handle.
     * 
     * @param title: Expected window title.
     * @return: A window handle or null if not found.
     */
    public String getWindowHandleByTitle(String title) {
        Set<String> windowsHandles = driver.getWindowHandles();
        String handle = null;
        printf(ANSI_YELLOW + "GetWindowHandle...");
        for (String windowHandle : windowsHandles) {
            driver.switchTo().window(windowHandle);
            if (driver.getTitle().equals(title)) {
                handle = windowHandle;
                break;
            }
        }
        if (handle == null)
            printf(ANSI_YELLOW + "not found.\n");
        else
            printf(ANSI_YELLOW + "found.\n");

        return handle;
    }

    /**
     * Obtains a set of handles for all the current windows.
     * 
     * @return
     */
    public Set<String> getWindowHandles() {
        printf(ANSI_YELLOW + "GetWindowHandles...");
        try {
            Set<String> ventanasPrevias = new HashSet<String>(driver.getWindowHandles());
            printf(ANSI_YELLOW + "done...\n");
            return ventanasPrevias;
        } catch (Exception e) {
            printf(ANSI_RED + "failed!!! %s\n", e.getMessage());
            throw new WebDriverException(e.getMessage());
        }
    }

    /**
     * Waits for a new window to be opened and returns its handle during
     * timeOutInSeconds.
     * 
     * @return
     */
    public String waitForNewWindow() {
        return waitForNewWindow(timeOutInSeconds); // Uses standard timeout value.
    }

    /**
     * Waits for a new window to be opened and returns its handle.
     * 
     * @timeoutSeconds: Max time to wait for new window.
     * @return
     */
    public String waitForNewWindow(long timeoutSeconds) {
        printf(ANSI_YELLOW + "Waiting for new window...");
        try {
            Set<String> currentWindows = getWindowHandles();
            Set<String> newCurrentWindows = getWindowHandles();
            Stopwatch stopwatch = Stopwatch.createStarted();
            while (currentWindows.size() == newCurrentWindows.size()) {
                newCurrentWindows = getWindowHandles();
                if (stopwatch.elapsed(TimeUnit.SECONDS) > timeoutSeconds)
                    throw new WebDriverException("ERROR: New Window not found in " + timeoutSeconds + " seconds.\n");
                printf("-");
            }
            for (String newWindow : newCurrentWindows) {
                if (!currentWindows.contains(newWindow)) {
                    driver.switchTo().window(newWindow);
                    printf(ANSI_YELLOW + "done... Window title %s\n", driver.getTitle());
                    return newWindow;
                } else
                    printf(".");
            }
            throw new WebDriverException("ERROR: New Window not found in " + timeoutSeconds + " seconds.\n");
        } catch (Exception e) {
            printf(ANSI_RED + "failed!!! %s\n", e.getMessage());
            throw new WebDriverException(e.getMessage());
        }
    }

    /**
     * Waits until the number of opened windows is equal to NWindows.
     */
    public void waitForNWindows(int NWindows) {
        waitForNWindows(NWindows, timeOutInSeconds);
    }

    /**
     * Waits until the number of opened windows is equal to NWindows.
     */
    public void waitForNWindows(int NWindows, long timeoutSeconds) {
        printf(ANSI_YELLOW + "Waiting for new window...");
        try {
            Stopwatch stopwatch = Stopwatch.createStarted();
            while (driver.getWindowHandles().size() != NWindows) {
                if (stopwatch.elapsed(TimeUnit.SECONDS) > timeoutSeconds)
                    throw new WebDriverException("ERROR: New Window not found in " + timeoutSeconds + " seconds.\n");
                else
                    printf(".");
            }
            printf("\n");
        } catch (Exception e) {
            printf(ANSI_RED + "failed!!! %s\n", e.getMessage());
            throw new WebDriverException(e.getMessage());
        }
    }

    /**
     * Close the selected window. Please refer to
     * "https://www.selenium.dev/selenium/docs/api/java/com/thoughtworks/selenium/webdriven/commands/Close.html"
     */
    public void CloseCurrentWindow() {
        println(ANSI_YELLOW + "CloseCurrentWindow.");
        driver.close();
    }

    /**
     * Execute a JavaScript script.
     * Please refer to
     * "https://www.selenium.dev/selenium/docs/api/java/org/openqa/selenium/remote/server/handler/ExecuteScript.html"
     * 
     * @param script
     * @param args
     */
    public void executeJsScript(String script, java.lang.Object... args) {
        js.executeScript(script, args);
    }

    /**
     * Execute an asynchronous JavaScript script.
     * 
     * @param script
     * @param args
     */
    public void executeAsynchromousJsScript(String script, String args) {
        js.executeAsyncScript(script, args);
    }

    /**
     * Get a url and waits for complete load.
     * 
     * @param url
     */
    public void get(String url) {
        printf(ANSI_YELLOW + "Get [%s]...", url);
        try {
            driver.get(url);
            printf(ANSI_YELLOW + "done!!!\n");
        } catch (Exception e) {
            printf(ANSI_RED + "failed!!! %s\n", e.getMessage());
            throw new WebDriverException(e.getMessage());
        }
    }

    /**
     * Get Text of an element located by.
     * 
     * @param by
     */
    public String getText(By by) {
        printf(ANSI_YELLOW + "GetText from %s...", by);
        long currentTimeout = getTimeouts();
        try {
            highlightElement(by);
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
            String text = new WebDriverWait(driver, timeOutInSeconds)
                    .until(ExpectedConditions.presenceOfElementLocated(by))
                    .getText();
            printf(ANSI_YELLOW + "done [%s].\n", text, by);
            setTimeouts(currentTimeout);
            return text;
        } catch (Exception e) {
            setTimeouts(currentTimeout);
            printf(ANSI_RED + "failed!!!\n", e.getMessage());
            throw new WebDriverException(e.getMessage());
        }
    }

    /**
     * Get Value of an element located by.
     * 
     * @param by
     */
    public String getValue(By by) {
        printf(ANSI_YELLOW + "GetValue ");
        long currentTimeout = getTimeouts();
        try {
            highlightElement(by);
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
            String text = new WebDriverWait(driver, timeOutInSeconds)
                    .until(ExpectedConditions.presenceOfElementLocated(by))
                    .getAttribute("value");
            printf(ANSI_YELLOW + "done [%s] in object %s\n", text, by);
            setTimeouts(currentTimeout);
            return text;
        } catch (Exception e) {
            setTimeouts(currentTimeout);
            printf(ANSI_RED + "failed!!! %s\n", e.getMessage());
            throw new WebDriverException(e.getMessage());
        }
    }

    /**
     * Get Attribute of an element located by.
     * 
     * @param by
     * @param attributeName
     */
    public String getAttribute(By by, String attributeName) {
        printf(ANSI_YELLOW + "GetAttribute [" + attributeName + "]...");
        long currentTimeout = getTimeouts();
        try {
            highlightElement(by);
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
            String text = new WebDriverWait(driver, timeOutInSeconds)
                    .until(ExpectedConditions.presenceOfElementLocated(by))
                    .getAttribute(attributeName);
            printf(ANSI_YELLOW + "done [%s] in object %s\n", text, by);
            setTimeouts(currentTimeout);
            return text;
        } catch (Exception e) {
            setTimeouts(currentTimeout);
            printf(ANSI_RED + "failed!!! %s\n", e.getMessage());
            throw new WebDriverException(e.getMessage());
        }
    }

    /**
     * Sets the value of an attribute.
     * 
     * @param by
     * @param attributeName
     * @param value
     * @return Fails on attribute not found or other condition.
     */
    public void setAttributeValue(By by, String attributeName, String value) {
        printf(ANSI_YELLOW + "SetAttributeValue ");
        long currentTimeout = getTimeouts();
        try {
            highlightElement(by);
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
            new WebDriverWait(driver, timeOutInSeconds).until(ExpectedConditions.presenceOfElementLocated(by))
                    .getAttribute(attributeName);
            setTimeouts(currentTimeout);
            executeJsScript("arguments[0].setAttribute(arguments[1], arguments[2]);", driver.findElement(by),
                    attributeName, value);
            printf(ANSI_YELLOW + "done set attribute [%s] with value [%s] in object %s\n", attributeName, value, by);
        } catch (Exception e) {
            setTimeouts(currentTimeout);
            printf(ANSI_RED + "failed!!! %s\n", e.getMessage());
            throw new WebDriverException(e.getMessage());
        }
    }

    /**
     * Waits for a frame by handle is available and switch to it.
     * 
     * @param frameHandle
     * @throws InterruptedException
     */
    public void selectFrameByHandle(int frameHandle) throws InterruptedException {
        printf(ANSI_YELLOW + "SelectFrameByHandle %d...", frameHandle);
        long currentTimeout = getTimeouts();
        try {
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
            new WebDriverWait(driver, timeOutInSeconds)
                    .ignoring(StaleElementReferenceException.class)
                    .ignoring(WebDriverException.class)
                    .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameHandle));
            setTimeouts(currentTimeout);
        } catch (Exception e) {
            setTimeouts(currentTimeout);
            printf(ANSI_RED + "failed!!! %s\n", e.getMessage());
            throw new WebDriverException(e.getMessage());
        }

    }

    /**
     * Waits for a frame using By reference is available and switch to it.
     * 
     * @param frameLocator
     * @throws InterruptedException
     */
    public void selectFrameByLocator(By frameLocator) throws InterruptedException {
        printf(ANSI_YELLOW + "SelectFrameBy %s...", frameLocator);
        long currentTimeout = getTimeouts();
        try {
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
            new WebDriverWait(driver, timeOutInSeconds).ignoring(StaleElementReferenceException.class)
                    .ignoring(WebDriverException.class)
                    .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameLocator));
            /*
             * WebElement frame = driver.findElement(frameLocator);
             * driver.switchTo().defaultContent();
             * driver.switchTo().frame( frame );
             */
            setTimeouts(currentTimeout);
            printf(ANSI_YELLOW + "done...\n");
        } catch (Exception e) {
            setTimeouts(currentTimeout);
            printf(ANSI_RED + "failed!!! %s\n", e.getMessage());
            throw new WebDriverException(e.getMessage());
        }

    }

    /**
     * Waits for a frame using frameId (string) is available and switch to it.
     * 
     * @param frameId
     * @throws InterruptedException
     */
    public void selectFrameById(String frameID) {
        printf(ANSI_YELLOW + "SelectFrameBy %s...", frameID);
        long currentTimeout = getTimeouts();
        try {
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
            new WebDriverWait(driver, timeOutInSeconds)
                    .ignoring(StaleElementReferenceException.class)
                    .ignoring(WebDriverException.class)
                    .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameID));
            // driver.switchTo().frame( frameID );
            setTimeouts(currentTimeout);
            printf(ANSI_YELLOW + "done...\n");
        } catch (Exception e) {
            setTimeouts(currentTimeout);
            printf(ANSI_RED + "failed!!! %s\n", e.getMessage());
            throw new WebDriverException(e.getMessage());
        }
    }

    /**
     * Waits for an element to be clickable located using By and click in it.
     * 
     * @param locator
     */
    public void click(By locator) {
        printf(ANSI_YELLOW + "Clicking %s...", locator);
        long currentTimeout = getTimeouts();
        try {
            highlightElement(locator);
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
            new WebDriverWait(driver, timeOutInSeconds)
                    .ignoring(StaleElementReferenceException.class)
                    .ignoring(WebDriverException.class)
                    .until(ExpectedConditions.elementToBeClickable(locator));
            driver.findElement(locator).click();
            setTimeouts(currentTimeout);
            printf(ANSI_YELLOW + "done.\n", locator);
        } catch (Exception e) {
            setTimeouts(currentTimeout);
            printf(ANSI_RED + "failed!!! %s\n", e.getMessage());
            throw new WebDriverException(e.getMessage());
        }
    }

    /**
     * Click in a clickable element located using By and bypassing webdriver using
     * js executor.
     * 
     * @param locator
     */
    public void clickJS(By locator) {
        printf(ANSI_YELLOW + "Clicking JS %s...", locator);
        try {
            highlightElement(locator);
            // Do not add waits.
            executeJsScript("arguments[0].click();", driver.findElement(locator));
            printf(ANSI_YELLOW + "done.\n", driver.findElement(locator));
        } catch (Exception e) {
            printf(ANSI_RED + "failed!!! %s\n", e.getMessage());
            throw new WebDriverException(e.getMessage());
        }
    }

    /**
     * Click in a element located using webdriver Actions.
     * 
     * @param locator
     */
    public void clickUsingActions(By locator) {
        printf(ANSI_YELLOW + "Clicking simulated %s...", locator);
        long currentTimeout = getTimeouts();
        try {
            highlightElement(locator);
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
            new WebDriverWait(driver, timeOutInSeconds)
                    .ignoring(StaleElementReferenceException.class)
                    .ignoring(WebDriverException.class)
                    .until(ExpectedConditions.elementToBeClickable(locator));

            WebElement element = driver.findElement(locator);
            highlightElement(element);
            // Do not add waits.
            Actions builder = new Actions(driver);
            builder
                    .moveToElement(element)
                    .click(element)
                    .perform();
            printf(ANSI_YELLOW + "done.\n", locator);
            setTimeouts(currentTimeout);
        } catch (Exception e) {
            setTimeouts(currentTimeout);
            printf(ANSI_RED + "failed!!! %s\n", e.getMessage());
            throw new WebDriverException(e.getMessage());
        }
    }

    /**
     * Double Click in a element located by locator using webdriver Actions.
     * This method doesn't wait for element clickable but visible.
     * 
     * @param locator
     */
    public void doubleClick(By locator) {
        printf(ANSI_YELLOW + "DoubleClick %s...", locator);
        long currentTimeout = getTimeouts();
        try {
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
            new WebDriverWait(driver, timeOutInSeconds)
                    .ignoring(StaleElementReferenceException.class)
                    .ignoring(WebDriverException.class)
                    .until(ExpectedConditions.visibilityOfElementLocated(locator));
            highlightElement(locator);
            WebElement element = driver.findElement(locator);
            Actions builder = new Actions(driver);
            builder
                    .moveToElement(element)
                    .doubleClick(element)
                    .perform();
            printf(ANSI_YELLOW + "done.\n");
            setTimeouts(currentTimeout);
        } catch (Exception e) {
            setTimeouts(currentTimeout);
            printf(ANSI_RED + "failed!!! %s\n", e.getMessage());
            throw new WebDriverException(e.getMessage());
        }
    }

    /**
     * Double Click in a element located by locator using webdriver Actions.
     * 
     * @param webElement
     */
    public void doubleClick(WebElement webElement) {
        printf(ANSI_YELLOW + "DoubleClick %s...", webElement);
        long currentTimeout = getTimeouts();
        try {
            highlightElement(webElement);
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
            new WebDriverWait(driver, timeOutInSeconds)
                    .ignoring(StaleElementReferenceException.class)
                    .ignoring(WebDriverException.class)
                    .until(ExpectedConditions.elementToBeClickable(webElement));

            Actions builder = new Actions(driver);
            builder
                    .moveToElement(webElement)
                    .doubleClick(webElement)
                    .perform();
            printf(ANSI_YELLOW + "done.\n");
            setTimeouts(currentTimeout);
        } catch (Exception e) {
            setTimeouts(currentTimeout);
            printf(ANSI_RED + "failed!!! %s\n", e.getMessage());
            throw new WebDriverException(e.getMessage());
        }
    }

    /**
     * MouseOver in a element located using webdriver Actions.
     * 
     * @param locator
     */
    public void mouseOver(By locator) {
        printf(ANSI_YELLOW + "MouseOver sobre %s...", locator);
        long currentTimeout = getTimeouts();
        try {
            highlightElement(locator);
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
            new WebDriverWait(driver, timeOutInSeconds)
                    .ignoring(StaleElementReferenceException.class)
                    .ignoring(WebDriverException.class)
                    .until(ExpectedConditions.elementToBeClickable(locator));

            WebElement element = driver.findElement(locator);
            highlightElement(element);
            // Do not add waits.
            Actions builder = new Actions(driver);
            builder
                    .moveToElement(element)
                    .perform();
            printf(ANSI_YELLOW + "done.\n", locator);
            setTimeouts(currentTimeout);
        } catch (Exception e) {
            setTimeouts(currentTimeout);
            printf(ANSI_RED + "failed!!! %s\n", e.getMessage());
            throw new WebDriverException(e.getMessage());
        }
    }

    /**
     * MouseOut in a element located using webdriver Actions.
     * 
     * @param locator
     */
    public void mouseOut(By locator) {
        printf(ANSI_YELLOW + "MouseOut sobre %s...", locator);
        long currentTimeout = getTimeouts();
        try {
            highlightElement(locator);
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
            new WebDriverWait(driver, timeOutInSeconds)
                    .ignoring(StaleElementReferenceException.class)
                    .ignoring(WebDriverException.class)
                    .until(ExpectedConditions.elementToBeClickable(locator));

            WebElement element = driver.findElement(locator);
            highlightElement(element);

            // Do not add waits.
            Actions builder = new Actions(driver);
            builder
                    .moveToElement(element)
                    .release(element)
                    .perform();
            printf(ANSI_YELLOW + "done.\n", locator);
            setTimeouts(currentTimeout);
        } catch (Exception e) {
            setTimeouts(currentTimeout);
            printf(ANSI_RED + "failed!!! %s\n", e.getMessage());
            throw new WebDriverException(e.getMessage());
        }
    }

    /**
     * DragAndDrop sourceObject over targetObject.
     * 
     * @param sourceObject
     * @param targetObject
     */
    public void dragAndDrop(WebElement sourceObject, WebElement targetObject) {
        printf(ANSI_YELLOW + "DragAndDrop %s over %s...", sourceObject, targetObject);
        long currentTimeout = getTimeouts();
        try {
            highlightElement(sourceObject);
            highlightElement(targetObject);
            List<WebElement> elements = new ArrayList<WebElement>();
            elements.add(sourceObject);
            elements.add(targetObject);
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
            new WebDriverWait(driver, timeOutInSeconds)
                    .ignoring(StaleElementReferenceException.class)
                    .ignoring(WebDriverException.class)
                    .until(ExpectedConditions.visibilityOfAllElements(elements));

            // Uses javascript to simulate drag and drop action.
            final String java_script = "var src=arguments[0],tgt=arguments[1];var dataTransfer={dropEffe" +
                    "ct:'',effectAllowed:'all',files:[],items:{},types:[],setData:fun" +
                    "ction(format,data){this.items[format]=data;this.types.append(for" +
                    "mat);},getData:function(format){return this.items[format];},clea" +
                    "rData:function(format){}};var emit=function(event,target){var ev" +
                    "t=document.createEvent('Event');evt.initEvent(event,true,false);" +
                    "evt.dataTransfer=dataTransfer;target.dispatchEvent(evt);};emit('" +
                    "dragstart',src);emit('dragenter',tgt);emit('dragover',tgt);emit(" +
                    "'drop',tgt);emit('dragend',src);";

            ((JavascriptExecutor) driver).executeScript(java_script, elements.get(0), elements.get(1));
            printf(ANSI_YELLOW + "done.\n");
            setTimeouts(currentTimeout);
        } catch (Exception e) {
            setTimeouts(currentTimeout);
            printf(ANSI_RED + "failed!!! %s\n", e.getMessage());
            throw new WebDriverException(e.getMessage());
        }
    }

    /**
     * ClickAt in a clickable element located using By at position (x,y).
     * 
     * @param locator
     */
    public void clickAt(By locator, int x, int y) {
        printf(ANSI_YELLOW + "ClickAt %s x=%d y=%d", locator, x, y);
        long currentTimeout = getTimeouts();
        try {
            highlightElement(locator);
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
            highlightElement(locator);
            new WebDriverWait(driver, timeOutInSeconds).ignoring(StaleElementReferenceException.class)
                    .ignoring(WebDriverException.class).until(ExpectedConditions.elementToBeClickable(locator));
            new Actions(driver).moveToElement(driver.findElement(locator), x, y).click().perform();
            printf(ANSI_YELLOW + "done.\n", locator);
            setTimeouts(currentTimeout);
        } catch (Exception e) {
            setTimeouts(currentTimeout);
            printf(ANSI_RED + "failed!!! %s\n", e.getMessage());
            throw new WebDriverException(e.getMessage());
        }
    }

    /**
     * Waits for an element to be present and fail if not found on timeOutInSeconds.
     * 
     * @param locator By of the element.
     */
    public void waitForElementPresent(By locator) {
        waitForElementPresent(locator, timeOutInSeconds);
    }

    /**
     * Waits for an element to be present and not fail if not found on timeout,
     * instead it will return true or false.
     * 
     * @param locator
     * @param timeout max time to wait for element to be visible.
     * @return true if element is visible!!! false if element is not visible.
     */
    public boolean waitForElementPresent(By locator, long timeout) {
        printf(ANSI_YELLOW + "Waiting for element %s on %d seconds...", locator.toString(), timeout);
        long currentTimeout = getTimeouts();
        try {
            highlightElement(locator);
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
            new WebDriverWait(driver, timeout).ignoring(StaleElementReferenceException.class)
                    .ignoring(WebDriverException.class).until(ExpectedConditions.presenceOfElementLocated(locator));
            printf(ANSI_YELLOW + "visible!!!\n");
            setTimeouts(currentTimeout);
            return true;
        } catch (Exception e) {
            setTimeouts(currentTimeout);
            printf(ANSI_RED + "failed!!! %s\n", e.getMessage());
            return false;
        }
    }

    /**
     * Waits for an element to be visible and fail if not found on timeOutInSeconds.
     * 
     * @param locator
     */
    public void waitForElementVisible(By locator) {
        printf(ANSI_YELLOW + "Waiting for element %s...", locator.toString());
        long currentTimeout = getTimeouts();
        try {
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
            new WebDriverWait(driver, timeOutInSeconds).ignoring(StaleElementReferenceException.class)
                    .ignoring(WebDriverException.class).until(ExpectedConditions.visibilityOfElementLocated(locator));
            printf(ANSI_YELLOW + "visible!!!\n");
            setTimeouts(currentTimeout);
        } catch (Exception e) {
            setTimeouts(currentTimeout);
            printf(ANSI_RED + "failed!!! %s\n", e.getMessage());
            throw new WebDriverException(e.getMessage());
        }
    }

    /**
     * Waits for an element to be visible and not fail if not found on timeout,
     * instead it will return true or false.
     * 
     * @param locator
     * @param timeout max time to wait for element to be visible.
     * @return true or false depending on finding the element in timeout seconds.
     */
    public boolean waitForElementVisible(By locator, long timeout) {
        printf(ANSI_YELLOW + "Waiting for element %s on %d seconds...", locator.toString(), timeout);
        long currentTimeout = getTimeouts();
        try {
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
            new WebDriverWait(driver, timeout).ignoring(StaleElementReferenceException.class)
                    .ignoring(WebDriverException.class).until(ExpectedConditions.visibilityOfElementLocated(locator));
            printf(ANSI_YELLOW + "visible!!!\n");
            setTimeouts(currentTimeout);
            return true;
        } catch (Exception e) {
            setTimeouts(currentTimeout);
            printf(ANSI_RED + "no visible!!! %s\n", e.getMessage());
            return false;
        }
    }

    /**
     * Waits for an element to be visible and not fail if not found on timeout,
     * instead it will return true or false.
     * 
     * @param webElement
     * @param timeout    max time to wait for element to be visible.
     * @return true or false depending on finding the element in timeout seconds.
     */
    public boolean waitForElementVisible(WebElement webElement, long timeout) {
        printf(ANSI_YELLOW + "Waiting for element %s on %d seconds...", webElement.toString(), timeout);
        long currentTimeout = getTimeouts();
        try {
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
            new WebDriverWait(driver, timeout).ignoring(StaleElementReferenceException.class)
                    .ignoring(WebDriverException.class).until(ExpectedConditions.visibilityOf(webElement));
            printf(ANSI_YELLOW + "visible!!!\n");
            setTimeouts(currentTimeout);
            return true;
        } catch (Exception e) {
            setTimeouts(currentTimeout);
            printf(ANSI_RED + "no visible!!! %s\n", e.getMessage());
            return false;
        }
    }

    /**
     * Waits for an element to be visible and fail if not found on timeOutInSeconds.
     * 
     * @param webElement
     */
    public void waitForElementVisible(WebElement webElement) {
        printf(ANSI_YELLOW + "Waiting for element %s...", webElement.toString());
        long currentTimeout = getTimeouts();
        try {
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
            new WebDriverWait(driver, timeOutInSeconds).ignoring(StaleElementReferenceException.class)
                    .ignoring(WebDriverException.class).until(ExpectedConditions.visibilityOf(webElement));
            printf(ANSI_YELLOW + "visible!!!\n");
            setTimeouts(currentTimeout);
        } catch (Exception e) {
            setTimeouts(currentTimeout);
            printf(ANSI_RED + "failed!!! %s\n", e.getMessage());
            throw new WebDriverException(e.getMessage());
        }
    }

    /**
     * Waits for text in element.
     * 
     * @param text:    Text to wait for
     * @param locator: Locator to element.
     * @throws WebDriverException when not found in timeoutInSeconds.
     */
    public void waitForTextInElement(String text, By locator) {
        printf(ANSI_YELLOW + "Waiting for text %s in element %s...", text, locator.toString());
        long currentTimeout = getTimeouts();
        try {
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
            new WebDriverWait(driver, timeOutInSeconds)
                    .ignoring(StaleElementReferenceException.class)
                    .ignoring(WebDriverException.class)
                    .until(ExpectedConditions.and(
                            ExpectedConditions.visibilityOfElementLocated(locator),
                            ExpectedConditions.textToBe(locator, text)));
            setTimeouts(currentTimeout);
            printf(ANSI_YELLOW + "visible!!!\n");
        } catch (Exception e) {
            setTimeouts(currentTimeout);
            printf(ANSI_YELLOW + "failed after %d seconds!!!\n", timeOutInSeconds);
            throw new WebDriverException(e.getMessage());
        }
    }

    /**
     * Waits for text in element.
     * 
     * @param text:    Text to wait for
     * @param locator: Locator to element.
     * @param timeout: Time to wait for.
     * @return true if text is found on element before timeout seconds.
     */
    public boolean waitForTextInElement(String text, By locator, long timeout) {
        printf(ANSI_YELLOW + "Waiting for text %s in element %s...", text, locator.toString());
        long currentTimeout = getTimeouts();
        try {
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
            new WebDriverWait(driver, timeout)
                    .ignoring(StaleElementReferenceException.class)
                    .ignoring(WebDriverException.class)
                    .until(ExpectedConditions.and(
                            ExpectedConditions.visibilityOfElementLocated(locator),
                            ExpectedConditions.textToBe(locator, text)));
            printf(ANSI_YELLOW + "visible!!!\n");
            setTimeouts(currentTimeout);
            return true;
        } catch (Exception e) {
            setTimeouts(currentTimeout);
            printf(ANSI_YELLOW + "failed after %d seconds!!!\n", timeout);
            return false;
        }
    }

    /**
     * Waits for text in page to be visible.
     * 
     * @param text: Text to wait for
     */
    public void waitForTextInPage(String text) {
        printf(ANSI_YELLOW + "Searching for text %s...", text);
        long currentTimeout = getTimeouts();
        try {
            By by = By.xpath("//*[@text='" + text + "']");
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
            new WebDriverWait(driver, timeOutInSeconds).ignoring(StaleElementReferenceException.class)
                    .ignoring(WebDriverException.class)
                    .until(ExpectedConditions.presenceOfAllElementsLocatedBy(by));

            if (driver.getPageSource().contains(text)) {
                printf(ANSI_YELLOW + "visible!!!\n");
            }
            setTimeouts(currentTimeout);
        } catch (Exception e) {
            setTimeouts(currentTimeout);
            printf(ANSI_RED + "failed: %s\n", e.getMessage());
            throw new WebDriverException(e.getMessage());
        }
    }

    /**
     * Simulates typing Keys over an editable element located By.
     * 
     * @param locator
     * @param keys
     */
    public void sendKeys(By locator, String keys) {
        printf(ANSI_YELLOW + "SendKeys %s to object %s...", keys, locator);
        long currentTimeout = getTimeouts();
        try {
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
            new WebDriverWait(driver, timeOutInSeconds).ignoring(StaleElementReferenceException.class)
                    .ignoring(WebDriverException.class).until(ExpectedConditions.visibilityOfElementLocated(locator));
            WebElement textBox = driver.findElement(locator);
            //highlightElement( textBox);
            if ( textBox == null ){
                throw new Exception(ANSI_RED+" Can not find element "+ locator);
            }
            textBox.sendKeys(keys);
            setTimeouts(currentTimeout);
            println("done!");
        } catch (Exception e) {
            setTimeouts(currentTimeout);
            printf(ANSI_RED + "failed! %s\n", e.getMessage());
            throw new WebDriverException(e.getMessage());
        }
    }

    /**
     * Waits for alert present and Clicks OK if present during timeout period.
     */
    public void clickOKOnAlert() {
        printf(ANSI_YELLOW + "Clicking OK on alert...");
        try {
            waitForAlertPresent(timeOutInSeconds);
            Alert alert = driver.switchTo().alert();
            alert.accept();
            printf(ANSI_YELLOW + "done.\n");
        } catch (Exception e) {
            printf(ANSI_RED + "failed! %s\n", e.getMessage());
            throw new WebDriverException(e.getMessage());
        }
    }

    /**
     * Waits for alert present and Clicks CANCEL if present during timeout period.
     */
    public void clickCancelOnAlert() {
        try {
            printf(ANSI_YELLOW + "Clicking CANCEL on alert...");
            waitForAlertPresent(timeOutInSeconds);
            Alert alert = driver.switchTo().alert();
            alert.dismiss();
            printf(ANSI_YELLOW + "done.\n");
        } catch (Exception e) {
            printf(ANSI_RED + "failed! %s\n", e.getMessage());
            throw new WebDriverException(e.getMessage());
        }
    }

    /**
     * Waits for alert present and gets the inner text to a String.
     * 
     * @return String
     */
    public String getTextOfAlert() {
        try {
            printf(ANSI_YELLOW + "GetTextOfAlert: Waiting for alert present...");
            waitForAlertPresent(timeOutInSeconds);
            printf(ANSI_YELLOW + "Present!  Now getting text!!..");
            Alert alert = driver.switchTo().alert();
            String text = alert.getText();
            printf(ANSI_YELLOW + "Text retrieved=%s\n", text);
            return text;
        } catch (Exception e) {
            printf(ANSI_RED + "failed! %s\n", e.getMessage());
            throw new WebDriverException(e.getMessage());
        }
    }

    /**
     * Writes an string on the alert msg.
     * 
     * @param text
     */
    public void typeTextOnAlert(String text) {
        printf(ANSI_YELLOW + "TypeTextOnAlert: Waiting for alert present...");
        waitForAlertPresent(timeOutInSeconds);
        try {
            Alert alert = driver.switchTo().alert();
            alert.sendKeys(text);
            printf(ANSI_YELLOW + "Text sent=%s\n", text);
        } catch (Exception e) {
            printf(ANSI_RED + "ERROR:Text not sent=%s\n", e.getMessage());
            throw new WebDriverException(e.getMessage());
        }
    }

    /**
     * Waits for alert present during timeOutInSeconds period.
     * 
     * @param timeOutInSeconds
     * @return
     */
    public boolean waitForAlertPresent(long timeOutInSeconds) {
        printf(ANSI_YELLOW + "WaitForAlertPresent: Waiting for alert present in %d seconds...", timeOutInSeconds);
        long currentTimeout = getTimeouts();

        try {
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
            new WebDriverWait(driver, timeOutInSeconds).ignoring(StaleElementReferenceException.class)
                    .until(ExpectedConditions.alertIsPresent());
            setTimeouts(currentTimeout);
            printf(ANSI_YELLOW + "Alert is present.\n");
            return true;
        } catch (Exception e) {
            setTimeouts(currentTimeout);
            printf(ANSI_RED + "Alert is not present.\n");
            return false;
        }
    }

    /**
     * Highlights WebElement if debug mode is ON.
     * 
     * @param element
     */
    public void highlightElement(WebElement element) {
        /*if (getDebugMode())
            executeJsScript("arguments[0].setAttribute('style', 'background: yellow; border: 3px solid blue;');",
                    element);*/
    }

    /**
     * Highlights an element By if debug mode is ON.
     * 
     * @param element
     */
    public void highlightElement(By element) {
        /*if (getDebugMode())
            executeJsScript("arguments[0].setAttribute('style', 'background: yellow; border: 3px solid blue;');",
                    driver.findElement(element));*/
    }

    /**
     * Selects an option from by using VisibleText.
     * 
     * @param selectObject
     * @param VisibleText
     */
    public void selectOptionByVisibleText(By selectObject, String VisibleText) {
        printf(ANSI_YELLOW + "SelectOptionByVisibleText from %s option %s...", selectObject, VisibleText);
        try {
            highlightElement(driver.findElement(selectObject));
            Select dropdown = new Select(driver.findElement(selectObject));
            dropdown.selectByVisibleText(VisibleText);
            println(ANSI_YELLOW + "done.");
        } catch (Exception e) {
            printf(ANSI_RED + "ERROR: option not selected. %s\n", e.getMessage());
            throw new RuntimeException();
        }
    }

    /**
     * Selects an option from by using integer index.
     * 
     * @param by
     * @param index
     */
    public void selectOptionByIndex(By by, int index) {
        printf(ANSI_YELLOW + "SelectOptionByIndex from %s index %s\n", by, index);
        try {
            highlightElement(driver.findElement(by));
            Select dropdown = new Select(driver.findElement(by));
            dropdown.selectByIndex(index);
            println(ANSI_YELLOW + "done.");
        } catch (Exception e) {
            printf(ANSI_RED + "ERROR: option not selected. %s\n", e.getMessage());
            throw new RuntimeException();
        }

    }

    /**
     * Selects an option from by using current value.
     * 
     * @param by
     * @param value
     */
    public void selectOptionByValue(By by, String value) {
        printf(ANSI_YELLOW + "SelectOptionByValue from %s value %s...", by, value);
        try {
            Select dropdown = new Select(driver.findElement(by));
            highlightElement(driver.findElement(by));
            dropdown.selectByValue(value);
            println(ANSI_YELLOW + "done.");
        } catch (Exception e) {
            printf(ANSI_RED + "ERROR: option not selected. %s\n", e.getMessage());
            throw new RuntimeException();
        }

    }

    public void setLocation(double x, double y, double z) {
        printf(ANSI_YELLOW + "Set location to %f, %f, %f...", x, y, z);
        try {
            ((ChromeDriver) driver).setLocation(new Location(x, y, z));

            printf(ANSI_YELLOW + "done!!!\n");
        } catch (Exception e) {
            printf(ANSI_RED + "failed!!! %s\n", e.getMessage());
            throw new WebDriverException(e.getMessage());
        }
    }

    /**
     * Gets an screen snapshot and saves to a file.
     * 
     * @param fileWithPath: Pathname of the file to be generated.
     */
    public void takeScreenSnapshot(String fileWithPath) throws Exception {
        // Convert web driver object to TakeScreenshot
        TakesScreenshot scrShot = ((TakesScreenshot) driver);
        // Call getScreenshotAs method to create image file
        File SrcFile = scrShot.getScreenshotAs(OutputType.FILE);
        // Move image file to new destination
        File DestFile = new File(fileWithPath);
        // Copy file at destination
        FileUtils.copyFile(SrcFile, DestFile);
    }

    protected boolean isElementPresent(By selector) {
        printf(ANSI_YELLOW + "Is element present.???..." + selector);
        waitForElementPresent(selector);
        boolean returnVal = true;
        try {
            driver.findElement(selector);
            printf(ANSI_YELLOW + "yes.\n");
        } catch (NoSuchElementException e) {
            printf(ANSI_YELLOW + "no.\n");
            returnVal = false;
        }
        return returnVal;
    }

    /***
     * Takes an screenshot and leaves it in file named TestResults\Name-Date.png
     * 
     * @param Name: The name you want to recognize.
     */
    public void getScreenSnapshotWithDate(String Name) {
        LocalDateTime ldt = LocalDateTime.now();
        String date = ldt.toString().replaceAll("\\W+", "");
        try {
            takeScreenSnapshot("TestResults\\" + Name + "-" + date + ".png");
        } catch (Exception e) {
            printf("ERROR al tomar snapshot. Stack:%s\n", e.getMessage());
        }
    }

    /**
     * Waits for a a full page load by reading document.readyState.
     */
    public void waitForPageToLoad() {
        printf(ANSI_YELLOW + "WaitForPageToLoad...");
        try {
            String pageLoadStatus = null;
            do {
                js = (JavascriptExecutor) driver;
                pageLoadStatus = (String) js.executeScript("return document.readyState");
            } while (!pageLoadStatus.equals("complete"));
            printf(ANSI_YELLOW + "done.\n");
        } catch (Exception e) {
            printf(ANSI_RED + "failed. Stack=%s\n", e.getMessage());
            throw new WebDriverException(e.getMessage());
        }
    }

    /**
     * 
     * @param element
     */
    public void waitForElementInvisibility(By element) {
        printf(ANSI_YELLOW + "Waiting for invisibility of element %s...", element.toString());
        long currentTimeout = getTimeouts();
        try {
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
            new WebDriverWait(driver, timeOutInSeconds)
                    .until(ExpectedConditions.invisibilityOf(driver.findElement(element)));
            printf(ANSI_YELLOW + "invisible!!!\n");
            setTimeouts(currentTimeout);
        } catch (Exception e) {
            printf(ANSI_RED + "failed!!! %s\n", e.getMessage());
            setTimeouts(currentTimeout);
            throw new WebDriverException(e.getMessage());
        }
    }

    public void waitForElementToBeClickable(By element) {
        printf(ANSI_YELLOW + "Waiting for Clickeable of element %s...", element.toString());
        long currentTimeout = getTimeouts();
        try {
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
            new WebDriverWait(driver, timeOutInSeconds).until(ExpectedConditions.elementToBeClickable(element));
            printf(ANSI_YELLOW + "Clickeable!!!\n");
            setTimeouts(currentTimeout);
        } catch (Exception e) {
            printf(ANSI_RED + "failed!!! %s\n", e.getMessage());
            setTimeouts(currentTimeout);
            throw new WebDriverException(e.getMessage());
        }
    }

    public boolean waitForWebElementSelected(By chkDO, long timeOut) {
        printf(ANSI_YELLOW + "Waiting for element %s...", chkDO.toString());
        long currentTimeout = getTimeouts();
        try {
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
            new WebDriverWait(driver, timeOut).until(ExpectedConditions.elementSelectionStateToBe(chkDO, true));
            printf(ANSI_YELLOW + "Seleccionado!!!\n");
            setTimeouts(currentTimeout);
            return true;
        } catch (Exception e) {
            printf(ANSI_RED + "failed!!! %s\n", e.getMessage());
            setTimeouts(currentTimeout);
            return false;
        }
    }

    @Override
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    @Override
    public String getTitle() {
        return driver.getTitle();
    }

    @Override
    public String getPageSource() {
        return driver.getPageSource();
    }

    @Override
    public void close() {
        driver.close();        
    }

    @Override
    public void quit() {
        driver.quit();
    }

    @Override
    public TargetLocator switchTo() {
        return driver.switchTo();
    }

    @Override
    public Navigation navigate() {
        return driver.navigate();
    }

    @Override
    public Options manage() {
        return driver.manage();
    }

    public WebDriver getDriver() {
        return driver;
    }

    public void setDriver(WebDriver driver) {
        this.driver = driver;
    }

    public JavascriptExecutor getJs() {
        return js;
    }

    public void setJs(JavascriptExecutor js) {
        this.js = js;
    }

    public long getTimeOutInSeconds() {
        return timeOutInSeconds;
    }

    public void setTimeOutInSeconds(long timeOutInSeconds) {
        this.timeOutInSeconds = timeOutInSeconds;
    }
}
