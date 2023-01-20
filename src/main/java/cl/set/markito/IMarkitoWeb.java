package cl.set.markito;

import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WebDriver.Navigation;
import org.openqa.selenium.WebDriver.Options;
import org.openqa.selenium.WebDriver.TargetLocator;
import org.openqa.selenium.ie.InternetExplorerOptions;

public interface IMarkitoWeb {

    /***
     * Default chrome constructor that can use headless execution mode.
     * 
     * @param headless
     */
    WebDriver openChromeDriver();

    /***
     * Default firefox constructor that can use headless execution mode.
     * 
     * @param headless
     */
    void openFirefoxDriver(boolean headless);

    /**
     * A simple implementation of getting a webdriver server session for Internet
     * Explorer.
     * 
     * @return the driver session.
     */
    WebDriver openInternetExplorerDriver();

    /**
     * A simple implementation of getting a webdriver server session for Internet
     * Explorer using an initial URL.
     * This is handy to improve performance.
     * 
     * @return the driver session.
     */
    WebDriver openInternetExplorerDriver(String initialURL);

    /**
     * A simple implementation of getting a webdriver server session for Internet
     * Explorer
     * using a more general approach of using InternetExplorerOptions.
     * 
     * @return the driver session.
     */
    WebDriver openInternetExplorerDriver(InternetExplorerOptions options);

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
    void setInternetExplorerEnvironmentVariables(String pathToIEDriverServerExe, String debugLevel,
            String pathToIEDriverServerLog);

    /**
     * Close current webdriver session and collects possible garbage.
     * 
     * @throws Exception
     */
    void closeWebSessionInDevice() throws Exception;

    /**
     * Find an element in DOM using By locator. When debug mode is ON highlights the
     * element to help visual debug.
     * 
     * @param by
     * @return WebElement
     */
    WebElement findElement(By by);

    /**
     * Find elements in DOM using By locator. When debug mode is ON highlights the
     * element to help visual debug.
     * 
     * @param by
     * @return WebElement
     */
    List<WebElement> findElements(By by);

    /**
     * Establish a unified timeout parameter for implicit waits, page loads and
     * javascripts.
     * 
     * @param timeOutInSeconds
     */
    void setTimeouts(long timeOutInSeconds);

    /**
     * Get a unified timeout parameter for implicit waits, page loads and
     * javascripts.
     * 
     * @param timeOutInSeconds
     */
    long getTimeouts();

    /**
     * Creates a new window, selects it and returns the handler.
     * 
     * @param windowHandle
     */
    String createWindow();

    /**
     * Select the window referred by the window handle.
     * 
     * @param windowHandle
     */
    void selectWindow(String windowHandle);

    /**
     * Obtains the string handle be used to select the current window.
     */
    String getWindowHandle();

    /**
     * Finds in current webdriver windows handles a window with "title" and returns
     * the handle.
     * 
     * @param title: Expected window title.
     * @return: A window handle or null if not found.
     */
    String getWindowHandleByTitle(String title);

    /**
     * Obtains a set of handles for all the current windows.
     * 
     * @return
     */
    Set<String> getWindowHandles();

    /**
     * Waits for a new window to be opened and returns its handle during
     * timeOutInSeconds.
     * 
     * @return
     */
    String waitForNewWindow();

    /**
     * Waits for a new window to be opened and returns its handle.
     * 
     * @timeoutSeconds: Max time to wait for new window.
     * @return
     */
    String waitForNewWindow(long timeoutSeconds);

    /**
     * Waits until the number of opened windows is equal to NWindows.
     */
    void waitForNWindows(int NWindows);

    /**
     * Waits until the number of opened windows is equal to NWindows.
     */
    void waitForNWindows(int NWindows, long timeoutSeconds);

    /**
     * Close the selected window. Please refer to
     * "https://www.selenium.dev/selenium/docs/api/java/com/thoughtworks/selenium/webdriven/commands/Close.html"
     */
    void CloseCurrentWindow();

