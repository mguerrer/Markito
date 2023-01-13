package cl.set.markito.framework;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nullable;

import org.openqa.selenium.html5.Location;
import org.openqa.selenium.remote.Response;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import cl.set.markito.MarkitoBaseUtils;
import cl.set.markito.framework.browsers.Browser;
import cl.set.markito.framework.cloud.BrowserStack;
import cl.set.markito.framework.devices.Device;
import cl.set.markito.framework.devices.OS;
import io.appium.java_client.MultiTouchAction;
import io.appium.java_client.appmanagement.ApplicationState;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

/**
 * Generic interface to drive browsers in desktop and mobile devices.
 * Marcos Guerrero: 12-01-2023
 */
public class MarkitoWebDriver extends MarkitoBaseUtils implements MarkitoGenericWebDriver {
    private WebDriver driver = null;
    private JavascriptExecutor js;
    private long timeOutInSeconds = 60;
    private Boolean automaticDriverDownload = false;

    public Map<String, Object> vars = new HashMap<String, Object>();

    public MarkitoWebDriver() {
        println(ANSI_YELLOW + "\nMarkito WebDriver started.");
    }

    public MarkitoWebDriver(WebDriver driverObject) {
        driver = driverObject;
        js = (JavascriptExecutor) driver;
    }

    public void finalize() throws Throwable {
    }

    public Object clone() throws CloneNotSupportedException {
        return driver;
    }

    public void setAutomaticDriverDownload(Boolean automaticDriverDownload) {
        this.automaticDriverDownload = automaticDriverDownload;
    }

    public Boolean getAutomaticDriverDownload() {
        return automaticDriverDownload;
    }

