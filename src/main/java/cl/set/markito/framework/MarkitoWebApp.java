package cl.set.markito.framework;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nullable;

import org.openqa.selenium.html5.Location;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

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
 * Generic interface to drive browsers in desktop and mobile devices.
 * Marcos Guerrero: 12-01-2023
 */
public class MarkitoWebApp extends MarkitoBaseUtils implements WebDriver, WebElement {
    private WebDriver driver = null;
    private JavascriptExecutor js;
    private long timeOutInSeconds = 60;
    private Boolean automaticDriverDownload = false;
    private BrowserStack browserStack = new BrowserStack();
    public Map<String, Object> vars = new HashMap<String, Object>();

    public MarkitoWebApp() {
        println(ANSI_YELLOW + "\nMarkito WebDriver started.");
    }

    public MarkitoWebApp(WebDriver driverObject) {
        driver = driverObject;
        js = (JavascriptExecutor) driver;
    }

    public void finalize() throws Throwable {
    }

    public Object clone() throws CloneNotSupportedException {
        return driver;
    }

    public boolean isIOS() {
        return driver.toString().contains("io.appium.java_client.ios.IOSDriver");
    }

    public boolean isAndroid() {
        return driver.toString().contains("automationName=UIAutomator2");
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

        printf(ANSI_WHITE + "Creating Markito WEB session on browser " + ANSI_YELLOW + browser.getName() + " on device "
                + ANSI_WHITE + device.getName() + "-" + device.getPlatform() + "...\n");
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
                driver = setRemoteWebDrivers(device, browserStack.getCapabilities());
            }
        } catch (Exception e) {

            println("\n" + ANSI_RED + "ERROR on creating session." + e.getMessage());
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

        try {
            if (driver != null) {
                // driver.close();
                driver.quit();
                driver = null;
                println(ANSI_WHITE + "Markito is destroyed.");
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
    @Override
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

    public WebElement findElementByClassName(String className) {
        return getMobileDriver().findElementByClassName(className);
    }

    public WebElement findElementByCssSelector(String cssSelector) {
        return getMobileDriver().findElementByCssSelector(cssSelector);
    }

    public WebElement findElementById(String id) {
        return getMobileDriver().findElementById(id);
    }

    public WebElement findElementByLinkText(String linkText) {
        return getMobileDriver().findElementByLinkText(linkText);
    }

    public WebElement findElementByName(String name) {
        return getMobileDriver().findElementByName(name);
    }

    public WebElement findElementByPartialLinkText(String partialLinkText) {
        return getMobileDriver().findElementByPartialLinkText(partialLinkText);
    }

    public WebElement findElementByTagName(String tagName) {
        return getMobileDriver().findElementByTagName(tagName);
    }

    public WebElement findElementByXPath(String xPath) {
        return getMobileDriver().findElementByXPath(xPath);
    }

    @Override
    public List<WebElement> findElements(By by) {
        printf(ANSI_YELLOW + "Finding elements %s...", by);
        try {
            List<WebElement> elements;
            if (isIOS()) { // iOS
                elements = getIosDriver().findElements(by);
            } else if (isAndroid()) { // Android
                elements = getAndroidDriver().findElements(by);
            } else
                elements = driver.findElements(by);
            /*
             * TODO elements.forEach(element -> {
             * highlightElement(element);
             * }); // Highlights on debug mode.
             */
            printf(ANSI_YELLOW + "found %d elements.\n", elements.size());
            return elements;
        } catch (Exception e) {
            printf(ANSI_RED + "failed!!! %s\n", e.getMessage());
            throw new WebDriverException(e.getMessage());
        }
    }

    public List<WebElement> findElementsByClassName(String className) {
        return getMobileDriver().findElementsByClassName(className);
    }

    public List<WebElement> findElementsByCssSelector(String cssSelector) {
        return getMobileDriver().findElementsByCssSelector(cssSelector);
    }

    public List<WebElement> findElementsById(String id) {
        return getMobileDriver().findElementsById(id);
    }

    public List<WebElement> findElementsByLinkText(String linkText) {
        return getMobileDriver().findElementsByLinkText(linkText);
    }

    public List<WebElement> findElementsByName(String name) {
        return getMobileDriver().findElementsByName(name);
    }

    public List<WebElement> findElementsByPartialLinkText(String partialLinkText) {
        return getMobileDriver().findElementsByPartialLinkText(partialLinkText);
    }

    public List<WebElement> findElementsByTagName(String tagName) {
        return getMobileDriver().findElementsByTagName(tagName);
    }

    public List<WebElement> findElementsByXPath(String xPath) {
        return getMobileDriver().findElementsByXPath(xPath);
    }

    @Override
    public void close() {
        closeCurrentWindow();
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
    @Override
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

    @Override
    public String getCurrentUrl() {
        return getDriver().getCurrentUrl();
    }

    @Override
    public String getPageSource() {
        return getDriver().getPageSource();
    }

    @Override
    public String getTitle() {
        return getDriver().getTitle();
    }

    /**
     * Obtains the string handle be used to select the current window.
     */
    @Override
    public String getWindowHandle() {
        printf(ANSI_YELLOW + "GetWindowHandle...");
        try {
            String currentWindowHandle = driver.getWindowHandle();
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

    @Override
    public Options manage() {
        return getDriver().manage();
    }

    @Override
    public Navigation navigate() {
        return getDriver().navigate();
    }

    @Override
    public void quit() {
        getDriver().quit();
    }

    @Override
    public TargetLocator switchTo() {
        return getDriver().switchTo();
    }

    public WebDriver getDriver() {
        return driver;
    }

    public JavascriptExecutor getJs() {
        return js;
    }

    public long getTimeOutInSeconds() {
        return timeOutInSeconds;
    }

    public void setDriver(WebDriver driver) {
        this.driver = driver;
    }

    public void setJs(JavascriptExecutor js) {
        this.js = js;
    }

    public void setTimeOutInSeconds(long timeOutInSeconds) {
        this.timeOutInSeconds = timeOutInSeconds;
    }

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

    @Override
    public void ClearConsole() {
        super.ClearConsole();
    }

    @Override
    public int RandomNumber(int min, int max) {
        return super.RandomNumber(min, max);
    }

    @Override
    public String RandomString(int size) {
        return super.RandomString(size);
    }

    @Override
    public void SetDebugModeOFF() {
        super.SetDebugModeOFF();
    }

    @Override
    public void SetDebugModeON() {
        super.SetDebugModeON();
    }

    @Override
    public boolean checkIfFileIsDownloaded(String fileName) {
        return super.checkIfFileIsDownloaded(fileName);
    }

    @Override
    public void deleteDownloadedFileIfExists(String fileName) {
        super.deleteDownloadedFileIfExists(fileName);
    }

    @Override
    public boolean deleteFile(String filename) {
        return super.deleteFile(filename);
    }

    @Override
    public int downloadFile(String URL, String targetFilePathname) {
        return super.downloadFile(URL, targetFilePathname);
    }

    @Override
    public File[] findFilesByNameRegex(String nameRegex, String folder) {
        return super.findFilesByNameRegex(nameRegex, folder);
    }

    @Override
    public String getComputerName() {
        return super.getComputerName();
    }

    @Override
    public PrintStream getDebugManagerOutputStream() {
        return super.getDebugManagerOutputStream();
    }

    @Override
    public boolean getDebugMode() {
        return super.getDebugMode();
    }

    @Override
    public boolean isProcessRunning(String processName) {
        return super.isProcessRunning(processName);
    }

    @Override
    public int killProcess(String processName) {
        return super.killProcess(processName);
    }

    @Override
    public void printProcessResults(Process process) throws IOException {
        super.printProcessResults(process);
    }

    @Override
    public void printf(String format, Object... args) {
        super.printf(format, args);
    }

    @Override
    public void println(String x) {
        super.println(x);
    }

    @Override
    public String readFileToString(String filePath) throws IOException {
        return super.readFileToString(filePath);
    }

    @Override
    public void setDebugManagerOutputStream(PrintStream output) {
        super.setDebugManagerOutputStream(output);
    }

    @Override
    public boolean waitForFileDownloaded(String fileName, long timeoutInSeconds) {
        return super.waitForFileDownloaded(fileName, timeoutInSeconds);
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public void activateApp(String bundleId) {
        getIosDriver().activateApp(bundleId);
    }

    public void clear(WebElement element) {
        element.clear();
    }

    /**
     * @param element
     */
    public void click(WebElement element) {
    }

    /**
     * Waits for an element to be clickable located using By and click in it.
     * 
     * @param locator
     */
    public void click(By locator) {
        printf(ANSI_YELLOW + "click %s...", locator);
        long currentTimeout = getTimeOutInSeconds();
        try {
            // highlightElement(locator);
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
            new WebDriverWait(driver, timeOutInSeconds)
                    .ignoring(StaleElementReferenceException.class)
                    .ignoring(Exception.class)
                    .until(ExpectedConditions.elementToBeClickable(locator));
            driver.findElement(locator).click();
            setTimeOutInSeconds(currentTimeout);
            printf(ANSI_YELLOW + "done.\n", locator);
        } catch (Exception e) {
            setTimeOutInSeconds(currentTimeout);
            printf(ANSI_RED + "failed!!! %s\n", e.getMessage());
            throw new WebDriverException(e.getMessage());
        }
    }

    public void execute(By locator, WebDriverWait condition) {
        // Return the current time
        printf(ANSI_YELLOW + "click %s...", locator);
        long currentTimeout = getTimeOutInSeconds();
        try {
            // highlightElement(locator);
            condition.wait();
            driver.findElement(locator).click();
            setTimeOutInSeconds(currentTimeout);
            printf(ANSI_YELLOW + "done.\n", locator);
        } catch (Exception e) {
            setTimeOutInSeconds(currentTimeout);
            printf(ANSI_RED + "failed!!! %s\n", e.getMessage());
            throw new WebDriverException(e.getMessage());
        }
    }

    public void closeApp() {
        getMobileDriver().closeApp();
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
        println(ANSI_YELLOW + "SetContextHandle " + name); // prints out something like NATIVE_APP \n WEBVIEW_1
        return getMobileDriver().context(name);
    }

    /**
     * Get contexts for Hybrid Apps.
     * 
     * @return ContextNames: Obtained contexts from Android driver.
     */
    public Set<String> getContextHandles() {
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
        String contextName = getMobileDriver().getContext();
        println(ANSI_YELLOW + contextName); // prints out something like NATIVE_APP \n WEBVIEW_1
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

    public WebElement findElement(String by, String using) {
        return getMobileDriver().findElement(by, using);
    }

    public WebElement findElementByAccessibilityId(String using) {
        return getMobileDriver().findElementByAccessibilityId(using);
    }

    public List<WebElement> findElementsByAccessibilityId(String using) {
        return getMobileDriver().findElementsByAccessibilityId(using);
    }

    public Map<String, String> getAppStringMap() {
        return getMobileDriver().getAppStringMap();
    }

    public Map<String, String> getAppStringMap(String language) {
        return getMobileDriver().getAppStringMap(language);
    }

    public Map<String, String> getAppStringMap(String language, String stringFile) {
        return getMobileDriver().getAppStringMap(language, stringFile);
    }

    /**
     * Get Value of an element located by.
     * 
     * @param by
     */
    public String getValue(By by) {
        printf(ANSI_YELLOW + "GetValue ");
        long currentTimeout = getTimeOutInSeconds();
        try {
            // highlightElement(by);
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
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
        return getMobileDriver().getAutomationName();
    }

    public String getDeviceTime() {
        return getMobileDriver().getDeviceTime();
    }

    @Nullable
    public String getPlatformName() {
        return getMobileDriver().getPlatformName();
    }

    @Override
    public Rectangle getRect() {
        return ((RemoteWebElement) getDriver()).getRect();
    }

    @Override
    public <X> X getScreenshotAs(OutputType<X> target) {
        return ((RemoteWebDriver) getDriver()).getScreenshotAs(target);
    }

    /**
     * Gets an screen snapshot and saves to a file.
     * 
     * @param fileWithPath: Pathname of the file to be generated.
     */
    public void getScreenshotAs(String fileWithPath) throws Exception {
        // Convert web driver object to TakeScreenshot
        TakesScreenshot scrShot = ((TakesScreenshot) driver);
        // Call getScreenshotAs method to create image file
        File SrcFile = scrShot.getScreenshotAs(OutputType.FILE);
        // Move image file to new destination
        File DestFile = new File(fileWithPath);
        // Copy file at destination
        FileUtils.copyFile(SrcFile, DestFile);
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
            getScreenshotAs("TestResults\\" + Name + "-" + date + ".png");
        } catch (Exception e) {
            printf("ERROR al tomar snapshot. Stack:%s\n", e.getMessage());
        }
    }

    @Nullable
    public Object getSessionDetail(String detail) {
        return getMobileDriver().getSessionDetail(detail);
    }

    public Map<String, Object> getSessionDetails() {
        return getMobileDriver().getSessionDetails();
    }

    @Override
    public Dimension getSize() {
        return ((RemoteWebElement) getDriver()).getSize();
    }

    @Override
    public String getTagName() {
        return ((RemoteWebElement) getDriver()).getTagName();
    }

    @Override
    public String getText() {
        return ((RemoteWebElement) getDriver()).getText();
    }

    public void hideKeyboard() {
        getMobileDriver().hideKeyboard();
    }

    public void installApp(String appPath) {
        getMobileDriver().installApp(appPath);
    }

    public boolean isAppInstalled(String bundleId) {
        return getMobileDriver().isAppInstalled(bundleId);
    }

    public boolean isBrowser() {
        return getMobileDriver().isBrowser();
    }

    @Override
    public boolean isDisplayed() {
        return ((RemoteWebElement) getDriver()).isDisplayed();
    }

    @Override
    public boolean isEnabled() {
        return ((RemoteWebElement) getDriver()).isEnabled();
    }

    @Override
    public boolean isSelected() {
        return ((RemoteWebElement) getDriver()).isSelected();
    }

    public void launchApp() {
        getMobileDriver().launchApp();
    }

    public Location location() {
        return getMobileDriver().location();
    }

    public void performMultiTouchAction(MultiTouchAction multiAction) {
        getMobileDriver().performMultiTouchAction(multiAction);
    }

    public byte[] pullFile(String remotePath) {
        return getMobileDriver().pullFile(remotePath);
    }

    public byte[] pullFolder(String remotePath) {
        return getMobileDriver().pullFolder(remotePath);
    }

    public ApplicationState queryAppState(String bundleId) {
        return getMobileDriver().queryAppState(bundleId);
    }

    public boolean removeApp(String bundleId) {
        return getMobileDriver().removeApp(bundleId);
    }

    public void resetApp() {
        getMobileDriver().resetApp();
    }

    public void rotate(ScreenOrientation orientation) {
        getMobileDriver().rotate(orientation);
    }

    public void rotate(DeviceRotation rotation) {
        getMobileDriver().rotate(rotation);
    }

    public DeviceRotation rotation() {
        return getMobileDriver().rotation();
    }

    public void runAppInBackground(Duration duration) {
        getMobileDriver().runAppInBackground(duration);
    }

    public void sendKeys(CharSequence... keysToSend) {
        ((RemoteWebElement) getDriver()).sendKeys(keysToSend);
    }

    /**
     * Simulates typing Keys over an editable element located By.
     * 
     * @param locator
     * @param keys
     */
    public void sendKeys(By locator, String keys) {
        printf(ANSI_YELLOW + "SendKeys %s to object %s...", keys, locator);
        long currentTimeout = getTimeOutInSeconds();
        try {
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
            new WebDriverWait(driver, timeOutInSeconds).ignoring(StaleElementReferenceException.class)
                    .ignoring(Exception.class).until(ExpectedConditions.elementToBeClickable(locator));
            WebElement textBox = driver.findElement(locator);
            // highlightElement( textBox);
            if (textBox == null) {
                throw new Exception(ANSI_RED + " Can not find element " + locator);
            }
            textBox.sendKeys(keys);
            setTimeOutInSeconds(currentTimeout);
            println("done!");
        } catch (Exception e) {
            setTimeOutInSeconds(currentTimeout);
            printf(ANSI_RED + "failed! %s\n", e.getMessage());
            throw new WebDriverException(e.getMessage());
        }
    }

    /**
     * Simulates typing Keys over an editable element located By.
     * 
     * @param locator
     * @param keys
     */
    public void sendKeys(WebElement element, String keys) {
        printf(ANSI_YELLOW + "SendKeys %s to element...", keys);
        try {
            // highlightElement( textBox);
            element.sendKeys(keys);
            println("done!");
        } catch (Exception e) {
            printf(ANSI_RED + "failed! %s\n", e.getMessage());
            throw new WebDriverException(e.getMessage());
        }
    }

    public void setLocation(Location location) {
        getMobileDriver().setLocation(location);
    }

    public boolean terminateApp(String bundleId) {
        return getMobileDriver().terminateApp(bundleId);
    }

    @Override
    public void click() {
        // TODO Auto-generated method stub

    }

    @Override
    public void clear() {
        // TODO Auto-generated method stub

    }

    @Override
    public String getAttribute(String name) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getCssValue(String propertyName) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Point getLocation() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void submit() {
        // TODO Auto-generated method stub

    }

    /*
     * @Override
     * public TouchAction performTouchAction(TouchAction touchAction) {
     * // TODO Auto-generated method stub
     * return getMobileDriver().performTouchAction(touchAction);
     * }
     * 
     * @Override
     * public void activateApp(String bundleId, @Nullable
     * BaseActivateApplicationOptions options) {
     * // TODO Auto-generated method stub
     * getMobileDriver().activateApp(bundleId, options);
     * }
     * 
     * @Override
     * public void installApp(String appPath, @Nullable
     * BaseInstallApplicationOptions options) {
     * // TODO Auto-generated method stub
     * getMobileDriver().installApp(appPath, options);
     * }
     * 
     * @Override
     * public boolean removeApp(String bundleId, @Nullable
     * BaseRemoveApplicationOptions options) {
     * // TODO Auto-generated method stub
     * return getMobileDriver().removeApp(bundleId, options);
     * }
     * 
     * @Override
     * public boolean terminateApp(String bundleId, @Nullable
     * BaseTerminateApplicationOptions options) {
     * // TODO Auto-generated method stub
     * return getMobileDriver().terminateApp(bundleId, options);
     * }
     */

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
    public void closeCurrentWindow() {
        println(ANSI_YELLOW + "CloseCurrentWindow.");
        driver.close();
    }

    /**
     * Get Text of a web element.
     * 
     * @param element
     * @throws Exception
     */
    public String getText(WebElement element) throws Exception {
        printf(ANSI_YELLOW + "GetText from WebElement...");
        long currentTimeout = getTimeOutInSeconds();
        try {
            // highlightElement(by);
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
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
        printf(ANSI_YELLOW + "GetText from %s...", by);
        long currentTimeout = getTimeOutInSeconds();
        try {
            // highlightElement(by);
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
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
        printf(ANSI_YELLOW + "GetAttribute [" + attributeName + "]...");
        long currentTimeout = getTimeOutInSeconds();
        try {
            // highlightElement(by);
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
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

    /** Navigate interface */

    /**
     * Move back a single "item" in the browser's history.
     */
    void back() {
        getDriver().navigate().back();
    }

    /**
     * Move a single "item" forward in the browser's history. Does nothing if we are
     * on the latest
     * page viewed.
     */
    void forward() {
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
    void to(String url) {
        getDriver().navigate().to(url);
    }

    /**
     * Overloaded version of {@link #to(String)} that makes it easy to pass in a
     * URL.
     *
     * @param url URL
     */
    void to(URL url) {
        getDriver().navigate().to(url);
    }

    /**
     * Refresh the current page
     */
    void refresh() {
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
    void addCookie(Cookie cookie) {
        getDriver().manage().addCookie(cookie);
    }

    /**
     * Delete the named cookie from the current domain. This is equivalent to
     * setting the named
     * cookie's expiry date to some time in the past.
     *
     * @param name The name of the cookie to delete
     */
    void deleteCookieNamed(String name) {
        getDriver().manage().deleteCookieNamed(name);
    }

    /**
     * Delete a cookie from the browser's "cookie jar". The domain of the cookie
     * will be ignored.
     *
     * @param cookie nom nom nom
     */
    void deleteCookie(Cookie cookie) {
        getDriver().manage().deleteCookie(cookie);
    }

    /**
     * Delete all the cookies for the current domain.
     */
    void deleteAllCookies() {
        getDriver().manage().deleteAllCookies();
    }

    /**
     * Get all the cookies for the current domain. This is the equivalent of calling
     * "document.cookie" and parsing the result
     *
     * @return A Set of cookies for the current domain.
     */
    Set<Cookie> getCookies() {
        return getDriver().manage().getCookies();
    }

    /**
     * Get a cookie with a given name.
     *
     * @param name the name of the cookie
     * @return the cookie, or null if no cookie with the given name is present
     */
    Cookie getCookieNamed(String name) {
        return getDriver().manage().getCookieNamed(name);
    }

    /** Timeouts interface */
    /**
     * Specifies the amount of time the driver should wait when searching for an
     * element if it is
     * not immediately present.
     * <p>
     * When searching for a single element, the driver should poll the page until
     * the element has
     * been found, or this timeout expires before throwing a
     * {@link NoSuchElementException}. When
     * searching for multiple elements, the driver should poll the page until at
     * least one element
     * has been found or this timeout has expired.
     * <p>
     * Increasing the implicit wait timeout should be used judiciously as it will
     * have an adverse
     * effect on test run time, especially when used with slower location strategies
     * like XPath.
     *
     * @param time The amount of time to wait.
     * @param unit The unit of measure for {@code time}.
     * @return A self reference.
     */
    Timeouts implicitlyWait(long time, TimeUnit unit) {
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
    Timeouts setScriptTimeout(long time, TimeUnit unit) {
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
    Timeouts pageLoadTimeout(long time, TimeUnit unit) {
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
    WebDriver switchToFrame(int index) {
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
    WebDriver switchToFrame(String nameOrId) {
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
    WebDriver switchToFrame(WebElement frameElement) {
        return getDriver().switchTo().frame(frameElement);
    }

    /**
     * Change focus to the parent context. If the current context is the top level
     * browsing context,
     * the context remains unchanged.
     *
     * @return This driver focused on the parent frame
     */
    WebDriver switchToParentFrame() {
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
    WebDriver switchToWindow(String nameOrHandle) {
        return getDriver().switchTo().window(nameOrHandle);
    }

    /**
     * Selects either the first frame on the page, or the main document when a page
     * contains
     * iframes.
     *
     * @return This driver focused on the top window/first frame.
     */
    WebDriver switchToDefaultContent() {
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
    WebElement switchToActiveElement() {
        return getDriver().switchTo().activeElement();
    }

    /**
     * Switches to the currently active modal dialog for this particular driver
     * instance.
     *
     * @return A handle to the dialog.
     * @throws NoAlertPresentException If the dialog cannot be found
     */
    Alert switchToAlert() {
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
    List<String> getAvailableEngines() {
        return getMobileDriver().manage().ime().getAvailableEngines();
    }

    /**
     * Get the name of the active IME engine. The name string is platform-specific.
     *
     * @return name of the active IME engine.
     * @throws ImeNotAvailableException if the host does not support IME.
     */
    String getActiveEngine() {
        return getMobileDriver().manage().ime().getActiveEngine();
    }

    /**
     * Indicates whether IME input active at the moment (not if it's available).
     *
     * @return true if IME input is available and currently active, false otherwise.
     * @throws ImeNotAvailableException if the host does not support IME.
     */
    boolean isActivated() {
        return getMobileDriver().manage().ime().isActivated();
    }

    /**
     * De-activate IME input (turns off the currently activated engine). Note that
     * getActiveEngine
     * may still return the name of the engine but isActivated will return false.
     *
     * @throws ImeNotAvailableException if the host does not support IME.
     */
    void deactivate() {
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
    void activateEngine(String engine) {
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
    void setSize(Dimension targetSize) {
        getDriver().manage().window().setSize(targetSize);
    }

    /**
     * Set the position of the current window. This is relative to the upper left
     * corner of the
     * screen, synonymous to window.moveTo() in JS.
     *
     * @param targetPosition The target position of the window.
     */
    void setPosition(Point targetPosition) {
        getDriver().manage().window().setPosition(targetPosition);
    }

    /**
     * Get the size of the current window. This will return the outer window
     * dimension, not just
     * the view port.
     *
     * @return The current window size.
     */
    Dimension getCurrentWindowSize() {
        return getDriver().manage().window().getSize();
    }

    /**
     * Get the position of the current window, relative to the upper left corner of
     * the screen.
     *
     * @return The current window position.
     */
    Point getPosition() {
        return getDriver().manage().window().getPosition();
    }

    /**
     * Maximizes the current window if it is not already maximized
     */
    void maximize() {
        getDriver().manage().window().maximize();
    }

    /**
     * Fullscreen the current window if it is not already fullscreen
     */
    void fullscreen() {
        getDriver().manage().window().fullscreen();
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
    LogEntries getLogEntries(String logType) {
        return getDriver().manage().logs().get(logType);
    }

    /**
     * Queries for available log types.
     *
     * @return A set of available log types.
     */
    Set<String> getAvailableLogTypes() {
        return getDriver().manage().logs().getAvailableLogTypes();
    }

    @Override
    public Set<String> getWindowHandles() {
        // TODO Auto-generated method stub
        return null;
    }

}