    /**
     * Execute a JavaScript script.
     * Please refer to
     * "https://www.selenium.dev/selenium/docs/api/java/org/openqa/selenium/remote/server/handler/ExecuteScript.html"
     * 
     * @param script
     * @param args
     */
    void executeJsScript(String script, java.lang.Object... args);

    /**
     * Execute an asynchronous JavaScript script.
     * 
     * @param script
     * @param args
     */
    void executeAsynchromousJsScript(String script, String args);

    /**
     * Get a url and waits for complete load.
     * 
     * @param url
     */
    void get(String url);

    /**
     * Get Text of an element located by.
     * 
     * @param by
     */
    String getText(By by);

    /**
     * Get Value of an element located by.
     * 
     * @param by
     */
    String getValue(By by);

    /**
     * Get Attribute of an element located by.
     * 
     * @param by
     * @param attributeName
     */
    String getAttribute(By by, String attributeName);

    /**
     * Sets the value of an attribute.
     * 
     * @param by
     * @param attributeName
     * @param value
     * @return Fails on attribute not found or other condition.
     */
    void setAttributeValue(By by, String attributeName, String value);

    /**
     * Waits for a frame by handle is available and switch to it.
     * 
     * @param frameHandle
     * @throws InterruptedException
     */
    void selectFrameByHandle(int frameHandle) throws InterruptedException;

    /**
     * Waits for a frame using By reference is available and switch to it.
     * 
     * @param frameLocator
     * @throws InterruptedException
     */
    void selectFrameByLocator(By frameLocator) throws InterruptedException;

    /**
     * Waits for a frame using frameId (string) is available and switch to it.
     * 
     * @param frameId
     * @throws InterruptedException
     */
    void selectFrameById(String frameID);

    /**
     * Waits for an element to be clickable located using By and click in it.
     * 
     * @param locator
     */
    void click(By locator);

    /**
     * Click in a clickable element located using By and bypassing webdriver using
     * js executor.
     * 
     * @param locator
     */
    void clickJS(By locator);

    /**
     * Click in a element located using webdriver Actions.
     * 
     * @param locator
     */
    void clickUsingActions(By locator);

    /**
     * Double Click in a element located by locator using webdriver Actions.
     * This method doesn't wait for element clickable but visible.
     * 
     * @param locator
     */
    void doubleClick(By locator);

    /**
     * Double Click in a element located by locator using webdriver Actions.
     * 
     * @param webElement
     */
    void doubleClick(WebElement webElement);

    /**
     * MouseOver in a element located using webdriver Actions.
     * 
     * @param locator
     */
    void mouseOver(By locator);

    /**
     * MouseOut in a element located using webdriver Actions.
     * 
     * @param locator
     */
    void mouseOut(By locator);

    /**
     * DragAndDrop sourceObject over targetObject.
     * 
     * @param sourceObject
     * @param targetObject
     */
    void dragAndDrop(WebElement sourceObject, WebElement targetObject);

    /**
     * ClickAt in a clickable element located using By at position (x,y).
     * 
     * @param locator
     */
    void clickAt(By locator, int x, int y);

    /**
     * Waits for an element to be present and fail if not found on timeOutInSeconds.
     * 
     * @param locator By of the element.
     */
    void waitForElementPresent(By locator);

    /**
     * Waits for an element to be present and not fail if not found on timeout,
     * instead it will return true or false.
     * 
     * @param locator
     * @param timeout max time to wait for element to be visible.
     * @return true if element is visible!!! false if element is not visible.
     */
    boolean waitForElementPresent(By locator, long timeout);

    /**
     * Waits for an element to be visible and fail if not found on timeOutInSeconds.
     * 
     * @param locator
     */
    void waitForElementVisible(By locator);

    /**
     * Waits for an element to be visible and not fail if not found on timeout,
     * instead it will return true or false.
     * 
     * @param locator
     * @param timeout max time to wait for element to be visible.
     * @return true or false depending on finding the element in timeout seconds.
     */
    boolean waitForElementVisible(By locator, long timeout);