    public WebDriver openBrowserSessionInDevice(Browser browser, Device device) throws Exception {
        WebDriver driver;
        BrowserStack bs = new BrowserStack();

        printf(ANSI_YELLOW + "Creating Markito WEB session on browser " + browser.getName() + " on device "
                + device.getName() + " " + device.getPlatform());
        try {
            if (device.getProviderURL().contains("browserstack")) {
                bs.setDesiredWebTechnicalCapabilities(browser.getName().toString(), device.getName(), device.getPlatform().toString(),
                        device.getPlatform_version());
                bs.setProjectInformation("Markito", "MultiTests", "MultiBrowserTest");
            } else if (!device.getProviderURL().equals("")) {
                throw new Exception(ANSI_RED + "ERROR: Provider " + device.getProviderURL()
                        + " not supported for device " + device.getName());
            }
            if (device.equals(LOCAL_COMPUTER_DEVICE)) {
                driver = setLocalWebDrivers(browser);
            } else {
                driver = setRemoteWebDrivers(device, bs.getCapabilities());
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
                println(ANSI_YELLOW + "Markito is destroyed.");
            } else
                println(ANSI_YELLOW + "WARNING: Markito is null.  Please check your teardown process.");
        } catch (Exception e) {
            println(ANSI_YELLOW + "ERROR: There was a problem closing Markito webdriver.");
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public WebElement findElement(By by) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public WebElement findElementByClassName(String className) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public WebElement findElementByCssSelector(String cssSelector) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public WebElement findElementById(String id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public WebElement findElementByLinkText(String linkText) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public WebElement findElementByName(String name) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public WebElement findElementByPartialLinkText(String partialLinkText) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public WebElement findElementByTagName(String tagName) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public WebElement findElementByXPath(String xPath) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<WebElement> findElements(By by) {
        printf(ANSI_YELLOW + "Finding elements %s...", by);
        try {
            List<WebElement> elements = driver.findElements(by);
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

    @Override
    public List<WebElement> findElementsByClassName(String className) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<WebElement> findElementsByCssSelector(String cssSelector) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<WebElement> findElementsById(String id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<WebElement> findElementsByLinkText(String linkText) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<WebElement> findElementsByName(String name) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<WebElement> findElementsByPartialLinkText(String partialLinkText) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<WebElement> findElementsByTagName(String tagName) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<WebElement> findElementsByXPath(String xPath) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void close() {
        // TODO Auto-generated method stub

    }

    @Override
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

    @Override
    public String getCurrentUrl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getPageSource() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getTitle() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getWindowHandle() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Set<String> getWindowHandles() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Options manage() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Navigation navigate() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void quit() {
        // TODO Auto-generated method stub

    }

    @Override
    public TargetLocator switchTo() {
        // TODO Auto-generated method stub
        return null;
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

    private WebDriver setRemoteWebDrivers(Device device, MutableCapabilities caps)
            throws MalformedURLException, Exception {
        WebDriver driver;

        switch (device.getPlatform()) {
            case ANDROID:
            driver = new RemoteWebDriver(new URL(device.getProviderURL()), caps);

//                driver = new AndroidDriver<MobileElement>(new URL(device.getProviderURL()), caps);
                break;
            case IOS:
                driver = new RemoteWebDriver(new URL(device.getProviderURL()), caps);

    //            driver = new IOSDriver<MobileElement>(new URL(device.getProviderURL()), caps);
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
        } catch(Exception e) {
            throw new Exception(ANSI_RED + e.getMessage());
        }
        return driver;
    }

    @Override
    public void ClearConsole() {
        // TODO Auto-generated method stub
        super.ClearConsole();
    }

    @Override
    public int RandomNumber(int min, int max) {
        // TODO Auto-generated method stub
        return super.RandomNumber(min, max);
    }

    @Override
    public String RandomString(int size) {
        // TODO Auto-generated method stub
        return super.RandomString(size);
    }

    @Override
    public void SetDebugModeOFF() {
        // TODO Auto-generated method stub
        super.SetDebugModeOFF();
    }

    @Override
    public void SetDebugModeON() {
        // TODO Auto-generated method stub
        super.SetDebugModeON();
    }

    @Override
    public boolean checkIfFileIsDownloaded(String fileName) {
        // TODO Auto-generated method stub
        return super.checkIfFileIsDownloaded(fileName);
    }

    @Override
    public void deleteDownloadedFileIfExists(String fileName) {
        // TODO Auto-generated method stub
        super.deleteDownloadedFileIfExists(fileName);
    }

    @Override
    public boolean deleteFile(String filename) {
        // TODO Auto-generated method stub
        return super.deleteFile(filename);
    }

    @Override
    public int downloadFile(String URL, String targetFilePathname) {
        // TODO Auto-generated method stub
        return super.downloadFile(URL, targetFilePathname);
    }

    @Override
    public File[] findFilesByNameRegex(String nameRegex, String folder) {
        // TODO Auto-generated method stub
        return super.findFilesByNameRegex(nameRegex, folder);
    }

    @Override
    public String getComputerName() {
        // TODO Auto-generated method stub
        return super.getComputerName();
    }

    @Override
    public PrintStream getDebugManagerOutputStream() {
        // TODO Auto-generated method stub
        return super.getDebugManagerOutputStream();
    }

    @Override
    public boolean getDebugMode() {
        // TODO Auto-generated method stub
        return super.getDebugMode();
    }

    @Override
    public boolean isProcessRunning(String processName) {
        // TODO Auto-generated method stub
        return super.isProcessRunning(processName);
    }

    @Override
    public int killProcess(String processName) {
        // TODO Auto-generated method stub
        return super.killProcess(processName);
    }

    @Override
    public void printProcessResults(Process process) throws IOException {
        // TODO Auto-generated method stub
        super.printProcessResults(process);
    }

    @Override
    public void printf(String format, Object... args) {
        // TODO Auto-generated method stub
        super.printf(format, args);
    }

    @Override
    public void println(String x) {
        // TODO Auto-generated method stub
        super.println(x);
    }

    @Override
    public String readFileToString(String filePath) throws IOException {
        // TODO Auto-generated method stub
        return super.readFileToString(filePath);
    }

    @Override
    public void setDebugManagerOutputStream(PrintStream output) {
        // TODO Auto-generated method stub
        super.setDebugManagerOutputStream(output);
    }

    @Override
    public boolean waitForFileDownloaded(String fileName, long timeoutInSeconds) {
        // TODO Auto-generated method stub
        return super.waitForFileDownloaded(fileName, timeoutInSeconds);
    }

    @Override
    public boolean equals(Object obj) {
        // TODO Auto-generated method stub
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        // TODO Auto-generated method stub
        return super.hashCode();
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return super.toString();
    }

    @Override
    public void activateApp(String bundleId) {
        // TODO Auto-generated method stub
        MarkitoGenericWebDriver.super.activateApp(bundleId);
    }

    @Override
    public void clear() {
        // TODO Auto-generated method stub
        MarkitoGenericWebDriver.super.clear();
    }

    @Override
    public void click() {
        // TODO Auto-generated method stub
        MarkitoGenericWebDriver.super.click();
    }

    @Override
    public void closeApp() {
        // TODO Auto-generated method stub
        MarkitoGenericWebDriver.super.closeApp();
    }

    @Override
    public WebDriver context(String name) {
        // TODO Auto-generated method stub
        return MarkitoGenericWebDriver.super.context(name);
    }

    @Override
    public Response execute(String driverCommand) {
        // TODO Auto-generated method stub
        return MarkitoGenericWebDriver.super.execute(driverCommand);
    }

    @Override
    public Response execute(String driverCommand, Map<String, ?> parameters) {
        // TODO Auto-generated method stub
        return MarkitoGenericWebDriver.super.execute(driverCommand, parameters);
    }

    @Override
    public WebElement findElement(String by, String using) {
        // TODO Auto-generated method stub
        return MarkitoGenericWebDriver.super.findElement(by, using);
    }

    @Override
    public WebElement findElementByAccessibilityId(String using) {
        // TODO Auto-generated method stub
        return MarkitoGenericWebDriver.super.findElementByAccessibilityId(using);
    }

    @Override
    public List<WebElement> findElements(String by, String using) {
        // TODO Auto-generated method stub
        return MarkitoGenericWebDriver.super.findElements(by, using);
    }

    @Override
    public List<WebElement> findElementsByAccessibilityId(String using) {
        // TODO Auto-generated method stub
        return MarkitoGenericWebDriver.super.findElementsByAccessibilityId(using);
    }

    @Override
    public Map<String, String> getAppStringMap() {
        // TODO Auto-generated method stub
        return MarkitoGenericWebDriver.super.getAppStringMap();
    }

    @Override
    public Map<String, String> getAppStringMap(String language) {
        // TODO Auto-generated method stub
        return MarkitoGenericWebDriver.super.getAppStringMap(language);
    }

    @Override
    public Map<String, String> getAppStringMap(String language, String stringFile) {
        // TODO Auto-generated method stub
        return MarkitoGenericWebDriver.super.getAppStringMap(language, stringFile);
    }

    @Override
    public String getAttribute(String name) {
        // TODO Auto-generated method stub
        return MarkitoGenericWebDriver.super.getAttribute(name);
    }

    @Override
    @Nullable
    public String getAutomationName() {
        // TODO Auto-generated method stub
        return MarkitoGenericWebDriver.super.getAutomationName();
    }

    @Override
    public String getContext() {
        // TODO Auto-generated method stub
        return MarkitoGenericWebDriver.super.getContext();
    }

    @Override
    public Set<String> getContextHandles() {
        // TODO Auto-generated method stub
        return MarkitoGenericWebDriver.super.getContextHandles();
    }

    @Override
    public String getCssValue(String propertyName) {
        // TODO Auto-generated method stub
        return MarkitoGenericWebDriver.super.getCssValue(propertyName);
    }

    @Override
    public String getDeviceTime() {
        // TODO Auto-generated method stub
        return MarkitoGenericWebDriver.super.getDeviceTime();
    }

    @Override
    public Point getLocation() {
        // TODO Auto-generated method stub
        return MarkitoGenericWebDriver.super.getLocation();
    }

    @Override
    public ScreenOrientation getOrientation() {
        // TODO Auto-generated method stub
        return MarkitoGenericWebDriver.super.getOrientation();
    }

    @Override
    @Nullable
    public String getPlatformName() {
        // TODO Auto-generated method stub
        return MarkitoGenericWebDriver.super.getPlatformName();
    }

    @Override
    public Rectangle getRect() {
        // TODO Auto-generated method stub
        return MarkitoGenericWebDriver.super.getRect();
    }

    @Override
    public <X> X getScreenshotAs(OutputType<X> target) throws WebDriverException {
        // TODO Auto-generated method stub
        return MarkitoGenericWebDriver.super.getScreenshotAs(target);
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

    @Override
    @Nullable
    public Object getSessionDetail(String detail) {
        // TODO Auto-generated method stub
        return MarkitoGenericWebDriver.super.getSessionDetail(detail);
    }

    @Override
    public Map<String, Object> getSessionDetails() {
        // TODO Auto-generated method stub
        return MarkitoGenericWebDriver.super.getSessionDetails();
    }

    @Override
    public Dimension getSize() {
        // TODO Auto-generated method stub
        return MarkitoGenericWebDriver.super.getSize();
    }

    @Override
    public String getTagName() {
        // TODO Auto-generated method stub
        return MarkitoGenericWebDriver.super.getTagName();
    }

    @Override
    public String getText() {
        // TODO Auto-generated method stub
        return MarkitoGenericWebDriver.super.getText();
    }

    @Override
    public void hideKeyboard() {
        // TODO Auto-generated method stub
        MarkitoGenericWebDriver.super.hideKeyboard();
    }

    @Override
    public void installApp(String appPath) {
        // TODO Auto-generated method stub
        MarkitoGenericWebDriver.super.installApp(appPath);
    }

    @Override
    public boolean isAppInstalled(String bundleId) {
        // TODO Auto-generated method stub
        return MarkitoGenericWebDriver.super.isAppInstalled(bundleId);
    }

    @Override
    public boolean isBrowser() {
        // TODO Auto-generated method stub
        return MarkitoGenericWebDriver.super.isBrowser();
    }

    @Override
    public boolean isDisplayed() {
        // TODO Auto-generated method stub
        return MarkitoGenericWebDriver.super.isDisplayed();
    }

    @Override
    public boolean isEnabled() {
        // TODO Auto-generated method stub
        return MarkitoGenericWebDriver.super.isEnabled();
    }

    @Override
    public boolean isSelected() {
        // TODO Auto-generated method stub
        return MarkitoGenericWebDriver.super.isSelected();
    }

    @Override
    public void launchApp() {
        // TODO Auto-generated method stub
        MarkitoGenericWebDriver.super.launchApp();
    }

    @Override
    public Location location() {
        // TODO Auto-generated method stub
        return MarkitoGenericWebDriver.super.location();
    }

    @Override
    public void performMultiTouchAction(MultiTouchAction multiAction) {
        // TODO Auto-generated method stub
        MarkitoGenericWebDriver.super.performMultiTouchAction(multiAction);
    }

    @Override
    public byte[] pullFile(String remotePath) {
        // TODO Auto-generated method stub
        return MarkitoGenericWebDriver.super.pullFile(remotePath);
    }

    @Override
    public byte[] pullFolder(String remotePath) {
        // TODO Auto-generated method stub
        return MarkitoGenericWebDriver.super.pullFolder(remotePath);
    }

    @Override
    public ApplicationState queryAppState(String bundleId) {
        // TODO Auto-generated method stub
        return MarkitoGenericWebDriver.super.queryAppState(bundleId);
    }

    @Override
    public boolean removeApp(String bundleId) {
        // TODO Auto-generated method stub
        return MarkitoGenericWebDriver.super.removeApp(bundleId);
    }

    @Override
    public void resetApp() {
        // TODO Auto-generated method stub
        MarkitoGenericWebDriver.super.resetApp();
    }

    @Override
    public void rotate(ScreenOrientation orientation) {
        // TODO Auto-generated method stub
        MarkitoGenericWebDriver.super.rotate(orientation);
    }

    @Override
    public void rotate(DeviceRotation rotation) {
        // TODO Auto-generated method stub
        MarkitoGenericWebDriver.super.rotate(rotation);
    }

    @Override
    public DeviceRotation rotation() {
        // TODO Auto-generated method stub
        return MarkitoGenericWebDriver.super.rotation();
    }

    @Override
    public void runAppInBackground(Duration duration) {
        // TODO Auto-generated method stub
        MarkitoGenericWebDriver.super.runAppInBackground(duration);
    }

    @Override
    public void sendKeys(CharSequence... keysToSend) {
        MarkitoGenericWebDriver.super.sendKeys(keysToSend);
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
                    .ignoring(WebDriverException.class).until(ExpectedConditions.visibilityOfElementLocated(locator));
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

    @Override
    public void setLocation(Location location) {
        // TODO Auto-generated method stub
        MarkitoGenericWebDriver.super.setLocation(location);
    }

    @Override
    public void submit() {
        // TODO Auto-generated method stub
        MarkitoGenericWebDriver.super.submit();
    }

    @Override
    public boolean terminateApp(String bundleId) {
        // TODO Auto-generated method stub
        return MarkitoGenericWebDriver.super.terminateApp(bundleId);
    }

    /*
     * @Override
     * public TouchAction performTouchAction(TouchAction touchAction) {
     * // TODO Auto-generated method stub
     * return MarkitoGenericWebDriver.super.performTouchAction(touchAction);
     * }
     * 
     * @Override
     * public void activateApp(String bundleId, @Nullable
     * BaseActivateApplicationOptions options) {
     * // TODO Auto-generated method stub
     * MarkitoGenericWebDriver.super.activateApp(bundleId, options);
     * }
     * 
     * @Override
     * public void installApp(String appPath, @Nullable
     * BaseInstallApplicationOptions options) {
     * // TODO Auto-generated method stub
     * MarkitoGenericWebDriver.super.installApp(appPath, options);
     * }
     * 
     * @Override
     * public boolean removeApp(String bundleId, @Nullable
     * BaseRemoveApplicationOptions options) {
     * // TODO Auto-generated method stub
     * return MarkitoGenericWebDriver.super.removeApp(bundleId, options);
     * }
     * 
     * @Override
     * public boolean terminateApp(String bundleId, @Nullable
     * BaseTerminateApplicationOptions options) {
     * // TODO Auto-generated method stub
     * return MarkitoGenericWebDriver.super.terminateApp(bundleId, options);
     * }
     */
}
