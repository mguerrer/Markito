// Markito web main class.
// Marcos Guerrero
// 20-10-2020
package cl.set.markito;

import java.io.File;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;

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
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.html5.Location;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class MarkitoWeb extends MarkitoBaseUtils {
    public WebDriver driver = null;
    public Map<String, Object> vars;
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
    public void OpenChromeDriver(boolean headless) {
        if (headless) {
            ChromeOptions options = new ChromeOptions();
            printf("Opening Chrome session with desired %s\n", options);
            options.addArguments("--headless", "--no-sandbox");
            driver = new ChromeDriver(options);
        } else {
            println("Opening Chrome session all by default.\n");
            driver = new ChromeDriver();
        }
        js = (JavascriptExecutor) driver;
        vars = new HashMap<String, Object>();
        driver.manage().timeouts().implicitlyWait(timeOutInSeconds, TimeUnit.SECONDS); // Timeouts de waitFor*
        driver.manage().timeouts().pageLoadTimeout(timeOutInSeconds, TimeUnit.SECONDS); // TImeout de espera de página
        driver.manage().timeouts().setScriptTimeout(timeOutInSeconds, TimeUnit.SECONDS); // Timeout de ejecución
    }
    /***
     * Default firefox constructor that can use headless execution mode.
     * 
     * @param headless
     */
    public void OpenFirefoxDriver(boolean headless) {
        if (headless) {
            FirefoxOptions  options = new FirefoxOptions();
            printf("Opening Firefox session with desired %s\n", options);
            options.addArguments("-headless");
            driver = new FirefoxDriver(options);
        } else {
            println("Opening Firefox session all by default.\n");
            driver = new FirefoxDriver();
        }
        js = (JavascriptExecutor) driver;
        // vars = new HashMap<String, Object>();
        driver.manage().timeouts().implicitlyWait(timeOutInSeconds, TimeUnit.SECONDS); // Timeouts de waitFor*
        driver.manage().timeouts().pageLoadTimeout(timeOutInSeconds, TimeUnit.SECONDS); // TImeout de espera de página
        driver.manage().timeouts().setScriptTimeout(timeOutInSeconds, TimeUnit.SECONDS); // Timeout de ejecución
    }
    /**
     * Close current webdriver session and collects possible garbage.
     */
    public void CloseWebDriver() {
        if (driver != null) {
            driver.quit();
            driver=null;
            println("Markito is destroyed.");
        }
        MarkitoBaseUtils util = new MarkitoBaseUtils();
        // Kill Windows processes if they are running.
        util.killProcessIfRunning(util.CHROME_EXE);
        util.killProcessIfRunning(util.EDGE_EXE);
        util.killProcessIfRunning(util.FIREFOX_EXE);
        util.killProcessIfRunning(util.CHROMEDRIVER_EXE);
        util.killProcessIfRunning(util.EDGEDRIVER_EXE);
        util.killProcessIfRunning(util.FIREFOXDRIVER_EXE);
    }
     /** 
     * Find an element in DOM using By locator.  When debug mode is ON highlights the element to help visual debug.
     * @param by
     * @return WebElement
     */
    public WebElement FindElement(By by) {
        printf( ANSI_YELLOW+"Finding element %s...", by);
        try{
            WebElement element = driver.findElement(by);
            HighLightElement(element);
            printf( ANSI_YELLOW+"found.\n", by);
            return element;            
        } catch (Exception e) {
            printf( ANSI_RED+"failed!!! %s\n", e.getMessage());
            throw new WebDriverException( e.getMessage());
        }  
    }    
    /** 
    * Find elements in DOM using By locator.  When debug mode is ON highlights the element to help visual debug.
    * @param by
    * @return WebElement
    */
    public List <WebElement> FindElements(By by) {
        printf( ANSI_YELLOW+"Finding elements %s...", by);
        try {
            List <WebElement> elements = driver.findElements(by);
            elements.forEach(element -> { HighLightElement(element);} ); // Highlights on debug mode.
            printf( ANSI_YELLOW+"found %d elements.\n", elements.size());
            return elements;
        } catch (Exception e) {
            printf( ANSI_RED+"failed!!! %s\n", e.getMessage());
            throw new WebDriverException( e.getMessage());
        } 
    }
    /**
     * Establish a unified timeout parameter for implicit waits, page loads and
     * javascripts.
     * 
     * @param timeOutInSeconds
     */
    public void SetTimeouts(long timeOutInSeconds) {
        this.timeOutInSeconds = timeOutInSeconds;
        try {
            driver.manage().timeouts().implicitlyWait(timeOutInSeconds, TimeUnit.SECONDS); // Timeouts de waitFor*
            driver.manage().timeouts().setScriptTimeout(timeOutInSeconds, TimeUnit.SECONDS); // Timeout de ejecución javascript
            driver.manage().timeouts().pageLoadTimeout(timeOutInSeconds, TimeUnit.SECONDS); // Timeout de espera de página
        } catch ( Exception e) {
            printf( ANSI_YELLOW+"SetTimeouts in %d seconds. WARNING can not set pageLoadTimeout/setScriptTimeout.\n", timeOutInSeconds);
        }
        //printf( ANSI_YELLOW+"SetTimeouts in %d seconds.\n", timeOutInSeconds);
    }
    /**
     * Get a unified timeout parameter for implicit waits, page loads and
     * javascripts.
     * 
     * @param timeOutInSeconds
     */
    public long GetTimeouts() {
        return this.timeOutInSeconds;
    }

    /**
     * Select the window referred by the window handle.
     * @param windowHandle
     */
    public void SelectWindow(String windowHandle) {
        printf( ANSI_YELLOW+"SelectWindow...");
        try {
            driver.switchTo().window(windowHandle);
            printf( ANSI_YELLOW+"done... Window title %s\n", driver.getTitle());
        } catch (Exception e) {
            printf( ANSI_RED+"failed!!! %s\n", e.getMessage());
            throw new WebDriverException( e.getMessage());
        }
    }
     /**
     * Obtains the string handle be used to select the current window.
     */
    public String GetWindowHandle() {
        printf( ANSI_YELLOW+"GetWindowHandle...");
        try {
            String ventanasPrevias = driver.getWindowHandle();
            printf( ANSI_YELLOW+"done...");
            return ventanasPrevias;
        } catch (Exception e) {
            printf( ANSI_RED+"failed!!! %s\n", e.getMessage());
            throw new WebDriverException( e.getMessage());
        }
    }
    /**
     * Obtains a set of handles for all the current windows.
     * @return
     */
    public Set<String> GetWindowHandles() {
        printf( ANSI_YELLOW+"GetWindowHandles...");
        try {
            Set<String> ventanasPrevias = new HashSet<String>(driver.getWindowHandles());
            printf( ANSI_YELLOW+"done...\n");
            return ventanasPrevias;
        } catch (Exception e) {
            printf( ANSI_RED+"failed!!! %s\n", e.getMessage());
            throw new WebDriverException( e.getMessage());
        }
    }
    /**
     * Waits for a new window to be opened and returns its handle.
     * @return
     */
    public String WaitAndGetNewWindow(Set<String> priorWindows) {
        printf( ANSI_YELLOW+"Waiting for new window...");
        try {
            Set<String> currentWindows = null;
            Stopwatch stopwatch = Stopwatch.createStarted();
            do {
                currentWindows = driver.getWindowHandles();
                if (stopwatch.elapsed(TimeUnit.SECONDS)>timeOutInSeconds)
                    throw new WebDriverException("ERROR: New Window not found.");
                printf( ANSI_YELLOW+".");
            } while ( currentWindows.size() == priorWindows.size());
            for (String window : currentWindows) {
                if (! priorWindows.contains(window)) { // If not found is new.
                    printf( ANSI_YELLOW+"found %s...\n", window );
                    return window;
                }
            }
            throw new WebDriverException( "ERROR: New window not found.\n");
        } catch (Exception e) {
            printf( ANSI_RED+"failed!!! %s\n", e.getMessage());
            throw new WebDriverException( e.getMessage());
        }
    }
    /**
     * Close the selected window. Please refer to "https://www.selenium.dev/selenium/docs/api/java/com/thoughtworks/selenium/webdriven/commands/Close.html"
     */
    public void CloseCurrentWindow() {
        println(ANSI_YELLOW+"CloseCurrentWindow.");
        driver.close();
    }
    /**
     * Execute a JavaScript script.
     * Please refer to "https://www.selenium.dev/selenium/docs/api/java/org/openqa/selenium/remote/server/handler/ExecuteScript.html"
     * 
     * @param script
     * @param args
     */
    public void ExecuteJsScript(String script, java.lang.Object... args) {
        js.executeScript(script, args);
    }
    /**
     * Execute an asynchronous JavaScript script.
     * @param script
     * @param args
     */
    public void ExecuteAsynchromousJsScript(String script, String args) {
        js.executeAsyncScript(script, args);
    }
    /**
     * Get a url and waits for complete load.
     * @param url
     */
    public void Get(String url) {
        printf( ANSI_YELLOW+"Get [%s]", url);
        try{
            driver.get(url);
            printf( ANSI_YELLOW+"done!!!\n");
        } catch (Exception e) {
            printf( ANSI_RED+"failed!!! %s\n", e.getMessage());
            throw new WebDriverException( e.getMessage());
        } 
    }
    /**
     * Get Text of an element located by.
     * 
     * @param by
     */
    public String GetText(By by) {
        long currentTimeout = GetTimeouts();
        try {
            HighLightElement( by );
            driver.manage().timeouts().implicitlyWait(0,TimeUnit.SECONDS);
            String text = new WebDriverWait(driver, timeOutInSeconds)
                            .until(ExpectedConditions.presenceOfElementLocated(by))
                            .getText();
                            printf( ANSI_YELLOW+"done [%s] in object %s\n", text, by);
            SetTimeouts(currentTimeout);
            return text;
        } catch (Exception e) {
            SetTimeouts(currentTimeout);
            printf( ANSI_RED+"failed!!! %s\n", e.getMessage());
            throw new WebDriverException( e.getMessage());
        } 
    }
    /**
     * Get Value of an element located by.
     * 
     * @param by
     */
    public String GetValue(By by) {
        printf( ANSI_YELLOW+"GetValue ");
        long currentTimeout = GetTimeouts();
        try {
            HighLightElement( by );
            driver.manage().timeouts().implicitlyWait(0,TimeUnit.SECONDS);
            String text = new WebDriverWait(driver, timeOutInSeconds)
                            .until(ExpectedConditions.presenceOfElementLocated(by))
                            .getAttribute("value");
                            printf( ANSI_YELLOW+"done [%s] in object %s\n", text, by);
            SetTimeouts(currentTimeout);
            return text;
        } catch (Exception e) {
            SetTimeouts(currentTimeout);
            printf( ANSI_RED+"failed!!! %s\n", e.getMessage());
            throw new WebDriverException( e.getMessage());
        } 
    }
    /**
     * Sets the value of an attribute.
     * @param by
     * @param attributeName
     * @param value
     * @return Fails on attribute not found or other condition.
     */
    public void SetAttributeValue(By by, String attributeName, String value) {
        printf( ANSI_YELLOW+"SetAttributeValue ");
        long currentTimeout = GetTimeouts();
        try {
            HighLightElement( by );
            driver.manage().timeouts().implicitlyWait(0,TimeUnit.SECONDS);
            new WebDriverWait(driver, timeOutInSeconds).until(ExpectedConditions.presenceOfElementLocated(by)).getAttribute(attributeName);
            SetTimeouts(currentTimeout);
            ExecuteJsScript("arguments[0].setAttribute(arguments[1], arguments[2]);", driver.findElement(by), attributeName, value );
            printf( ANSI_YELLOW+"done set attribute [%s] with value [%s] in object %s\n", attributeName, value, by);
        } catch (Exception e) {
            SetTimeouts(currentTimeout);
            printf( ANSI_RED+"failed!!! %s\n", e.getMessage());
            throw new WebDriverException( e.getMessage());
        } 
    }
    /**
     * Waits for a frame by handle is available and switch to it.
     * 
     * @param frameHandle
     * @throws InterruptedException
     */
    public void SelectFrameByHandle(int frameHandle) throws InterruptedException {
        printf( ANSI_YELLOW+"SelectFrameByHandle %d...", frameHandle);
        long currentTimeout = GetTimeouts();
        try {
            driver.manage().timeouts().implicitlyWait(0,TimeUnit.SECONDS);
            new WebDriverWait(driver, timeOutInSeconds)
            .ignoring(StaleElementReferenceException.class)
            .ignoring(WebDriverException.class)
            .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameHandle));
            SetTimeouts(currentTimeout);
        } catch (Exception e) {
            SetTimeouts(currentTimeout);
            printf( ANSI_RED+"failed!!! %s\n", e.getMessage());
            throw new WebDriverException( e.getMessage());
        } 

    }
    /**
     * Waits for a frame using By reference is available and switch to it.
     * 
     * @param by
     * @throws InterruptedException
     */
    public void SelectFrameByLocator(By by) throws InterruptedException {
        printf( ANSI_YELLOW+"SelectFrameBy %s...", by);
        long currentTimeout = GetTimeouts();
        try {
            driver.manage().timeouts().implicitlyWait(0,TimeUnit.SECONDS);
            new WebDriverWait(driver, timeOutInSeconds).ignoring(StaleElementReferenceException.class)
            .ignoring(WebDriverException.class)
            .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(by));
            SetTimeouts(currentTimeout);
            printf( ANSI_YELLOW+"done...\n");
        } catch (Exception e) {
            SetTimeouts(currentTimeout);
            printf( ANSI_RED+"failed!!! %s\n", e.getMessage());
            throw new WebDriverException( e.getMessage());
        }

    }
    public void SelectFrameById(String frameID)  {
        printf( ANSI_YELLOW+"SelectFrameBy %s...", frameID);
        long currentTimeout = GetTimeouts();
        try {
            driver.manage().timeouts().implicitlyWait(0,TimeUnit.SECONDS);
            new WebDriverWait(driver, timeOutInSeconds)
            .ignoring(StaleElementReferenceException.class)
            .ignoring(WebDriverException.class)
            .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameID));
            //driver.switchTo().frame( frameID );
            SetTimeouts(currentTimeout);
            printf( ANSI_YELLOW+"done...\n");
        } catch (Exception e) {
            SetTimeouts(currentTimeout);
            printf( ANSI_RED+"failed!!! %s\n", e.getMessage());
            throw new WebDriverException( e.getMessage());
        }
    }
    /**
     * Waits for an element tobe clickable located using By and click in it.
     * @param locator
     */
    public void Click(By locator) {
        printf( ANSI_YELLOW+"Clicking %s...", locator);
        long currentTimeout = GetTimeouts();
        try {
            HighLightElement( locator );
            driver.manage().timeouts().implicitlyWait(0,TimeUnit.SECONDS);
            new WebDriverWait(driver, timeOutInSeconds)
                .ignoring(StaleElementReferenceException.class)
                .ignoring(WebDriverException.class)
                .until(ExpectedConditions.elementToBeClickable(locator));
            driver.findElement(locator).click();
            SetTimeouts(currentTimeout);
            printf( ANSI_YELLOW+"done.\n", locator);
        } catch (Exception e) {
            SetTimeouts(currentTimeout);
            printf( ANSI_RED+"failed!!! %s\n", e.getMessage());
            throw new WebDriverException( e.getMessage());
        }    
    }
    /**
     * Click in a clickable element located using By and bypassing webdriver.
     * 
     * @param locator
     */
    public void ClickJS(By locator) {
        printf( ANSI_YELLOW+"Clicking JS %s...", locator);
        try {
            HighLightElement( locator );
            // Do not add waits.
            ExecuteJsScript("arguments[0].click();", driver.findElement(locator));	
            printf( ANSI_YELLOW+"done.\n", driver.findElement(locator));
        } catch (Exception e) {
            printf( ANSI_RED+"failed!!! %s\n", e.getMessage());
            throw new WebDriverException( e.getMessage());
        }    
    }
    /**
     * Click in a element located using webdriver Actions.
     * 
     * @param locator
     */
    public void ClickUsingActions(By locator) {
        printf( ANSI_YELLOW+"Clicking simulated %s...", locator);
        long currentTimeout = GetTimeouts();
        try {
            HighLightElement( locator );
            driver.manage().timeouts().implicitlyWait(0,TimeUnit.SECONDS);
            new WebDriverWait(driver, timeOutInSeconds)
                .ignoring(StaleElementReferenceException.class)
                .ignoring(WebDriverException.class)
                .until(ExpectedConditions.elementToBeClickable(locator));
  
            WebElement element = driver.findElement(locator);
            HighLightElement( element );
            // Do not add waits.
            Actions builder = new Actions(driver);
            builder
            .moveToElement(element)
            .click(element)		
            .perform();
            printf( ANSI_YELLOW+"done.\n", locator);
            SetTimeouts(currentTimeout);
        } catch (Exception e) {
            SetTimeouts(currentTimeout);
            printf( ANSI_RED+"failed!!! %s\n", e.getMessage());
            throw new WebDriverException( e.getMessage());
        }    
    }
     /**
     * MouseOver in a element located using webdriver Actions.
     * 
     * @param locator
     */
    public void MouseOver(By locator) {
        printf( ANSI_YELLOW+"MouseOver sobre %s...", locator);
        long currentTimeout = GetTimeouts();
        try {
            HighLightElement( locator );
            driver.manage().timeouts().implicitlyWait(0,TimeUnit.SECONDS);
            new WebDriverWait(driver, timeOutInSeconds)
                .ignoring(StaleElementReferenceException.class)
                .ignoring(WebDriverException.class)
                .until(ExpectedConditions.elementToBeClickable(locator));
  
            WebElement element = driver.findElement(locator);
            HighLightElement( element );
            // Do not add waits.
            Actions builder = new Actions(driver);
            builder
            .moveToElement(element)
            .perform();
            printf( ANSI_YELLOW+"done.\n", locator);
            SetTimeouts(currentTimeout);
        } catch (Exception e) {
            SetTimeouts(currentTimeout);
            printf( ANSI_RED+"failed!!! %s\n", e.getMessage());
            throw new WebDriverException( e.getMessage());
        }    
    }
    /**
     * MouseOut in a element located using webdriver Actions.
     * 
     * @param locator
     */
    public void MouseOut(By locator) {
    printf( ANSI_YELLOW+"MouseOut sobre %s...", locator);
    long currentTimeout = GetTimeouts();
    try {
        HighLightElement( locator );
        driver.manage().timeouts().implicitlyWait(0,TimeUnit.SECONDS);
        new WebDriverWait(driver, timeOutInSeconds)
            .ignoring(StaleElementReferenceException.class)
            .ignoring(WebDriverException.class)
            .until(ExpectedConditions.elementToBeClickable(locator));

        WebElement element = driver.findElement(locator);
        HighLightElement( element );

        // Do not add waits.
        Actions builder = new Actions(driver);
        builder
        .moveToElement(element)
        .release(element)
        .perform();
        printf( ANSI_YELLOW+"done.\n", locator);
        SetTimeouts(currentTimeout);
    } catch (Exception e) {
        SetTimeouts(currentTimeout);
        printf( ANSI_RED+"failed!!! %s\n", e.getMessage());
        throw new WebDriverException( e.getMessage());
    }    
}
    /**
     * ClickAt in a clickable element located using By at position (x,y).
     * 
     * @param locator
     */
    public void ClickAt(By locator, int x, int y) {
        printf( ANSI_YELLOW+"ClickAt %s x=%d y=%d", locator, x, y);
        long currentTimeout = GetTimeouts();
        try {
            HighLightElement( locator );
            driver.manage().timeouts().implicitlyWait(0,TimeUnit.SECONDS);
            HighLightElement( locator );
            new WebDriverWait(driver, timeOutInSeconds).ignoring(StaleElementReferenceException.class)
                .ignoring(WebDriverException.class).until(ExpectedConditions.elementToBeClickable(locator));
            new Actions(driver).moveToElement( driver.findElement(locator), x, y).click().perform();
            printf( ANSI_YELLOW+"done.\n", locator);
            SetTimeouts(currentTimeout);
        } catch (Exception e) {
            SetTimeouts(currentTimeout);
            printf( ANSI_RED+"failed!!! %s\n", e.getMessage());
            throw new WebDriverException( e.getMessage());
        }  
    }
    /**
     * Waits for an element to be present and fail if not found on timeOutInSeconds.
     * @param locator By of the element.
     */
    public void WaitForElementPresent(By locator) {
        printf( ANSI_YELLOW+"Waiting for element %s...", locator.toString());
        long currentTimeout = GetTimeouts();
        try {
            driver.manage().timeouts().implicitlyWait(0,TimeUnit.SECONDS);
            new WebDriverWait(driver, timeOutInSeconds).ignoring(StaleElementReferenceException.class)
                    .ignoring(WebDriverException.class).until(ExpectedConditions.presenceOfElementLocated(locator));
            printf( ANSI_YELLOW+"present!!!\n");
            SetTimeouts(currentTimeout);
        } catch (Exception e) {
            SetTimeouts(currentTimeout);
            printf( ANSI_RED+"failed!!! %s\n", e.getMessage());
            throw new WebDriverException( e.getMessage());
        }
    } 
    /**
     * Waits for an element to be present and not fail if not found on timeout, instead it will return true or false.
     * @param locator
     * @param timeout max time to wait for element to be visible.
     * @return true if element is visible!!! false if element is not visible.
     */
    public boolean WaitForElementPresent(By locator, long timeout) {
        printf( ANSI_YELLOW+"Waiting for element %s on %d seconds...", locator.toString(), timeout);
        long currentTimeout = GetTimeouts();
        try {
            HighLightElement( locator );
            driver.manage().timeouts().implicitlyWait(0,TimeUnit.SECONDS);
            new WebDriverWait(driver, timeout).ignoring(StaleElementReferenceException.class)
                    .ignoring(WebDriverException.class).until(ExpectedConditions.presenceOfElementLocated(locator));
            printf( ANSI_YELLOW+"visible!!!\n");
            SetTimeouts(currentTimeout);
            return true;
        } catch (Exception e) {
            SetTimeouts(currentTimeout);
            printf( ANSI_RED+"failed!!! %s\n", e.getMessage());
            return false;
        }
    }   
    /**
    * Waits for an element to be visible and fail if not found on timeOutInSeconds.
    * @param locator
    */
    public void WaitForElementVisible(By locator) {
       printf( ANSI_YELLOW+"Waiting for element %s...", locator.toString());
       long currentTimeout = GetTimeouts();
       try {
            driver.manage().timeouts().implicitlyWait(0,TimeUnit.SECONDS);
            new WebDriverWait(driver, timeOutInSeconds).ignoring(StaleElementReferenceException.class)
                   .ignoring(WebDriverException.class).until(ExpectedConditions.visibilityOfElementLocated(locator));
            printf( ANSI_YELLOW+"visible!!!\n");
            SetTimeouts(currentTimeout);
       } catch (Exception e) {
            SetTimeouts(currentTimeout);
            printf( ANSI_RED+"failed!!! %s\n", e.getMessage());
            throw new WebDriverException( e.getMessage());
       }
   }
    /**
     * Waits for an element to be visible and not fail if not found on timeout, instead it will return true or false.
     * @param locator
     * @param timeout max time to wait for element to be visible.
     * @return true or false depending on finding the element in timeout seconds.
     */
    public boolean WaitForElementVisible(By locator, long timeout) {
        printf( ANSI_YELLOW+"Waiting for element %s on %d seconds...", locator.toString(), timeout);
        long currentTimeout = GetTimeouts();
        try {
            driver.manage().timeouts().implicitlyWait(0,TimeUnit.SECONDS);
            new WebDriverWait(driver, timeout).ignoring(StaleElementReferenceException.class)
                    .ignoring(WebDriverException.class).until(ExpectedConditions.visibilityOfElementLocated(locator));
            printf( ANSI_YELLOW+"visible!!!\n");
            SetTimeouts(currentTimeout);
            return true;
        } catch (Exception e) {
            SetTimeouts(currentTimeout);
            printf( ANSI_RED+"no visible!!! %s\n", e.getMessage());
            return false;
        }
    }
    /**
     * Waits for text in element to be visible.
     * @param text: Text to wait for
     * @param locator: Locator to element.
     * @throws WebDriverException when not found in timeoutInSeconds.
     */
    public void WaitForTextInElementVisible(String text, By locator) {
        printf( ANSI_YELLOW+"Waiting for text %s in element %s...", text, locator.toString());
        long currentTimeout = GetTimeouts();
        try {
            driver.manage().timeouts().implicitlyWait(0,TimeUnit.SECONDS);
            new WebDriverWait(driver, timeOutInSeconds)
                    .ignoring(StaleElementReferenceException.class)
                    .ignoring(WebDriverException.class)
                    .until(ExpectedConditions.and(
                        ExpectedConditions.visibilityOfElementLocated(locator),
                        ExpectedConditions.textToBe(locator, text))
                    );
            SetTimeouts(currentTimeout);
            printf( ANSI_YELLOW+"visible!!!\n");
        } catch(Exception e){
            SetTimeouts(currentTimeout);
            printf( ANSI_YELLOW+"failed after %d seconds!!!\n", timeOutInSeconds);
            throw new WebDriverException( e.getMessage());
        }   
    }
     /**
     * Waits for text in element to be visible .
     * @param text: Text to wait for
     * @param locator: Locator to element.
     * @param timeout: Time to wait for.
     * @return true if text is found on element before timeout seconds.
     */
    public boolean WaitForTextInVisibleElement(String text, By locator, long timeout) {
        printf( ANSI_YELLOW+"Waiting for text %s in element %s...", text, locator.toString());
        long currentTimeout = GetTimeouts();
        try {
            driver.manage().timeouts().implicitlyWait(0,TimeUnit.SECONDS);
            new WebDriverWait(driver, timeout)
                    .ignoring(StaleElementReferenceException.class)
                    .ignoring(WebDriverException.class)
                    .until(ExpectedConditions.and(
                        ExpectedConditions.visibilityOfElementLocated(locator),
                        ExpectedConditions.textToBe(locator, text))
                    );
            printf( ANSI_YELLOW+"visible!!!\n");
            SetTimeouts(currentTimeout);
            return true;
        } catch(Exception e){
            SetTimeouts(currentTimeout);
            printf( ANSI_YELLOW+"failed after %d seconds!!!\n", timeout);
            return false;
        }   
    }
    /**
     * Waits for text in page to be visible.
     * @param text: Text to wait for
     */
    public void WaitForTextInPage(String text) {
        printf( ANSI_YELLOW+"Searching for text %s...", text);
        long currentTimeout = GetTimeouts();
        try {
            By by = By.xpath("//*[@text='"+text+"']");
            driver.manage().timeouts().implicitlyWait(0,TimeUnit.SECONDS);
            new WebDriverWait(driver, timeOutInSeconds).ignoring(StaleElementReferenceException.class)
            .ignoring(WebDriverException.class)
            .until(ExpectedConditions.presenceOfAllElementsLocatedBy(by));

            if (driver.getPageSource().contains(text)){
                printf( ANSI_YELLOW+"visible!!!\n");
            }
            SetTimeouts(currentTimeout);
        } catch(Exception e){
            SetTimeouts(currentTimeout);
            printf( ANSI_RED+"failed: %s\n", e.getMessage());
            throw new WebDriverException( e.getMessage());
        }
    }
    /**
     * Simulates typing Keys over an editable element located By.
     * 
     * @param locator
     * @param keys
     */
    public void SendKeys(By locator, String keys) {
        printf( ANSI_YELLOW+"SendKeys %s to object %s...", keys, locator);
        long currentTimeout = GetTimeouts();
        HighLightElement(driver.findElement(locator));
        try {
            driver.manage().timeouts().implicitlyWait(0,TimeUnit.SECONDS);
            new WebDriverWait(driver, timeOutInSeconds).ignoring(StaleElementReferenceException.class)
            .ignoring(WebDriverException.class).until(ExpectedConditions.visibilityOfElementLocated(locator));
            driver.findElement(locator).sendKeys(keys);
            SetTimeouts(currentTimeout);
            println("done!");
        } catch (Exception e) {
            SetTimeouts(currentTimeout);
            printf(ANSI_RED+"failed! %s\n", e.getMessage());
            throw new WebDriverException( e.getMessage());
        }
    }
    /**
     * Waits for alert present and Clicks OK if present during timeout period.
     */
    public void ClickOKOnAlert() {
        printf( ANSI_YELLOW+"Clicking OK on alert...");
        try {
            WaitForAlertPresent();
            Alert alert = driver.switchTo().alert();
            alert.accept();
            printf( ANSI_YELLOW+"done.\n");
        } catch (Exception e) {
            printf(ANSI_RED+"failed! %s\n", e.getMessage());
            throw new WebDriverException( e.getMessage());
        }
    }
    /**
     * Waits for alert present and Clicks CANCEL if present during timeout period.
     */
    public void ClickCancelOnAlert() {
        try {
            printf( ANSI_YELLOW+"Clicking CANCEL on alert...");
            WaitForAlertPresent();
            Alert alert = driver.switchTo().alert();
            alert.dismiss();
            printf( ANSI_YELLOW+"done.\n");
        } catch (Exception e) {
            printf(ANSI_RED+"failed! %s\n", e.getMessage());
            throw new WebDriverException( e.getMessage());
        }            
    }
    /**
     * Waits for alert present and gets the inner text to a String.
     * 
     * @return String
     */
    public String GetTextOfAlert() {
        try {
            printf( ANSI_YELLOW+"GetTextOfAlert: Waiting for alert present...");
            WaitForAlertPresent();
            printf( ANSI_YELLOW+"Present!  Now getting text!!..");
            Alert alert = driver.switchTo().alert();
            String text = alert.getText();
            printf( ANSI_YELLOW+"Text retrieved=%s\n", text);
            return text;
        } catch (Exception e) {
            printf(ANSI_RED+"failed! %s\n", e.getMessage());
            throw new WebDriverException( e.getMessage());
        }        
    }
    /**
     * Writes an string on the alert msg.
     * 
     * @param text
     */
    public void TypeTextOnAlert(String text) {
        printf( ANSI_YELLOW+"TypeTextOnAlert: Waiting for alert present...");
        WaitForAlertPresent();
        try {
            Alert alert = driver.switchTo().alert();
            alert.sendKeys(text);
            printf( ANSI_YELLOW+"Text sent=%s\n", text);
        } catch (Exception e){
            printf( ANSI_RED+"ERROR:Text not sent=%s\n", e.getMessage());
            throw new WebDriverException(e.getMessage());
        }
    }
    /**
     * Waits for alert present during timeOutInSeconds period.
     */
    public void WaitForAlertPresent() {
        printf( ANSI_YELLOW+"WaitForAlertPresent: Waiting for alert present...");
        long currentTimeout = GetTimeouts();

        try {
            driver.manage().timeouts().implicitlyWait(0,TimeUnit.SECONDS);
            new WebDriverWait(driver, timeOutInSeconds).ignoring(StaleElementReferenceException.class).until(ExpectedConditions.alertIsPresent());
            SetTimeouts(currentTimeout);
            printf( ANSI_YELLOW+"Alert is present.\n");
        } catch (Exception e) {
            SetTimeouts(currentTimeout);
            printf( ANSI_RED+"Alert is not present.\n");
            throw new WebDriverException(e.getMessage());
        }
    }
    /**
     * Highlights WebElement if debug mode is ON.
     * 
     * @param element
     */
    public void HighLightElement(WebElement element) {
        if (debug)
            ExecuteJsScript("arguments[0].setAttribute('style', 'background: yellow; border: 3px solid blue;');", element);
    }
     /**
     * Highlights an element By if debug mode is ON.
     * 
     * @param element
     */
    public void HighLightElement(By element) {
        if (debug)
            ExecuteJsScript("arguments[0].setAttribute('style', 'background: yellow; border: 3px solid blue;');", driver.findElement(element));
    }
    /**
     * Selects an option from by using VisibleText.
     * 
     * @param by
     * @param VisibleText
     */
    public void SelectOptionByVisibleText(By by, String VisibleText) {
        Select dropdown = new Select(driver.findElement(by));
        HighLightElement(driver.findElement(by));
        dropdown.selectByVisibleText(VisibleText);
        printf( ANSI_YELLOW+"SelectOptionByVisibleText from %s option %s\n", by, VisibleText);
    }
    /**
     * Selects an option from by using integer index.
     * 
     * @param by
     * @param index
     */
    public void SelectOptionByIndex(By by, int index) {
        Select dropdown = new Select(driver.findElement(by));
        HighLightElement(driver.findElement(by));
        dropdown.selectByIndex(index);
        printf( ANSI_YELLOW+"SelectOptionByIndex from %s index %s\n", by, index);
    }
    /**
     * Selects an option from by using current value.
     * 
     * @param by
     * @param value
     */
    public void SelectOptionByValue(By by, String value) {
        Select dropdown = new Select(driver.findElement(by));
        HighLightElement(driver.findElement(by));
        dropdown.selectByValue(value);
        printf( ANSI_YELLOW+"SelectOptionByValue from %s value %s\n", by, value);
    }
    public void SetLocation( double x, double y, double z ) {
        printf(ANSI_YELLOW + "Set location to %f, %f, %f...", x,y,z);
        try {
            ((ChromeDriver) driver).setLocation(new Location(x, y, z));

            printf(ANSI_YELLOW + "done!!!\n");
        } catch (Exception e) {
            printf(ANSI_RED + "failed!!! %s\n", e.getMessage());
            throw new WebDriverException(e.getMessage());
        }
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
    protected boolean isElementPresent(By selector) {
        printf(ANSI_YELLOW+"Is element present.???..." + selector );    
        WaitForElementPresent(selector);         
        boolean returnVal = true;
        try{
            driver.findElement(selector);
            printf(ANSI_YELLOW+"yes.\n");
        } catch (NoSuchElementException e){
            printf(ANSI_YELLOW+"no.\n");  
            returnVal = false;
        } 
        return returnVal;
    }
    /***
   * Takes an screenshot and leaves it in file named TestResults\Name-Date.png
   * @param Name: The name you want to recognize.
   */
    public void TakeScreenSnapshotWithDate(String Name) {
        LocalDateTime ldt = LocalDateTime.now();
        String date = ldt.toString().replaceAll("\\W+", "");
        try {
            TakeScreenSnapshot("TestResults\\"+Name + "-" + date + ".png");
        } catch (Exception e) {
            printf("ERROR al tomar snapshot. Stack:%s\n", e.getMessage());
        }
    }
    /**
     * Waits for a a full page load by reading document.readyState.
     */
    public  void WaitForPageToLoad() {
        printf(ANSI_YELLOW+"WaitForPageToLoad...");
        try {
            String pageLoadStatus = null;
            do {
              js = (JavascriptExecutor) driver;
              pageLoadStatus = (String)js.executeScript("return document.readyState");
            } while ( !pageLoadStatus.equals("complete") );
            printf(ANSI_YELLOW+"done.\n");
        } catch (Exception e) {
            printf( ANSI_RED+"failed. Stack=%s\n", e.getMessage());
            throw new WebDriverException(e.getMessage());
        }
    }
    /**
     * 
     * @param element
     */
    public void WaitForElementInvisibility(By element) {
        printf( ANSI_YELLOW+"Waiting for invisibility of element %s...", element.toString());
        long currentTimeout = GetTimeouts();
        try {
            driver.manage().timeouts().implicitlyWait(0,TimeUnit.SECONDS);
            new WebDriverWait(driver, timeOutInSeconds)
            .until(ExpectedConditions.invisibilityOf(driver.findElement(element)));
            printf( ANSI_YELLOW+"invisible!!!\n");
            SetTimeouts(currentTimeout);
        } catch (Exception e) {
            printf( ANSI_RED+"failed!!! %s\n", e.getMessage());
            SetTimeouts(currentTimeout);
            throw new WebDriverException( e.getMessage());
        }
    } 
    public void WaitForElementToBeClickable(By element) {
        printf( ANSI_YELLOW+"Waiting for Clickeable of element %s...", element.toString());
        long currentTimeout = GetTimeouts();
        try {
            driver.manage().timeouts().implicitlyWait(0,TimeUnit.SECONDS);
            new WebDriverWait(driver, timeOutInSeconds).until(ExpectedConditions.elementToBeClickable(element));
            printf( ANSI_YELLOW+"Clickeable!!!\n");
            SetTimeouts(currentTimeout);
        } catch (Exception e) {
            printf( ANSI_RED+"failed!!! %s\n", e.getMessage());
            SetTimeouts(currentTimeout);
            throw new WebDriverException( e.getMessage());
        }
    } 
    public boolean WaitForWebElementSelected(By chkDO, long timeOut) {
        printf( ANSI_YELLOW+"Waiting for element %s...", chkDO.toString());
        long currentTimeout = GetTimeouts();
        try {
            driver.manage().timeouts().implicitlyWait(0,TimeUnit.SECONDS);
            new WebDriverWait(driver, timeOut).until(ExpectedConditions.elementSelectionStateToBe(chkDO,true));
            printf( ANSI_YELLOW+"Seleccionado!!!\n");
            SetTimeouts(currentTimeout);
            return true;
        } catch (Exception e) {
            printf( ANSI_RED+"failed!!! %s\n", e.getMessage());
            SetTimeouts(currentTimeout);
            return false;
        }
    } 
}