    /**
     * Waits for an element to be visible and not fail if not found on timeout,
     * instead it will return true or false.
     * 
     * @param webElement
     * @param timeout    max time to wait for element to be visible.
     * @return true or false depending on finding the element in timeout seconds.
     */
    boolean waitForElementVisible(WebElement webElement, long timeout);

    /**
     * Waits for an element to be visible and fail if not found on timeOutInSeconds.
     * 
     * @param webElement
     */
    void waitForElementVisible(WebElement webElement);

    /**
     * Waits for text in element.
     * 
     * @param text:    Text to wait for
     * @param locator: Locator to element.
     * @throws WebDriverException when not found in timeoutInSeconds.
     */
    void waitForTextInElement(String text, By locator);

    /**
     * Waits for text in element.
     * 
     * @param text:    Text to wait for
     * @param locator: Locator to element.
     * @param timeout: Time to wait for.
     * @return true if text is found on element before timeout seconds.
     */
    boolean waitForTextInElement(String text, By locator, long timeout);

    /**
     * Waits for text in page to be visible.
     * 
     * @param text: Text to wait for
     */
    void waitForTextInPage(String text);

    /**
     * Simulates typing Keys over an editable element located By.
     * 
     * @param locator
     * @param keys
     */
    void sendKeys(By locator, String keys);

    /**
     * Waits for alert present and Clicks OK if present during timeout period.
     */
    void clickOKOnAlert();

    /**
     * Waits for alert present and Clicks CANCEL if present during timeout period.
     */
    void clickCancelOnAlert();

    /**
     * Waits for alert present and gets the inner text to a String.
     * 
     * @return String
     */
    String getTextOfAlert();

    /**
     * Writes an string on the alert msg.
     * 
     * @param text
     */
    void typeTextOnAlert(String text);

    /**
     * Waits for alert present during timeOutInSeconds period.
     * 
     * @param timeOutInSeconds
     * @return
     */
    boolean waitForAlertPresent(long timeOutInSeconds);

    /**
     * Highlights WebElement if debug mode is ON.
     * 
     * @param element
     */
    void highlightElement(WebElement element);

    /**
     * Highlights an element By if debug mode is ON.
     * 
     * @param element
     */
    void highlightElement(By element);

    /**
     * Selects an option from by using VisibleText.
     * 
     * @param selectObject
     * @param VisibleText
     */
    void selectOptionByVisibleText(By selectObject, String VisibleText);

    /**
     * Selects an option from by using integer index.
     * 
     * @param by
     * @param index
     */
    void selectOptionByIndex(By by, int index);

    /**
     * Selects an option from by using current value.
     * 
     * @param by
     * @param value
     */
    void selectOptionByValue(By by, String value);

    void setLocation(double x, double y, double z);

    /**
     * Gets an screen snapshot and saves to a file.
     * 
     * @param fileWithPath: Pathname of the file to be generated.
     */
    void takeScreenSnapshot(String fileWithPath) throws Exception;

    /***
     * Takes an screenshot and leaves it in file named TestResults\Name-Date.png
     * 
     * @param Name: The name you want to recognize.
     */
    void getScreenSnapshotWithDate(String Name);

    /**
     * Waits for a a full page load by reading document.readyState.
     */
    void waitForPageToLoad();

    /**
     * 
     * @param element
     */
    void waitForElementInvisibility(By element);

    void waitForElementToBeClickable(By element);

    boolean waitForWebElementSelected(By chkDO, long timeOut);

    String getCurrentUrl();

    String getTitle();

    String getPageSource();

    void close();

    void quit();

    TargetLocator switchTo();

    Navigation navigate();

    Options manage();

    WebDriver getDriver();

    void setDriver(WebDriver driver);

    JavascriptExecutor getJs();

    void setJs(JavascriptExecutor js);

    long getTimeOutInSeconds();

    void setTimeOutInSeconds(long timeOutInSeconds);

}