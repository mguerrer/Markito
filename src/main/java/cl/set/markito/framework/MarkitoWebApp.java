package cl.set.markito.framework;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nullable;

import org.openqa.selenium.html5.Location;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.github.sukgu.Shadow;
import com.google.common.base.Stopwatch;

import cl.set.markito.MarkitoBaseUtils;
import cl.set.markito.framework.browsers.Browser;
import cl.set.markito.framework.cloud.BrowserStack;
import cl.set.markito.framework.devices.Device;
import io.appium.java_client.MobileDriver;
import io.appium.java_client.MultiTouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.appmanagement.ApplicationState;
import io.appium.java_client.ios.IOSDriver;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.WebDriver.Navigation;
import org.openqa.selenium.WebDriver.Options;
import org.openqa.selenium.WebDriver.TargetLocator;
import org.openqa.selenium.WebDriver.Timeouts;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;

import io.github.bonigarcia.wdm.WebDriverManager;

/**
 * Generic class to drive browsers in desktop and mobile devices.
 * Marcos Guerrero: 12-01-2023
 */
public class MarkitoWebApp extends MarkitoBaseUtils {
    private WebDriver driver = null;
    private long timeOutInSeconds = 60;
    private Boolean automaticDriverDownload = false;
    private BrowserStack browserStack = null;
    public Map<String, Object> vars = new HashMap<String, Object>();

    public MarkitoWebApp() {
        browserStack = new BrowserStack(getColoredOutput());
        println(ANSI_GREEN + "\nMarkito Web App created. Thread=" + Thread.currentThread().getId());
    }

    public MarkitoWebApp(WebDriver driverObject) {
        browserStack = new BrowserStack(getColoredOutput());
        println(ANSI_GREEN + "\nMarkito Web App created. Thread=" + Thread.currentThread().getId());
        driver = driverObject;
    }

    @Deprecated
    public void finalize() throws Throwable {
    }

    @Deprecated
    public Object clone() throws CloneNotSupportedException {
        return driver;
    }

    public boolean isIOSDriver() throws Exception {
        if (getDriver() == null) {
            throw new Exception(ANSI_RED + "isIOSDriver ERROR: Markito Web App is null.");
        }
        return getDriver().toString().contains("io.appium.java_client.ios.IOSDriver");
    }

    public boolean isAndroid() throws Exception {
        if (getDriver() == null) {
            throw new Exception(ANSI_RED + "isAndroid ERROR: Markito Web App is null.");
        }
        return getDriver().toString().contains("automationName=UIAutomator2");
    }

    @SuppressWarnings("unchecked")
    public IOSDriver<WebElement> getIosDriver() {
        return (IOSDriver<WebElement>) driver;
    }

    @SuppressWarnings("unchecked")
    public AndroidDriver<WebElement> getAndroidDriver() {
        return (AndroidDriver<WebElement>) driver;
    }

    @SuppressWarnings("unchecked")
    public MobileDriver<WebElement> getMobileDriver() {
        return (MobileDriver<WebElement>) driver;
    }

    /**
     * For local execution download/update needed drivers for current installed
     * browser version. This method allows to do (true) or not (false).
     * 
     * @param automaticDriverDownload: true or false
     */
    public void setAutomaticDriverDownload(Boolean automaticDriverDownload) {
        this.automaticDriverDownload = automaticDriverDownload;
    }

    public Boolean getAutomaticDriverDownload() {
        return automaticDriverDownload;
    }

    /**
     * Setup of BrowserStack dashboard parameters to organize test results.
     * 
     * @param projectName
     * @param buildName
     * @param testName
     */
    public void setBrowserstackProjectInformation(String projectName, String buildName, String testName) {
        browserStack.setProjectInformation(projectName, buildName, testName);
    }

    /**
     * Open a webbrowser session in a device.
     * 
     * @param browser
     * @param device
     * @return
     * @throws Exception
     */
    public WebDriver openBrowserSessionInDevice(Browser browser, Device device) throws Exception {
        WebDriver driver;

        printf(ANSI_WHITE + "Creating Markito WEB session on browser " + ANSI_YELLOW + browser.getName() + ANSI_WHITE
                + " on device "
                + ANSI_YELLOW + device.getName() + "-" + device.getPlatform() + "...\n");
        try {
            if (device.getProviderURL().contains("browserstack")) {
                browserStack.setDesiredWebTechnicalCapabilities(browser.getName().toString(), device.getName(),
                        device.getPlatform().toString(),
                        device.getPlatform_version());

            } else if (!device.getProviderURL().equals("")) {
                throw new Exception(ANSI_RED + "ERROR: Provider " + device.getProviderURL()
                        + " not supported for device " + device.getName());
            }
            if (device.equals(LOCAL_COMPUTER_DEVICE)) {
                driver = setLocalWebDrivers(browser);
            } else {
                LogCapabilities(browserStack.getCapabilities());
                driver = setRemoteWebDrivers(device, browserStack.getCapabilities());
            }
        } catch (Exception e) {
            println("\n" + ANSI_RED + "ERROR on creating session on device " + device.getName() + " Stack:"
                    + e.getMessage());
            throw new Exception(e.getMessage());
        }
        return driver;
    }

    /**
     * Close current webdriver session and collects possible garbage.
     * 
     * @throws Exception
     */
    public void closeWebSessionInDevice() throws Exception {
        println(ANSI_YELLOW + getMethodName() + "...");
        try {
            if (driver != null) {
                getDriver().quit();
                setDriver(null);
                println(ANSI_WHITE + "Markito Web App is destroyed.");
            } else
                println(ANSI_YELLOW + "WARNING: Markito is null.  Please check your teardown process.");
        } catch (Exception e) {
            println(ANSI_YELLOW + "ERROR: There was a problem closing Markito webdriver.");
            throw new Exception(e.getMessage());
        }
    }

    /**
     * Find an element in DOM using By locator. When debug mode is ON highlights the
     * element to help visual debug.
     * 
     * @param by
     * @return WebElement
     */
    public WebElement findElement(By by) {
        printf(ANSI_YELLOW + getMethodName() + " %s...", by);
        try {
            WebElement element = getDriver().findElement(by);
            highlightElement(element);
            printf(ANSI_YELLOW + "found.\n", by);
            return element;
        } catch (Exception e) {
            printf(ANSI_RED + "failed!!! %s\n", e.getMessage());
            throw new WebDriverException(e.getMessage());
        }
    }

    /**
     * Find all elements within the current page using the given mechanism.
     * This method is affected by the 'implicit wait' times in force at the time of
     * execution. When
     * implicitly waiting, this method will return as soon as there are more than 0
     * items in the
     * found collection, or will return an empty list if the timeout is reached.
     *
     * @param by The locating mechanism to use
     * @return A list of all {@link WebElement}s, or an empty list if nothing
     *         matches
     * @see org.openqa.selenium.By
     * @see org.openqa.selenium.WebDriver.Timeouts
     */
    public List<WebElement> findElements(By by) {
        printf(ANSI_YELLOW + getMethodName() + " %s...", by);
        try {
            List<WebElement> elements;
            if (isIOSDriver()) { // iOS
                elements = getIosDriver().findElements(by);
            } else if (isAndroid()) { // Android
                elements = getAndroidDriver().findElements(by);
            } else
                elements = getDriver().findElements(by);
            elements.forEach(element -> {// Highlights on debug mode.
                highlightElement(element);
            });
            printf(ANSI_YELLOW + "found %d elements.\n", elements.size());
            return elements;
        } catch (Exception e) {
            printf(ANSI_RED + "failed!!! %s\n", e.getMessage());
            throw new WebDriverException(e.getMessage());
        }
    }

    /**
     * Find an element in DOM using CSS selector supporting custom tags and SHADOW
     * roots.
     * See https://github.com/sukgu/shadow-automation-selenium/wiki for explanation
     * and examples.
     * When debug mode is ON highlights the element to help visual debug.
     * 
     * @param cssSelector
     * @return
     */
    public WebElement findShadowElementByCssSelector(String cssSelector) {
        printf(ANSI_YELLOW + getMethodName() + " %s...", cssSelector);
        try {
            Shadow shadow = new Shadow(getDriver());
            shadow.setExplicitWait((int) getTimeOutInSeconds(), 5); // will wait for maximum secs and will check after
                                                                    // every 5 secs.
            WebElement element = null;
            element = shadow.findElement(cssSelector);
            highlightElement(element);
            println(ANSI_YELLOW + "found.");
            return element;
        } catch (Exception e) {
            printf(ANSI_RED + "failed!!! %s\n", e.getMessage());
            throw new WebDriverException(e.getMessage());
        }
    }

    /**
     * Find an element in DOM using xpath supporting custom tags and SHADOW roots.
     * See https://github.com/sukgu/shadow-automation-selenium/wiki for explanation
     * and examples.
     * When debug mode is ON highlights the element to help visual debug.
     * 
     * @param xpath
     * @return WebElement
     */
    public WebElement findShadowElementByXPath(String xpath) {
        printf(ANSI_YELLOW + getMethodName() + " %s...", xpath);
        try {
            Shadow shadow = new Shadow(getDriver());
            shadow.setExplicitWait((int) getTimeOutInSeconds(), 5); // will wait for maximum secs and will check after
                                                                    // every 5 secs.
            WebElement element = null;
            element = shadow.findElementByXPath(xpath);
            highlightElement(element);
            println(ANSI_YELLOW + "found.");
            return element;
        } catch (Exception e) {
            printf(ANSI_RED + "failed!!! %s\n", e.getMessage());
            throw new WebDriverException(e.getMessage());
        }
    }

    /**
     * Find a list of elements in DOM using CSS selector supporting custom tags and
     * SHADOW roots.
     * See https://github.com/sukgu/shadow-automation-selenium/wiki for explanation
     * and examples.
     * When debug mode is ON highlights the element to help visual debug.
     * 
     * @param cssSelector
     * @return
     */
    List<WebElement> findShadowElementsByCssSelector(String cssSelector) {
        printf(ANSI_YELLOW + getMethodName() + " %s...", cssSelector);
        try {
            Shadow shadow = new Shadow(getDriver());
            shadow.setExplicitWait((int) getTimeOutInSeconds(), 5); // will wait for maximum secs and will check after
                                                                    // every 5 secs.
            List<WebElement> elements = null;
            elements = shadow.findElements(cssSelector);
            println(ANSI_YELLOW + "found.");
            return elements;
        } catch (Exception e) {
            printf(ANSI_RED + "failed!!! %s\n", e.getMessage());
            throw new WebDriverException(e.getMessage());
        }
    }

    /**
     * Find a list of elements in DOM using xpath supporting custom tags and SHADOW
     * roots.
     * See https://github.com/sukgu/shadow-automation-selenium/wiki for explanation
     * and examples.
     * When debug mode is ON highlights the element to help visual debug.
     * 
     * @param xpath
     * @return WebElement
     */
    public List<WebElement> findShadowElementsByXPath(String xpath) {
        printf(ANSI_YELLOW + getMethodName() + " %s...", xpath);
        try {
            Shadow shadow = new Shadow(getDriver());
            shadow.setExplicitWait((int) getTimeOutInSeconds(), 5); // will wait for maximum secs and will check after
                                                                    // every 5 secs.
            List<WebElement> elements = null;
            elements = shadow.findElementsByXPath(xpath);
            println(ANSI_YELLOW + "found.");
            return elements;
        } catch (Exception e) {
            printf(ANSI_RED + "failed!!! %s\n", e.getMessage());
            throw new WebDriverException(e.getMessage());
        }
    }

    /**
     * Load a new web page in the current browser window. This is done using an HTTP
     * GET operation,
     * and the method will block until the load is complete. This will follow
     * redirects issued either
     * by the server or as a meta-redirect from within the returned HTML. Should a
     * meta-redirect
     * "rest" for any duration of time, it is best to wait until this timeout is
     * over, since should
     * the underlying page change whilst your test is executing the results of
     * future calls against
     * this interface will be against the freshly loaded page. Synonym for
     * {@link org.openqa.selenium.WebDriver.Navigation#to(String)}.
     *
     * @param url The URL to load. It is best to use a fully qualified URL
     */
    public void get(String url) {
        printf(ANSI_YELLOW + getMethodName() + " [%s]...", url);
        try {
            getDriver().get(url);
            println(ANSI_YELLOW + "done!!!");
        } catch (Exception e) {
            printf(ANSI_RED + "failed!!! %s\n", e.getMessage());
            throw new WebDriverException(e.getMessage());
        }
    }

    /**
     * Get a string representing the current URL that the browser is looking at.
     *
     * @return The URL of the page currently loaded in the browser
     */
    public String getCurrentUrl() {
        printf(ANSI_YELLOW + getMethodName() + " [%s]...", getDriver().getCurrentUrl());
        return getDriver().getCurrentUrl();
    }

    /**
     * Get the source of the last loaded page. If the page has been modified after
     * loading (for
     * example, by Javascript) there is no guarantee that the returned text is that
     * of the modified
     * page. Please consult the documentation of the particular driver being used to
     * determine whether
     * the returned text reflects the current state of the page or the text last
     * sent by the web
     * server. The page source returned is a representation of the underlying DOM:
     * do not expect it to
     * be formatted or escaped in the same way as the response sent from the web
     * server. Think of it as
     * an artist's impression.
     *
     * @return The source of the current page
     */
    public String getPageSource() {
        printf(ANSI_YELLOW + getMethodName() + " [%s]...", getDriver().getPageSource());
        return getDriver().getPageSource();
    }

    /**
     * The title of the current page.
     *
     * @return The title of the current page, with leading and trailing whitespace
     *         stripped, or null
     *         if one is not already set
     */
    public String getCurrentWindowTitle() {
        printf(ANSI_YELLOW + getMethodName() + " [%s]...", getDriver().getTitle());
        return getDriver().getTitle();
    }

    /**
     * Obtains the string handle be used to select the current window.
     */
    public String getCurrentWindowHandle() {
        printf(ANSI_YELLOW + getMethodName() + "...");

        try {
            String currentWindowHandle = getDriver().getWindowHandle();
            printf(ANSI_YELLOW + "done...\n");
            return currentWindowHandle;
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
        Set<String> windowsHandles = getDriver().getWindowHandles();
        String handle = null;
        printf(ANSI_YELLOW + getMethodName() + "...");
        for (String windowHandle : windowsHandles) {
            getDriver().switchTo().window(windowHandle);
            if (getDriver().getTitle().equals(title)) {
                handle = windowHandle;
                break;
            }
        }
        if (handle == null)
            printf(ANSI_RED + "not found.\n");
        else
            printf(ANSI_YELLOW + "found.\n");

        return handle;
    }

    /**
     * Sets the browser level of the browser by using Javascript.
     * 
     * @param percentage
     */
    public void setZoomLevelOfCurrentPage(int percentage) {
        printf(ANSI_YELLOW + getMethodName() + " " + percentage + "%s...", "%");
        JavascriptExecutor js = (JavascriptExecutor) getDriver();
        js.executeScript("document.body.style.zoom='" + percentage + "%'");
        println(ANSI_YELLOW + "done...");
    }

    public Options manage() {
        printf(ANSI_YELLOW + getMethodName() + "...");
        return getDriver().manage();
    }

    public Navigation navigate() {
        printf(ANSI_YELLOW + getMethodName() + "...");
        return getDriver().navigate();
    }

    public void quit() {
        printf(ANSI_YELLOW + getMethodName() + "...");
        getDriver().quit();
    }

    public TargetLocator switchTo() {
        printf(ANSI_YELLOW + getMethodName() + "...");
        return getDriver().switchTo();
    }

    /**
     * Factory to create remote driver according to the platform.
     * 
     * @param device: Identify platform
     * @param caps:   Identify desired capcbilities.
     * @return
     * @throws MalformedURLException
     * @throws Exception
     */
    private WebDriver setRemoteWebDrivers(Device device, DesiredCapabilities caps)
            throws MalformedURLException, Exception {
        WebDriver driver;

        switch (device.getPlatform()) {
            case ANDROID:
                driver = new AndroidDriver<WebElement>(new URL(device.getProviderURL()), caps);
                break;
            case IOS:
                driver = new IOSDriver<WebElement>(new URL(device.getProviderURL()), caps);
                break;
            case OSX:
            case WINDOWS:
            case LINUX:
                driver = new RemoteWebDriver(new URL(device.getProviderURL()), caps);
                break;
            default:
                throw new Exception(
                        ANSI_RED + "\nERROR on getting " + device.getPlatform() + " session, unknown platfom.");
        }
        return driver;
    }

    /**
     * Factory to create web driver according to the local platform.
     * 
     * @param browser
     * @return
     * @throws Exception
     */
    private WebDriver setLocalWebDrivers(Browser browser) throws Exception {
        WebDriver driver = null;
        try {
            switch (browser.getName()) {
                case CHROME:
                    if (automaticDriverDownload)
                        WebDriverManager.chromedriver().setup();
                    driver = new ChromeDriver();
                    break;
                case EDGE:
                    if (automaticDriverDownload)
                        WebDriverManager.edgedriver().setup();
                    driver = new EdgeDriver();
                    break;
                case FIREFOX:
                    if (automaticDriverDownload)
                        WebDriverManager.firefoxdriver().setup();
                    driver = new FirefoxDriver();
                    break;
                case IE:
                    if (automaticDriverDownload)
                        WebDriverManager.iedriver().setup();
                    driver = new InternetExplorerDriver();
                    break;
                default:
                    throw new Exception(ANSI_RED + "Browser " + browser.getName() + " not recognized by Markito.");
            }
        } catch (Exception e) {
            throw new Exception(ANSI_RED + e.getMessage());
        }
        return driver;
    }

    /**
     * If this element is a text entry element, this will clear the value. Has no
     * effect on other elements.
     * Text entry elements are INPUT and TEXTAREA elements. Note that the events
     * fired by this event may not be as you'd expect.
     * In particular, we don't fire any keyboard or mouse events. If you want to
     * ensure keyboard events are fired, consider using
     * something like sendKeys(CharSequence) with the backspace key.
     * To ensure you get a change event, consider following with a call to
     * sendKeys(CharSequence) with the tab key.
     * 
     * @param element
     */
    public void clear(WebElement element) {
        printf(ANSI_YELLOW + getMethodName() + " [%s]...", getWebElementIdentification(element));
        element.clear();
        println(ANSI_YELLOW + " done.");
    }

    /**
     * Waits for a web element to be clickable and click in it.
     * 
     * @param element
     */
    public void click(WebElement element) {
        printf(ANSI_YELLOW + getMethodName() + "[%s]...", getWebElementIdentification(element));
        long currentTimeout = getTimeOutInSeconds();
        try {
            implicitlyWait(0, TimeUnit.SECONDS);
            new WebDriverWait(driver, timeOutInSeconds)
                    .ignoring(StaleElementReferenceException.class)
                    .ignoring(Exception.class)
                    .until(ExpectedConditions.elementToBeClickable(element));
            highlightElement(element);
            element.click();
            setTimeOutInSeconds(currentTimeout);
            println(ANSI_YELLOW + " done.");
        } catch (Exception e) {
            setTimeOutInSeconds(currentTimeout);
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
        printf(ANSI_YELLOW + getMethodName() + "[%s]...", locator);
        long currentTimeout = getTimeOutInSeconds();
        try {
            getDriver().manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
            new WebDriverWait(driver, timeOutInSeconds)
                    .ignoring(StaleElementReferenceException.class)
                    .ignoring(Exception.class)
                    .until(ExpectedConditions.elementToBeClickable(locator));
            WebElement element = getDriver().findElement(locator);
            highlightElement(element);
            element.click();
            setTimeOutInSeconds(currentTimeout);
            printf(ANSI_YELLOW + "done.\n");
        } catch (Exception e) {
            setTimeOutInSeconds(currentTimeout);
            printf(ANSI_RED + "failed!!! %s\n", e.getMessage());
            throw new WebDriverException(e.getMessage());
        }
    }

    /** App Management for Mobile devices */
    /**
     * driver.activateApp('com.apple.Preferences');
     * driver.activateApp('io.appium.android.apis');
     * https://appium.io/docs/en/commands/device/app/activate-app/
     * 
     * @param bundleId
     */
    public void activateApp(String bundleId) {
        printf(ANSI_YELLOW + getMethodName() + "...");
        getIosDriver().activateApp(bundleId);
        println(ANSI_YELLOW + " done.");
    }

    /**
     * Install the given app onto the device
     * installApp("/Users/johndoe/path/to/app.apk");
     * https://appium.io/docs/en/commands/device/app/install-app/
     * 
     * @param appPath
     */
    public void installApp(String appPath) {
        printf(ANSI_YELLOW + getMethodName() + "...");
        getMobileDriver().installApp(appPath);
        println(ANSI_YELLOW + " done.");
    }

    /**
     * Launch the app-under-test on the device.
     * If the app-under-test (AUT) is closed, or backgrounded, it will launch it. If
     * the AUT is already open, it will background it and re-launch it. iOS tests
     * with XCUITest can also use the mobile: launchApp method. See detailed
     * documentation.
     * launchApp();
     * https://appium.io/docs/en/commands/device/app/launch-app/
     * 
     * @return
     */
    public void launchApp() {
        println(ANSI_YELLOW + getMethodName() + "...");
        getMobileDriver().launchApp();
    }

    /**
     * Close an app on device
     * https://appium.io/docs/en/commands/device/app/close-app/
     */
    public void closeApp() {
        printf(ANSI_YELLOW + getMethodName() + "...");
        getMobileDriver().closeApp();
        println(ANSI_YELLOW + " done.");
    }

    /**
     * Get app strings
     * https://appium.io/docs/en/commands/device/app/get-app-strings/
     * 
     * @return
     */
    public Map<String, String> getAppStringMap() {
        println(ANSI_YELLOW + getMethodName() + "...");
        return getMobileDriver().getAppStringMap();
    }

    /**
     * Get app strings
     * https://appium.io/docs/en/commands/device/app/get-app-strings/
     * 
     * @return
     */
    public Map<String, String> getAppStringMap(String language) {
        println(ANSI_YELLOW + getMethodName() + "...");
        return getMobileDriver().getAppStringMap(language);
    }

    /**
     * Get app strings
     * https://appium.io/docs/en/commands/device/app/get-app-strings/
     * 
     * @return
     */
    public Map<String, String> getAppStringMap(String language, String stringFile) {
        println(ANSI_YELLOW + getMethodName() + "...");
        return getMobileDriver().getAppStringMap(language, stringFile);
    }

    /**
     * Switch the focus of future commands for this driver to the context with the
     * given name.
     * https://www.selenium.dev/selenium/docs/api/java/org/openqa/selenium/ContextAware.html
     * 
     * @param name
     * @return
     */
    public WebDriver context(String name) {
        printf(ANSI_YELLOW + getMethodName() + " [%s]...", name);
        WebDriver context = getMobileDriver().context(name);
        println(ANSI_YELLOW + " done.");
        return context;
    }

    /**
     * Get contexts for Hybrid Apps.
     * 
     * @return ContextNames: Obtained contexts from Android driver.
     */
    public Set<String> getContextHandles() {
        printf(ANSI_YELLOW + getMethodName() + " ...\n");
        Set<String> contextNames = getMobileDriver().getContextHandles();
        for (String contextName : contextNames) {
            println(ANSI_YELLOW + contextName); // prints out something like NATIVE_APP \n WEBVIEW_1
        }
        return contextNames;
    }

    /**
     * Get contexts for Hybrid Apps.
     * 
     * @return ContextNames: Obtained contexts from Android driver.
     */
    public String getContextHandle() {
        printf(ANSI_YELLOW + getMethodName() + "...");
        String contextName = getMobileDriver().getContext();
        println(ANSI_YELLOW + contextName); // prints out something like NATIVE_APP or WEBVIEW_1
        println(ANSI_YELLOW + " done.");
        return contextName;
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
        JavascriptExecutor js = (JavascriptExecutor) getDriver();
        js.executeScript(script, args);
    }

    /**
     * Execute an asynchronous JavaScript script.
     * 
     * @param script
     * @param args
     */
    public void executeAsynchromousJsScript(String script, String args) {
        JavascriptExecutor js = (JavascriptExecutor) getDriver();
        js.executeAsyncScript(script, args);
    }

    /**
     * Get Value of an element located by.
     * 
     * @param by
     */
    public String getValue(By by) {
        printf(ANSI_YELLOW + getMethodName() + "..." + by);
        long currentTimeout = getTimeOutInSeconds();
        try {
            getDriver().manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
            String text = new WebDriverWait(driver, timeOutInSeconds)
                    .until(ExpectedConditions.presenceOfElementLocated(by))
                    .getAttribute("value");
            printf(ANSI_YELLOW + "done [%s] in object %s\n", text, by);
            setTimeOutInSeconds(currentTimeout);
            return text;
        } catch (Exception e) {
            setTimeOutInSeconds(currentTimeout);
            printf(ANSI_RED + "failed!!! %s\n", e.getMessage());
            throw new WebDriverException(e.getMessage());
        }
    }

    @Nullable
    public String getAutomationName() {
        println(ANSI_YELLOW + getMethodName() + "...");
        return getMobileDriver().getAutomationName();
    }

    public String getDeviceTime() {
        println(ANSI_YELLOW + getMethodName() + "...");
        return getMobileDriver().getDeviceTime();
    }

    @Nullable
    public String getPlatformName() {
        println(ANSI_YELLOW + getMethodName() + "...");
        return getMobileDriver().getPlatformName();
    }

    public Rectangle getRect() {
        println(ANSI_YELLOW + getMethodName() + "...");
        return ((RemoteWebElement) getDriver()).getRect();
    }

    /**
     * Gets an screen snapshot and saves to a target outpùt type.
     * 
     * @param fileWithPath: Pathname of the file to be generated.
     */
    public <X> X getScreenshotAs(OutputType<X> target) {
        return ((RemoteWebDriver) getDriver()).getScreenshotAs(target);
    }

    /**
     * Gets an screen snapshot and saves to a file.
     * 
     * @param fileWithPath: Pathname of the file to be generated.
     */
    public void getScreenshotAs(String fileWithPath) throws Exception {
        printf(ANSI_YELLOW + "getScreenshotAs..." + fileWithPath);

        // Convert web driver object to TakeScreenshot
        TakesScreenshot scrShot = ((TakesScreenshot) getDriver());
        // Call getScreenshotAs method to create image file
        File SrcFile = scrShot.getScreenshotAs(OutputType.FILE);
        // Move image file to new destination
        File DestFile = new File(fileWithPath);
        // Copy file at destination
        FileUtils.copyFile(SrcFile, DestFile);
        println(ANSI_YELLOW + " done.");
    }

    /***
     * Takes an screenshot and leaves it in file named TestResults\Name-Date.png
     * 
     * @param Name: The name you want to recognize.
     * @throws Exception
     */
    public void getScreenSnapshotWithDate(String name) throws Exception {
        printf(ANSI_YELLOW + "getScreenSnapshotWithDate...");
        Boolean currentDebug = getDebugMode();
        SetDebugModeOFF();

        LocalDateTime ldt = LocalDateTime.now();
        String date = ldt.toString().replaceAll("\\W+", "");
        try {
            printf(ANSI_YELLOW + "TestResults\\" + name + "-" + date + ".png");
            getScreenshotAs("TestResults\\" + name + "-" + date + ".png");
        } catch (Exception e) {
            throw new Exception(ANSI_RED + "ERROR on snapshot. Stack:" + e.getMessage());
        }
        if (currentDebug)
            SetDebugModeON();
        println(ANSI_YELLOW + " done.");
    }

    @Nullable
    public Object getSessionDetail(String detail) {
        println(ANSI_YELLOW + getMethodName() + "...");
        return getMobileDriver().getSessionDetail(detail);
    }

    public Map<String, Object> getSessionDetails() {
        println(ANSI_YELLOW + getMethodName() + "...");
        return getMobileDriver().getSessionDetails();
    }

    public void hideKeyboard() {
        println(ANSI_YELLOW + getMethodName() + "...");
        getMobileDriver().hideKeyboard();
    }

    /**
     * Check whether the specified app is installed on the device
     * isAppInstalled("com.example.AppName");
     * https://appium.io/docs/en/commands/device/app/is-app-installed/
     * 
     * @param bundleId
     * @return
     */
    public boolean isAppInstalled(String bundleId) {
        println(ANSI_YELLOW + getMethodName() + "...");
        return getMobileDriver().isAppInstalled(bundleId);
    }

    public ApplicationState queryAppState(String bundleId) {
        println(ANSI_YELLOW + getMethodName() + "...");
        return getMobileDriver().queryAppState(bundleId);
    }

    public boolean removeApp(String bundleId) {
        println(ANSI_YELLOW + getMethodName() + "...");
        return getMobileDriver().removeApp(bundleId);
    }

    public void resetApp() {
        println(ANSI_YELLOW + getMethodName() + "...");
        getMobileDriver().resetApp();
    }

    public void runAppInBackground(Duration duration) {
        println(ANSI_YELLOW + getMethodName() + "...");
        getMobileDriver().runAppInBackground(duration);
    }

    public boolean terminateApp(String bundleId) {
        println(ANSI_YELLOW + getMethodName() + "...");
        return getMobileDriver().terminateApp(bundleId);
    }

    /**
     * https://appium.github.io/java-client/io/appium/java_client/AppiumDriver.html#isBrowser--
     * 
     * @return
     */
    public boolean isBrowser() {
        println(ANSI_YELLOW + getMethodName() + "...");
        return getMobileDriver().isBrowser();
    }

    /**
     * Is this element displayed or not? This method avoids the problem of having to
     * parse an element's "style" attribute.
     * This method will not fail if element is not found, instead will return false.
     * 
     * @param locator
     * @return
     */
    public boolean isDisplayed(By locator) {
        println(ANSI_YELLOW + getMethodName() + "...");
        WebElement element = getDriver().findElement(locator);
        if (element != null) {
            return element.isDisplayed();
        } else {
            return false;
        }
    }

    /**
     * Is the element currently enabled or not? This will generally return true for
     * everything but disabled input elements.
     * This method will not fail if element is not found, instead will return false.
     * 
     * @param locator
     * @return
     */
    public boolean isEnabled(By locator) {
        println(ANSI_YELLOW + getMethodName() + "...");
        WebElement element = getDriver().findElement(locator);
        if (element != null) {
            return element.isEnabled();
        } else {
            return false;
        }
    }

    /**
     * Determine whether or not this element is selected or not. This operation only
     * applies to input elements such as checkboxes, options in a select and radio
     * buttons.
     * For more information on which elements this method supports, refer to the
     * specification.
     * 
     * @param locator
     * @return
     */
    public boolean isSelected(By locator) {
        println(ANSI_YELLOW + getMethodName() + "...");
        WebElement element = getDriver().findElement(locator);
        if (element != null) {
            return element.isSelected();
        } else {
            return false;
        }
    }

    /**
     * What is the width and height of the rendered element?
     * Method will fail if element not found.
     * 
     * @param locator
     * @return
     * @throws Exception
     */
    public Dimension getSize(By locator) throws Exception {
        println(ANSI_YELLOW + getMethodName() + "...");
        WebElement element = getDriver().findElement(locator);
        if (element != null) {
            return element.getSize();
        } else {
            throw new Exception("ERROR: Element " + locator + "not found.");
        }
    }

    /**
     * Get the tag name of this element. Not the value of the name attribute: will
     * return "input" for the element <input name="foo" />.
     * Method will fail if element not found.
     * 
     * @param locator
     * @return
     * @throws Exception
     */
    public String getTagName(By locator) throws Exception {
        println(ANSI_YELLOW + getMethodName() + "...");
        WebElement element = getDriver().findElement(locator);
        if (element != null) {
            return element.getTagName();
        } else {
            throw new Exception("ERROR: Element " + locator + "not found.");
        }
    }

    /**
     * Gets the physical location (geoloc) of the browser.
     * 
     * @return
     */
    public Location location() {
        println(ANSI_YELLOW + getMethodName() + "...");
        return getMobileDriver().location();
    }

    /**
     * Performs multiple TouchAction gestures at the same time, to simulate multiple
     * fingers/touch inputs.
     * See the Webriver 3 spec
     * https://dvcs.w3.org/hg/webdriver/raw-file/default/webdriver-spec.html
     * It's more convenient to call the perform() method of the MultiTouchAction
     * object.
     * All the existing multi touch actions will be wiped out after this method is
     * called.
     * 
     * @param multiAction
     */
    public void performMultiTouchAction(MultiTouchAction multiAction) {
        println(ANSI_YELLOW + getMethodName() + "...");
        getMobileDriver().performMultiTouchAction(multiAction);
    }

    /**
     * Pull a file from the simulator/device. On iOS the server should have ifuse
     * libraries installed and configured properly for this feature to work on real
     * devices.
     * On Android the application under test should be built with debuggable flag
     * enabled in order to get access to its container on the internal file system.
     * 
     * @param remotePath
     * @return
     */
    public byte[] pullFile(String remotePath) {
        println(ANSI_YELLOW + getMethodName() + "...");
        return getMobileDriver().pullFile(remotePath);
    }

    /**
     * Pull a folder content from the simulator/device. On iOS the server should
     * have ifuse libraries installed and configured properly for this feature to
     * work on real devices.
     * On Android the application under test should be built with debuggable flag
     * enabled in order to get access to its container on the internal file system.
     * 
     * @param remotePath
     * @return
     */
    public byte[] pullFolder(String remotePath) {
        println(ANSI_YELLOW + getMethodName() + "...");
        return getMobileDriver().pullFolder(remotePath);
    }

    /**
     * Set the current browser rotation on device.
     * 
     * @return
     */
    public void rotate(ScreenOrientation orientation) {
        println(ANSI_YELLOW + getMethodName() + "..." + orientation);
        getMobileDriver().rotate(orientation);
    }

    /**
     * Set the current browser rotation on device.
     * 
     * @return
     */
    public void rotate(DeviceRotation rotation) {
        println(ANSI_YELLOW + getMethodName() + "...X=" + rotation.getX() + " Y=" + rotation.getY() + " Z="
                + rotation.getZ());
        getMobileDriver().rotate(rotation);
    }

    /**
     * Get the current browser rotation on device.
     * 
     * @return
     */
    public DeviceRotation rotation() {
        println(ANSI_YELLOW + getMethodName() + "...");
        return getMobileDriver().rotation();
    }

    /**
     * Simulates typing Keys over an editable element located By.
     * 
     * @param locator
     * @param keys
     */
    public void sendKeys(By locator, String keys) {
        printf(ANSI_YELLOW + getMethodName() + " %s to object %s...", keys, locator);
        long currentTimeout = getTimeOutInSeconds();
        try {
            getDriver().manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
            new WebDriverWait(driver, timeOutInSeconds).ignoring(StaleElementReferenceException.class)
                    .ignoring(Exception.class).until(ExpectedConditions.elementToBeClickable(locator));
            WebElement textBox = getDriver().findElement(locator);
            highlightElement(textBox);
            if (textBox == null) {
                throw new Exception(ANSI_RED + " Can not find element " + locator);
            }
            textBox.sendKeys(keys);
            setTimeOutInSeconds(currentTimeout);
            println(ANSI_YELLOW + "done!");
        } catch (Exception e) {
            setTimeOutInSeconds(currentTimeout);
            printf(ANSI_RED + "failed! %s\n", e.getMessage());
            throw new WebDriverException(e.getMessage());
        }
    }

    /**
     * Simulates typing Keys over an editable web element.
     * 
     * @param locator
     * @param keys
     */
    public void sendKeys(WebElement textBox, String keys) {
        printf(ANSI_YELLOW + getMethodName() + " %s to element...", keys);
        try {
            highlightElement(textBox);
            textBox.sendKeys(keys);
            println(ANSI_YELLOW + "done!");
        } catch (Exception e) {
            printf(ANSI_RED + "failed! %s\n", e.getMessage());
            throw new WebDriverException(e.getMessage());
        }
    }

    /**
     * Sets the physical location.
     * 
     * @param location
     */
    public void setLocation(Location location) {
        println(ANSI_YELLOW + getMethodName() + "...");
        getMobileDriver().setLocation(location);
    }

    /**
     * Get the value of a given CSS property.
     * Color values should be returned as rgba strings, so,
     * for example if the "background-color" property is set as "green" in the
     * HTML source, the returned value will be "rgba(0, 255, 0, 1)".
     *
     * Note that shorthand CSS properties (e.g. background, font, border,
     * border-top, margin,
     * margin-top, padding, padding-top, list-style, outline, pause, cue) are not
     * returned,
     * in accordance with the
     * <a href=
     * "http://www.w3.org/TR/DOM-Level-2-Style/css.html#CSS-CSSStyleDeclaration">DOM
     * CSS2 specification</a>
     * - you should directly access the longhand properties (e.g. background-color)
     * to access the
     * desired values.
     *
     * @param propertyName the css property name of the element
     * @return The current, computed value of the property.
     */
    public String getCssValue(WebElement element, String propertyName) {
        println(ANSI_YELLOW + getMethodName() + "...");
        return element.getCssValue(propertyName);
    }

    /**
     * Where on the page is the top left-hand corner of the rendered element?
     *
     * @return A point, containing the location of the top left-hand corner of the
     *         element
     */
    public Point getLocation(WebElement element) {
        println(ANSI_YELLOW + getMethodName() + "...");
        return element.getLocation();
    }

    /**
     * If this current element is a form, or an element within a form, then this
     * will be submitted to
     * the remote server. If this causes the current page to change, then this
     * method will block until
     * the new page is loaded.
     *
     * @throws NoSuchElementException If the given element is not within a form
     */
    public void submit(WebElement element) {
        println(ANSI_YELLOW + getMethodName() + "..." + getWebElementIdentification(element));
        element.submit();
    }

    /**
     * Establish a unified timeout parameter for implicit waits, page loads and
     * javascripts.
     * 
     * @param timeOutInSeconds
     */
    void setTimeouts(long timeOutInSeconds) {
        setTimeOutInSeconds(timeOutInSeconds);
    }

    /**
     * Get a unified timeout parameter for implicit waits, page loads and
     * javascripts.
     * 
     * @param timeOutInSeconds
     */
    long getTimeouts() {
        return getTimeOutInSeconds();
    }

    /**
     * Creates a new window, selects it and returns the handler.
     * 
     * @param windowHandle
     */
    public String createWindow() {
        printf(ANSI_YELLOW + getMethodName() + "...");
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
                    getDriver().switchTo().window(newWindow);
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
        printf(ANSI_YELLOW + getMethodName() + "...");
        try {
            getDriver().switchTo().window(windowHandle);
            printf(ANSI_YELLOW + "done... Window title %s\n", getDriver().getTitle());
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
        println(ANSI_YELLOW + getMethodName() + "...");
        return waitForNewWindow(timeOutInSeconds); // Uses standard timeout value.
    }

    /**
     * Waits for a new window to be opened and returns its handle.
     * 
     * @timeoutSeconds: Max time to wait for new window.
     * @return
     */
    public String waitForNewWindow(long timeoutSeconds) {
        printf(ANSI_YELLOW + getMethodName() + "...");
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
                    getDriver().switchTo().window(newWindow);
                    printf(ANSI_YELLOW + "done... Window title %s\n", getDriver().getTitle());
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
        println(ANSI_YELLOW + getMethodName() + "..." + NWindows);
        waitForNWindows(NWindows, timeOutInSeconds);
    }

    /**
     * Waits until the number of opened windows is equal to NWindows.
     */
    public void waitForNWindows(int NWindows, long timeoutSeconds) {
        println(ANSI_YELLOW + getMethodName() + "..." + NWindows);
        try {
            Stopwatch stopwatch = Stopwatch.createStarted();
            while (getDriver().getWindowHandles().size() != NWindows) {
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
    public void closeCurrentWindow() {
        println(ANSI_YELLOW + getMethodName() + "...");
        getDriver().close();
    }

    /**
     * Get Text of a web element.
     * 
     * @param element
     * @throws Exception
     */
    public String getText(WebElement element) throws Exception {
        printf(ANSI_YELLOW + getMethodName() + "[%s]...", getWebElementIdentification(element));
        long currentTimeout = getTimeOutInSeconds();
        try {
            highlightElement(element);
            getDriver().manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
            String text = element.getText();
            printf(ANSI_YELLOW + "done [%s].\n", text);
            setTimeOutInSeconds(currentTimeout);
            return text;
        } catch (Exception e) {
            setTimeOutInSeconds(currentTimeout);
            printf(ANSI_RED + "failed!!!\n", e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    /**
     * Get Text of an element located by.
     * 
     * @param by
     * @throws Exception
     */
    public String getText(By by) throws Exception {
        printf(ANSI_YELLOW + getMethodName() + "[%s]...", by);
        long currentTimeout = getTimeOutInSeconds();
        try {
            getDriver().manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
            String text = new WebDriverWait(driver, timeOutInSeconds)
                    .until(ExpectedConditions.presenceOfElementLocated(by))
                    .getText();
            printf(ANSI_YELLOW + "done [%s].\n", text, by);
            setTimeOutInSeconds(currentTimeout);
            return text;
        } catch (Exception e) {
            setTimeOutInSeconds(currentTimeout);
            printf(ANSI_RED + "failed!!!\n", e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    /**
     * Get Attribute of an element located by.
     * 
     * @param by
     * @param attributeName
     * @throws Exception
     */
    public String getAttribute(By by, String attributeName) throws Exception {
        printf(ANSI_YELLOW + "getAttribute [" + attributeName + "]...");
        long currentTimeout = getTimeOutInSeconds();
        try {
            getDriver().manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
            String text = new WebDriverWait(driver, timeOutInSeconds)
                    .until(ExpectedConditions.presenceOfElementLocated(by))
                    .getAttribute(attributeName);
            printf(ANSI_YELLOW + "done [%s] in object %s\n", text, by);
            setTimeOutInSeconds(currentTimeout);
            return text;
        } catch (Exception e) {
            setTimeOutInSeconds(currentTimeout);
            printf(ANSI_RED + "failed!!! %s\n", e.getMessage());
            throw new Exception(e.getMessage());
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
        printf(ANSI_YELLOW + getMethodName() + "...");
        long currentTimeout = getTimeouts();
        try {
            getDriver().manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
            new WebDriverWait(driver, timeOutInSeconds).until(ExpectedConditions.presenceOfElementLocated(by))
                    .getAttribute(attributeName);
            setTimeouts(currentTimeout);
            WebElement element = getDriver().findElement(by);
            highlightElement(element);

            executeJsScript("arguments[0].setAttribute(arguments[1], arguments[2]);", element, attributeName, value);
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
        printf(ANSI_YELLOW + getMethodName() + " %d...", frameHandle);
        long currentTimeout = getTimeouts();
        try {
            getDriver().manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
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
        printf(ANSI_YELLOW + getMethodName() + " %s...", frameLocator);
        long currentTimeout = getTimeouts();
        try {
            getDriver().manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
            new WebDriverWait(driver, timeOutInSeconds).ignoring(StaleElementReferenceException.class)
                    .ignoring(WebDriverException.class)
                    .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameLocator));
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
        printf(ANSI_YELLOW + getMethodName() + " %s...", frameID);
        long currentTimeout = getTimeouts();
        try {
            getDriver().manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
            new WebDriverWait(driver, timeOutInSeconds)
                    .ignoring(StaleElementReferenceException.class)
                    .ignoring(WebDriverException.class)
                    .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameID));
            setTimeouts(currentTimeout);
            printf(ANSI_YELLOW + "done...\n");
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
        printf(ANSI_YELLOW + getMethodName() + " %s...", locator);
        try {
            WebElement element = getDriver().findElement(locator);
            highlightElement(element);
            clickJS(element);
        } catch (Exception e) {
            printf(ANSI_RED + "failed!!! %s\n", e.getMessage());
            throw new WebDriverException(e.getMessage());
        }
    }

    /**
     * Click in a clickable web element bypassing webdriver using
     * js executor.
     * 
     * @param locator
     */
    public void clickJS(WebElement element) {
        printf(ANSI_YELLOW + getMethodName() + " %s...", getWebElementIdentification(element));
        try {
            highlightElement(element);
            // Do not add waits.
            executeJsScript("arguments[0].click();", element);
            printf(ANSI_YELLOW + "done.\n");
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
        printf(ANSI_YELLOW + getMethodName() + " %s...", locator);
        long currentTimeout = getTimeouts();
        try {
            getDriver().manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
            new WebDriverWait(driver, timeOutInSeconds)
                    .ignoring(StaleElementReferenceException.class)
                    .ignoring(WebDriverException.class)
                    .until(ExpectedConditions.elementToBeClickable(locator));

            WebElement element = getDriver().findElement(locator);
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
        printf(ANSI_YELLOW + getMethodName() + " %s...", locator);
        long currentTimeout = getTimeouts();
        try {
            getDriver().manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
            new WebDriverWait(driver, timeOutInSeconds)
                    .ignoring(StaleElementReferenceException.class)
                    .ignoring(WebDriverException.class)
                    .until(ExpectedConditions.visibilityOfElementLocated(locator));
            WebElement element = getDriver().findElement(locator);
            highlightElement(element);
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
        printf(ANSI_YELLOW + getMethodName() + " %s...", getWebElementIdentification(webElement));
        long currentTimeout = getTimeouts();
        try {
            highlightElement(webElement);
            getDriver().manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
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
        printf(ANSI_YELLOW + getMethodName() + " %s...", locator);
        long currentTimeout = getTimeouts();
        try {
            getDriver().manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
            new WebDriverWait(driver, timeOutInSeconds)
                    .ignoring(StaleElementReferenceException.class)
                    .ignoring(WebDriverException.class)
                    .until(ExpectedConditions.elementToBeClickable(locator));

            WebElement element = getDriver().findElement(locator);
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
        printf(ANSI_YELLOW + getMethodName() + " %s...", locator);
        long currentTimeout = getTimeouts();
        try {
            getDriver().manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
            new WebDriverWait(driver, timeOutInSeconds)
                    .ignoring(StaleElementReferenceException.class)
                    .ignoring(WebDriverException.class)
                    .until(ExpectedConditions.elementToBeClickable(locator));

            WebElement element = getDriver().findElement(locator);
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
        printf(ANSI_YELLOW + "DragAndDrop %s over %s...", getWebElementIdentification(sourceObject),
                getWebElementIdentification(targetObject));
        long currentTimeout = getTimeouts();
        try {
            highlightElement(sourceObject);
            highlightElement(targetObject);
            List<WebElement> elements = new ArrayList<WebElement>();
            elements.add(sourceObject);
            elements.add(targetObject);
            getDriver().manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
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
        printf(ANSI_YELLOW + "clickAt %s x=%d y=%d", locator, x, y);
        long currentTimeout = getTimeouts();
        try {
            getDriver().manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
            new WebDriverWait(driver, timeOutInSeconds).ignoring(StaleElementReferenceException.class)
                    .ignoring(WebDriverException.class).until(ExpectedConditions.elementToBeClickable(locator));
            WebElement element = getDriver().findElement(locator);
            highlightElement(element);
            new Actions(driver).moveToElement(element, x, y).click().perform();
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
        printf(ANSI_YELLOW + getMethodName() + " %s on %d seconds...", locator.toString(), timeout);
        long currentTimeout = getTimeouts();
        try {
            getDriver().manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
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
        printf(ANSI_YELLOW + getMethodName() + " %s...", locator);
        long currentTimeout = getTimeouts();
        try {
            getDriver().manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
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
        printf(ANSI_YELLOW + getMethodName() + " %s on %d seconds...", locator.toString(), timeout);
        long currentTimeout = getTimeouts();
        try {
            getDriver().manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
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
        printf(ANSI_YELLOW + getMethodName() + " %s on %d seconds...", getWebElementIdentification(webElement),
                timeout);
        long currentTimeout = getTimeouts();
        try {
            implicitlyWait(0, TimeUnit.SECONDS);
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
        printf(ANSI_YELLOW + getMethodName() + " %s...", getWebElementIdentification(webElement));
        long currentTimeout = getTimeouts();
        try {
            getDriver().manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
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
        printf(ANSI_YELLOW + getMethodName() + " %s in element %s...", text, locator.toString());
        long currentTimeout = getTimeouts();
        try {
            getDriver().manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
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
        printf(ANSI_YELLOW + getMethodName() + " %s in element %s...", text, locator.toString());
        long currentTimeout = getTimeouts();
        try {
            getDriver().manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
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
        printf(ANSI_YELLOW + getMethodName() + " %s...", text);
        long currentTimeout = getTimeouts();
        try {
            By by = By.xpath("//*[@text='" + text + "']");
            getDriver().manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
            new WebDriverWait(driver, timeOutInSeconds).ignoring(StaleElementReferenceException.class)
                    .ignoring(WebDriverException.class)
                    .until(ExpectedConditions.presenceOfAllElementsLocatedBy(by));

            if (getDriver().getPageSource().contains(text)) {
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
     * Waits for alert present and Clicks OK if present during timeout period.
     */
    public void clickOKOnAlert() {
        try {
            printf(ANSI_YELLOW + getMethodName() + "...");
            waitForAlertPresent(timeOutInSeconds);
            Alert alert = getDriver().switchTo().alert();
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
            printf(ANSI_YELLOW + getMethodName() + "...");
            waitForAlertPresent(timeOutInSeconds);
            Alert alert = getDriver().switchTo().alert();
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
            printf(ANSI_YELLOW + getMethodName() + "...");
            waitForAlertPresent(timeOutInSeconds);
            printf(ANSI_YELLOW + "Present!  Now getting text!!..");
            Alert alert = getDriver().switchTo().alert();
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
        printf(ANSI_YELLOW + getMethodName() + "...");
        waitForAlertPresent(timeOutInSeconds);
        try {
            Alert alert = getDriver().switchTo().alert();
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
        printf(ANSI_YELLOW + getMethodName() + " in %d seconds...", timeOutInSeconds);
        long currentTimeout = getTimeouts();

        try {
            implicitlyWait(0, TimeUnit.SECONDS);
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

        if (getDebugMode()) {
            try {
                executeJsScript("arguments[0].setAttribute('style', 'background: yellow; border: 3px solid blue;');",
                        element);
            } catch (Exception e) {
                printf(ANSI_GREEN + "WARNING: Can't highlight. ");
            }

        }

    }

    /**
     * Selects an option from by using VisibleText.
     * 
     * @param selectObject
     * @param VisibleText
     */
    public void selectOptionByVisibleText(By selectObject, String VisibleText) {
        printf(ANSI_YELLOW + getMethodName() + " from %s option %s...", selectObject, VisibleText);
        try {
            highlightElement(getDriver().findElement(selectObject));
            Select dropdown = new Select(getDriver().findElement(selectObject));
            dropdown.selectByVisibleText(VisibleText);
            println(ANSI_YELLOW + " done.");
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
        printf(ANSI_YELLOW + getMethodName() + " from %s index %s\n", by, index);
        try {
            highlightElement(getDriver().findElement(by));
            Select dropdown = new Select(getDriver().findElement(by));
            dropdown.selectByIndex(index);
            println(ANSI_YELLOW + " done.");
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
        printf(ANSI_YELLOW + getMethodName() + " from %s value %s...", by, value);
        try {
            Select dropdown = new Select(getDriver().findElement(by));
            highlightElement(getDriver().findElement(by));
            dropdown.selectByValue(value);
            println(ANSI_YELLOW + " done.");
        } catch (Exception e) {
            printf(ANSI_RED + "ERROR: option not selected. %s\n", e.getMessage());
            throw new RuntimeException();
        }

    }

    /**
     * Sets the physical location.
     * 
     * @param x
     * @param y
     * @param z
     */
    public void setDeviceLocation(double x, double y, double z) {
        printf(ANSI_YELLOW + getMethodName() + " to %f, %f, %f...", x, y, z);
        try {
            ((ChromeDriver) getDriver()).setLocation(new Location(x, y, z));

            printf(ANSI_YELLOW + "done!!!\n");
        } catch (Exception e) {
            printf(ANSI_RED + "failed!!! %s\n", e.getMessage());
            throw new WebDriverException(e.getMessage());
        }
    }

    /**
     * Waits for a a full page load by reading document.readyState.
     */
    public void waitForPageToLoad() {
        printf(ANSI_YELLOW + getMethodName() + "...");
        try {
            String pageLoadStatus = null;
            do {
                JavascriptExecutor js = (JavascriptExecutor) getDriver();
                pageLoadStatus = (String) js.executeScript("return document.readyState");
            } while (!pageLoadStatus.equals("complete"));
            printf(ANSI_YELLOW + "done.\n");
        } catch (Exception e) {
            printf(ANSI_RED + "failed. Stack=%s\n", e.getMessage());
            throw new WebDriverException(e.getMessage());
        }
    }

    /**
     * Waits till element by is invisible.
     * 
     * @param element
     */
    public void waitForElementInvisibility(By element) {
        printf(ANSI_YELLOW + getMethodName() + " %s...", element);
        long currentTimeout = getTimeouts();
        try {
            getDriver().manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
            new WebDriverWait(driver, timeOutInSeconds)
                    .until(ExpectedConditions.invisibilityOf(getDriver().findElement(element)));
            printf(ANSI_YELLOW + "invisible!!!\n");
            setTimeouts(currentTimeout);
        } catch (Exception e) {
            printf(ANSI_RED + "failed!!! %s\n", e.getMessage());
            setTimeouts(currentTimeout);
            throw new WebDriverException(e.getMessage());
        }
    }

    /**
     * Waits till element by is invisible.
     * 
     * @param element
     */
    public void waitForElementInvisibility(WebElement element) {
        printf(ANSI_YELLOW + getMethodName() + " %s...", getWebElementIdentification(element));
        long currentTimeout = getTimeouts();
        try {
            getDriver().manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
            new WebDriverWait(driver, timeOutInSeconds).until(ExpectedConditions.invisibilityOf(element));
            printf(ANSI_YELLOW + "invisible!!!\n");
            setTimeouts(currentTimeout);
        } catch (Exception e) {
            printf(ANSI_RED + "failed!!! %s\n", e.getMessage());
            setTimeouts(currentTimeout);
            throw new WebDriverException(e.getMessage());
        }
    }

    /**
     * Waits till an element is clickable and fail if timeout is reached.
     * 
     * @param by
     */
    public void waitForElementToBeClickable(By by) {
        printf(ANSI_YELLOW + getMethodName() + " %s...", by);
        long currentTimeout = getTimeouts();
        try {
            getDriver().manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
            new WebDriverWait(driver, timeOutInSeconds).until(ExpectedConditions.elementToBeClickable(by));
            printf(ANSI_YELLOW + "Clickeable!!!\n");
            setTimeouts(currentTimeout);
        } catch (Exception e) {
            printf(ANSI_RED + "failed!!! %s\n", e.getMessage());
            setTimeouts(currentTimeout);
            throw new WebDriverException(e.getMessage());
        }
    }

    /**
     * Waits a selectable element (e.g. a list option) is in state of selected.
     * 
     * @param chkDO
     * @param timeOut
     * @return
     */
    public boolean waitForWebElementSelected(By chkDO, long timeOut) {
        printf(ANSI_YELLOW + getMethodName() + " %s...", chkDO);
        long currentTimeout = getTimeouts();
        try {
            getDriver().manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
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

    /** Navigate interface */

    /**
     * Move back a single "item" in the browser's history.
     */
    public void back() {
        println(ANSI_YELLOW + getMethodName() + "...");
        getDriver().navigate().back();
    }

    /**
     * Move a single "item" forward in the browser's history. Does nothing if we are
     * on the latest
     * page viewed.
     */
    public void forward() {
        println(ANSI_YELLOW + getMethodName() + "...");
        getDriver().navigate().forward();
    }

    /**
     * Load a new web page in the current browser window. This is done using an HTTP
     * GET operation,
     * and the method will block until the load is complete. This will follow
     * redirects issued
     * either by the server or as a meta-redirect from within the returned HTML.
     * Should a
     * meta-redirect "rest" for any duration of time, it is best to wait until this
     * timeout is over,
     * since should the underlying page change whilst your test is executing the
     * results of future
     * calls against this interface will be against the freshly loaded page.
     *
     * @param url The URL to load. It is best to use a fully qualified URL
     */
    public void to(String url) {
        println(ANSI_YELLOW + getMethodName() + "..." + url);
        getDriver().navigate().to(url);
    }

    /**
     * Overloaded version of {@link #to(String)} that makes it easy to pass in a
     * URL.
     *
     * @param url URL
     */
    public void to(URL url) {
        println(ANSI_YELLOW + getMethodName() + "..." + url);
        getDriver().navigate().to(url);
    }

    /**
     * Refresh the current page
     */
    public void refresh() {
        println(ANSI_YELLOW + getMethodName() + "...");
        getDriver().navigate().refresh();
    }

    /** Manage interface */
    /**
     * Add a specific cookie. If the cookie's domain name is left blank, it is
     * assumed that the
     * cookie is meant for the domain of the current document.
     *
     * @param cookie The cookie to add.
     */
    public void addCookie(Cookie cookie) {
        println(ANSI_YELLOW + getMethodName() + "..." + cookie);
        getDriver().manage().addCookie(cookie);
    }

    /**
     * Delete the named cookie from the current domain. This is equivalent to
     * setting the named
     * cookie's expiry date to some time in the past.
     *
     * @param name The name of the cookie to delete
     */
    public void deleteCookieNamed(String name) {
        println(ANSI_YELLOW + getMethodName() + "..." + name);
        getDriver().manage().deleteCookieNamed(name);
    }

    /**
     * Delete a cookie from the browser's "cookie jar". The domain of the cookie
     * will be ignored.
     *
     * @param cookie nom nom nom
     */
    public void deleteCookie(Cookie cookie) {
        println(ANSI_YELLOW + getMethodName() + "..." + cookie);
        getDriver().manage().deleteCookie(cookie);
    }

    /**
     * Delete all the cookies for the current domain.
     */
    public void deleteAllCookies() {
        println(ANSI_YELLOW + getMethodName() + "...");
        getDriver().manage().deleteAllCookies();
    }

    /**
     * Get all the cookies for the current domain. This is the equivalent of calling
     * "document.cookie" and parsing the result
     *
     * @return A Set of cookies for the current domain.
     */
    public Set<Cookie> getCookies() {
        println(ANSI_YELLOW + getMethodName() + "...");
        return getDriver().manage().getCookies();
    }

    /**
     * Get a cookie with a given name.
     *
     * @param name the name of the cookie
     * @return the cookie, or null if no cookie with the given name is present
     */
    public Cookie getCookieNamed(String name) {
        println(ANSI_YELLOW + getMethodName() + "..." + name);
        return getDriver().manage().getCookieNamed(name);
    }

    /** Timeouts interface */
    /**
     * Specifies the amount of time the driver should wait when searching for an
     * element if it is not immediately present.
     * When searching for a single element, the driver should poll the page until
     * the element has been found, or this timeout expires before throwing a
     * {@link NoSuchElementException}.
     * When searching for multiple elements, the driver should poll the page until
     * at least one element has been found or this timeout has expired.
     * 
     * Increasing the implicit wait timeout should be used judiciously as it will
     * have an adverse effect on test run time, especially when used with slower
     * location strategies like XPath.
     *
     * @param time The amount of time to wait.
     * @param unit The unit of measure for {@code time}.
     * @return A self reference.
     */
    public Timeouts implicitlyWait(long time, TimeUnit unit) {
        println(ANSI_YELLOW + getMethodName() + "..." + time + " " + unit);
        return getDriver().manage().timeouts().implicitlyWait(time, unit);
    }

    /**
     * Sets the amount of time to wait for an asynchronous script to finish
     * execution before
     * throwing an error. If the timeout is negative, then the script will be
     * allowed to run
     * indefinitely.
     *
     * @param time The timeout value.
     * @param unit The unit of time.
     * @return A self reference.
     * @see JavascriptExecutor#executeAsyncScript(String, Object...)
     */
    public Timeouts setScriptTimeout(long time, TimeUnit unit) {
        println(ANSI_YELLOW + getMethodName() + "..." + time + " " + unit);
        return getDriver().manage().timeouts().setScriptTimeout(time, unit);
    }

    /**
     * Sets the amount of time to wait for a page load to complete before throwing
     * an error.
     * If the timeout is negative, page loads can be indefinite.
     *
     * @param time The timeout value.
     * @param unit The unit of time.
     * @return A Timeouts interface.
     */
    public Timeouts pageLoadTimeout(long time, TimeUnit unit) {
        println(ANSI_YELLOW + getMethodName() + "..." + time + " " + unit);
        return getDriver().manage().timeouts().pageLoadTimeout(time, unit);
    }

    /** switchTo interface */
    /**
     * Select a frame by its (zero-based) index. Selecting a frame by index is
     * equivalent to the
     * JS expression window.frames[index] where "window" is the DOM window
     * represented by the
     * current context. Once the frame has been selected, all subsequent calls on
     * the WebDriver
     * interface are made to that frame.
     *
     * @param index (zero-based) index
     * @return This driver focused on the given frame
     * @throws NoSuchFrameException If the frame cannot be found
     */
    public WebDriver switchToFrame(int index) {
        println(ANSI_YELLOW + getMethodName() + "...index=" + index);
        return getDriver().switchTo().frame(index);
    }

    /**
     * Select a frame by its name or ID. Frames located by matching name attributes
     * are always given
     * precedence over those matched by ID.
     *
     * @param nameOrId the name of the frame window, the id of the &lt;frame&gt; or
     *                 &lt;iframe&gt;
     *                 element, or the (zero-based) index
     * @return This driver focused on the given frame
     * @throws NoSuchFrameException If the frame cannot be found
     */
    public WebDriver switchToFrame(String nameOrId) {
        println(ANSI_YELLOW + getMethodName() + "..." + nameOrId);
        return getDriver().switchTo().frame(nameOrId);
    }

    /**
     * Select a frame using its previously located {@link WebElement}.
     *
     * @param frameElement The frame element to switch to.
     * @return This driver focused on the given frame.
     * @throws NoSuchFrameException           If the given element is neither an
     *                                        IFRAME nor a FRAME element.
     * @throws StaleElementReferenceException If the WebElement has gone stale.
     * @see WebDriver#findElement(By)
     */
    public WebDriver switchToFrame(WebElement frameElement) {
        println(ANSI_YELLOW + getMethodName() + "..." + frameElement);
        return getDriver().switchTo().frame(frameElement);
    }

    /**
     * Change focus to the parent context. If the current context is the top level
     * browsing context,
     * the context remains unchanged.
     *
     * @return This driver focused on the parent frame
     */
    public WebDriver switchToParentFrame() {
        println(ANSI_YELLOW + getMethodName() + "...");
        return getDriver().switchTo().parentFrame();
    }

    /**
     * Switch the focus of future commands for this driver to the window with the
     * given name/handle.
     *
     * @param nameOrHandle The name of the window or the handle as returned by
     *                     {@link WebDriver#getWindowHandle()}
     * @return This driver focused on the given window
     * @throws NoSuchWindowException If the window cannot be found
     */
    public WebDriver switchToWindow(String nameOrHandle) {
        println(ANSI_YELLOW + getMethodName() + "..." + nameOrHandle);
        return getDriver().switchTo().window(nameOrHandle);
    }

    /**
     * Selects either the first frame on the page, or the main document when a page
     * contains
     * iframes.
     *
     * @return This driver focused on the top window/first frame.
     */
    public WebDriver switchToDefaultContent() {
        println(ANSI_YELLOW + getMethodName() + "...");
        return getDriver().switchTo().defaultContent();
    }

    /**
     * Switches to the element that currently has focus within the document
     * currently "switched to",
     * or the body element if this cannot be detected. This matches the semantics of
     * calling
     * "document.activeElement" in Javascript.
     *
     * @return The WebElement with focus, or the body element if no element with
     *         focus can be
     *         detected.
     */
    public WebElement switchToActiveElement() {
        println(ANSI_YELLOW + getMethodName() + "...");
        return getDriver().switchTo().activeElement();
    }

    /**
     * Switches to the currently active modal dialog for this particular driver
     * instance.
     *
     * @return A handle to the dialog.
     * @throws NoAlertPresentException If the dialog cannot be found
     */
    public Alert switchToAlert() {
        println(ANSI_YELLOW + getMethodName() + "...");
        return getDriver().switchTo().alert();
    }

    /**
     * Interface for controlling IME engines to generate complex-script input.
     */
    /**
     * All available engines on the machine. To use an engine, it has to be
     * activated.
     *
     * @return list of available IME engines.
     * @throws ImeNotAvailableException if the host does not support IME.
     */
    public List<String> getAvailableEngines() {
        println(ANSI_YELLOW + getMethodName() + "...");
        return getMobileDriver().manage().ime().getAvailableEngines();
    }

    /**
     * Get the name of the active IME engine. The name string is platform-specific.
     *
     * @return name of the active IME engine.
     * @throws ImeNotAvailableException if the host does not support IME.
     */
    public String getActiveEngine() {
        println(ANSI_YELLOW + getMethodName() + "...");
        return getMobileDriver().manage().ime().getActiveEngine();
    }

    /**
     * Indicates whether IME input active at the moment (not if it's available).
     *
     * @return true if IME input is available and currently active, false otherwise.
     * @throws ImeNotAvailableException if the host does not support IME.
     */
    public boolean isActivated() {
        println(ANSI_YELLOW + getMethodName() + "...");
        return getMobileDriver().manage().ime().isActivated();
    }

    /**
     * De-activate IME input (turns off the currently activated engine). Note that
     * getActiveEngine
     * may still return the name of the engine but isActivated will return false.
     *
     * @throws ImeNotAvailableException if the host does not support IME.
     */
    public void deactivate() {
        println(ANSI_YELLOW + getMethodName() + "...");
        getMobileDriver().manage().ime().deactivate();
    }

    /**
     * Make an engines that is available (appears on the list returned by
     * getAvailableEngines)
     * active. After this call, the only loaded engine on the IME daemon will be
     * this one and the
     * input sent using sendKeys will be converted by the engine. Note that this is
     * a
     * platform-independent method of activating IME (the platform-specific way
     * being using keyboard
     * shortcuts).
     *
     *
     * @param engine name of engine to activate.
     * @throws ImeNotAvailableException     if the host does not support IME.
     * @throws ImeActivationFailedException if the engine is not available or if
     *                                      activation failed
     *                                      for other reasons.
     */
    public void activateEngine(String engine) {
        println(ANSI_YELLOW + getMethodName() + "..." + engine);
        getMobileDriver().manage().ime().activateEngine(engine);
    }

    /**
     * Window the interface for managing the current window.
     */
    /**
     * Set the size of the current window. This will change the outer window
     * dimension,
     * not just the view port, synonymous to window.resizeTo() in JS.
     *
     * @param targetSize The target size.
     */
    public void setWindowSize(Dimension targetSize) {
        printf(ANSI_YELLOW + getMethodName() + "..." + targetSize);
        getDriver().manage().window().setSize(targetSize);
        println(ANSI_YELLOW + " done.");
    }

    /**
     * Set the position of the current window. This is relative to the upper left
     * corner of the
     * screen, synonymous to window.moveTo() in JS.
     *
     * @param targetPosition The target position of the window.
     */
    public void setPosition(Point targetPosition) {
        printf(ANSI_YELLOW + getMethodName() + "..." + targetPosition);
        getDriver().manage().window().setPosition(targetPosition);
        println(ANSI_YELLOW + " done.");
    }

    /**
     * Get the size of the current window. This will return the outer window
     * dimension, not just
     * the view port.
     *
     * @return The current window size.
     */
    public Dimension getCurrentWindowSize() {
        printf(ANSI_YELLOW + getMethodName() + "...");
        Dimension dim = getDriver().manage().window().getSize();
        println(ANSI_YELLOW + " done.");
        return dim;
    }

    /**
     * Get the position of the current window, relative to the upper left corner of
     * the screen.
     *
     * @return The current window position.
     */
    public Point getPosition() {
        printf(ANSI_YELLOW + getMethodName() + "...");
        Point point = getDriver().manage().window().getPosition();
        println(ANSI_YELLOW + " done.");
        return point;
    }

    /**
     * Maximizes the current window if it is not already maximized
     */
    public void maximize() {
        printf(ANSI_YELLOW + getMethodName() + "...");
        getDriver().manage().window().maximize();
        println(ANSI_YELLOW + " done.");
    }

    /**
     * Fullscreen the current window if it is not already fullscreen
     */
    public void fullscreen() {
        printf(ANSI_YELLOW + getMethodName() + "...");
        getDriver().manage().window().fullscreen();
        println(ANSI_YELLOW + " done.");
    }

    /**
     * LOGS: Gets the {@link Logs} interface used to fetch different types of logs.
     *
     * <p>
     * To set the logging preferences {@link LoggingPreferences}.
     *
     * @return A Logs interface.
     */
    @Beta
    /**
     * Fetches available log entries for the given log type.
     *
     * Note that log buffers are reset after each call, meaning that available log
     * entries correspond to those entries not yet returned for a given log type.
     * In practice, this means that this call will return the available log entries
     * since the last call, or from the start of the session.
     *
     * For more info on enabling logging, look at {@link LoggingPreferences}.
     *
     * @param logType The log type.
     * @return Available log entries for the specified log type.
     */
    public LogEntries getLogEntries(String logType) {
        printf(ANSI_YELLOW + getMethodName() + "..." + logType);

        return getDriver().manage().logs().get(logType);
    }

    /**
     * Queries for available log types.
     *
     * @return A set of available log types.
     */
    public Set<String> getAvailableLogTypes() {
        printf(ANSI_YELLOW + getMethodName() + "...");
        return getDriver().manage().logs().getAvailableLogTypes();
    }

    /**
     * Obtains a set of handles for all the current windows.
     * 
     * @return
     */
    public Set<String> getWindowHandles() {
        printf(ANSI_YELLOW + getMethodName() + "...");
        try {
            Set<String> priorWindows = new HashSet<String>(getDriver().getWindowHandles());
            printf(ANSI_YELLOW + "done...\n");
            return priorWindows;
        } catch (Exception e) {
            printf(ANSI_RED + "failed!!! %s\n", e.getMessage());
            throw new WebDriverException(e.getMessage());
        }
    }

    public WebDriver getDriver() {
        return driver;
    }

    public void setDriver(WebDriver driver) {
        this.driver = driver;
    }

    public long getTimeOutInSeconds() {
        return timeOutInSeconds;
    }

    public void setTimeOutInSeconds(long timeOutInSeconds) {
        this.timeOutInSeconds = timeOutInSeconds;
    }

    public BrowserStack getBrowserStack() {
        return browserStack;
    }

    public void setBrowserStack(BrowserStack browserStack) {
        this.browserStack = browserStack;
    }

    public Map<String, Object> getVars() {
        return vars;
    }

    public void setVars(Map<String, Object> vars) {
        this.vars = vars;
    }

    /**
     * Get a simplified string to represent a WebElement.
     * 
     * @param element
     * @return
     */
    public String getWebElementIdentification(WebElement element) {
        String elementString = element.toString();
        if (elementString.contains("->")) {
            return elementString.split("->")[1];
        } else
            return elementString;
    }
}
