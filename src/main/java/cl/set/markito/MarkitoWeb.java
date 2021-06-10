// Markito web main class.
// Marcos Guerrero
// 20-10-2020
package cl.set.markito;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.html5.Location;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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
        driver.manage().timeouts().implicitlyWait(timeOutInSeconds, TimeUnit.SECONDS); // Timeouts de waitFor*
        try {
            driver.manage().timeouts().setScriptTimeout(timeOutInSeconds, TimeUnit.SECONDS); // Timeout de ejecución javascript
            driver.manage().timeouts().pageLoadTimeout(timeOutInSeconds, TimeUnit.SECONDS); // Timeout de espera de página
        } catch ( Exception e) {
            printf( ANSI_YELLOW+"SetTimeouts in %d seconds. WARNING can not set pageLoadTimeout/setScriptTimeout.\n", timeOutInSeconds);
        }
        printf( ANSI_YELLOW+"SetTimeouts in %d seconds.\n", timeOutInSeconds);
    }
    /**
     * Close the selected window. Please refer to "https://www.selenium.dev/selenium/docs/api/java/com/thoughtworks/selenium/webdriven/commands/Close.html"
     */
    public void CloseCurrentWindow() {
        println("CloseCurrentWindow.");
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
        printf( ANSI_YELLOW+"Get [%s]\n", url);
        driver.get(url);
    }
    /**
     * Get Text of an element located by.
     * 
     * @param by
     */
    public String GetText(By by) {
        String text = new WebDriverWait(driver, timeOutInSeconds)
                            .until(ExpectedConditions.presenceOfElementLocated(by))
                            .getText();
        printf( ANSI_YELLOW+"GetText [%s] in object %s\n", text, by);
        return text;
    }
    /**
     * Get Value of an element located by.
     * 
     * @param by
     */
    public String GetValue(By by) {
        String text = new WebDriverWait(driver, timeOutInSeconds)
                          .until(ExpectedConditions.presenceOfElementLocated(by)).getAttribute("value");
        printf( ANSI_YELLOW+"GetText [%s] in object %s\n", text, by);
        return text;
    }
    /**
     * Switch to default content and waits for a frame using a frame handle.
     * 
     * @param frameHandle
     * @throws InterruptedException
     */
    public void SelectFrameByLocator(int frameHandle) throws InterruptedException {
        driver.switchTo().defaultContent();
        new WebDriverWait(driver, timeOutInSeconds).ignoring(StaleElementReferenceException.class)
                .ignoring(WebDriverException.class)
                .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameHandle));
        printf( ANSI_YELLOW+"SelectFrame %d\n", frameHandle);
    }
    /**
     * Switch to default content and waits for a frame using a frame locator By.
     * 
     * @param by
     * @throws InterruptedException
     */
    public void SelectFrameBy(By by) throws InterruptedException {
        driver.switchTo().defaultContent();
        new WebDriverWait(driver, timeOutInSeconds).ignoring(StaleElementReferenceException.class)
                .ignoring(WebDriverException.class)
                .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt((driver.findElement(by))));
        printf( ANSI_YELLOW+"SelectFrame %s\n", by);
    }
    /**
     * Click in a clickable element located using By.
     * 
     * @param locator
     */
    public void Click(By locator) {
        printf( ANSI_YELLOW+"Clicking %s...", locator);
        try {
            WebElement element = driver.findElement(locator);
            if ( debug ) 
                HighLightElement( element );
            new WebDriverWait(driver, timeOutInSeconds).ignoring(StaleElementReferenceException.class)
                .ignoring(WebDriverException.class).until(ExpectedConditions.elementToBeClickable(locator));
            element.click();
            printf( ANSI_YELLOW+"done.\n", locator);
        } catch (Exception e) {
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
            WebElement element = driver.findElement(locator);
            if ( debug ) HighLightElement( element );
            // Do not add waits.
            ExecuteJsScript("arguments[0].click();", element);	
            printf( ANSI_YELLOW+"done.\n", locator);
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
    public void ClickSimulated(By locator) {
        printf( ANSI_YELLOW+"Clicking simulated %s...", locator);
        try {
            WebElement element = driver.findElement(locator);
            if ( debug ) HighLightElement( element );
            // Do not add waits.
            Actions builder = new Actions(driver);
            builder
            .moveToElement(element)
            .click(element)		
            .perform();
            printf( ANSI_YELLOW+"done.\n", locator);
        } catch (Exception e) {
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
        try {
            WebElement element = driver.findElement(locator);
            if ( debug ) HighLightElement( element );
            new WebDriverWait(driver, timeOutInSeconds).ignoring(StaleElementReferenceException.class)
                .ignoring(WebDriverException.class).until(ExpectedConditions.elementToBeClickable(locator));
            new Actions(driver).moveToElement( element, x, y).click().perform();
            printf( ANSI_YELLOW+"done.\n", locator);
        } catch (Exception e) {
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
        try {
            new WebDriverWait(driver, timeOutInSeconds).ignoring(StaleElementReferenceException.class)
                    .ignoring(WebDriverException.class).until(ExpectedConditions.presenceOfElementLocated(locator));
            printf( ANSI_YELLOW+"visible!!!\n");
        } catch (Exception e) {
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
        try {
            new WebDriverWait(driver, timeout).ignoring(StaleElementReferenceException.class)
                    .ignoring(WebDriverException.class).until(ExpectedConditions.presenceOfElementLocated(locator));
            printf( ANSI_YELLOW+"visible!!!\n");
            return true;
        } catch (Exception e) {
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
       try {
           new WebDriverWait(driver, timeOutInSeconds).ignoring(StaleElementReferenceException.class)
                   .ignoring(WebDriverException.class).until(ExpectedConditions.visibilityOfElementLocated(locator));
           printf( ANSI_YELLOW+"visible!!!\n");
       } catch (Exception e) {
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
        try {
            new WebDriverWait(driver, timeout).ignoring(StaleElementReferenceException.class)
                    .ignoring(WebDriverException.class).until(ExpectedConditions.visibilityOfElementLocated(locator));
            printf( ANSI_YELLOW+"visible!!!\n");
            return true;
        } catch (Exception e) {
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
        try {
            new WebDriverWait(driver, timeOutInSeconds)
                    .ignoring(StaleElementReferenceException.class)
                    .ignoring(WebDriverException.class)
                    .until(ExpectedConditions.and(
                        ExpectedConditions.visibilityOfElementLocated(locator),
                        ExpectedConditions.textToBe(locator, text))
                    );
            printf( ANSI_YELLOW+"visible!!!\n");
        } catch(Exception e){
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
    public boolean WaitForTextInElementVisible(String text, By locator, long timeout) {
        printf( ANSI_YELLOW+"Waiting for text %s in element %s...", text, locator.toString());
        try {
            new WebDriverWait(driver, timeout)
                    .ignoring(StaleElementReferenceException.class)
                    .ignoring(WebDriverException.class)
                    .until(ExpectedConditions.and(
                        ExpectedConditions.visibilityOfElementLocated(locator),
                        ExpectedConditions.textToBe(locator, text))
                    );
            printf( ANSI_YELLOW+"visible!!!\n");
            return true;
        } catch(Exception e){
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
        try {
            By by = By.xpath("//*[@text='"+text+"']");
            new WebDriverWait(driver, timeOutInSeconds).ignoring(StaleElementReferenceException.class)
            .ignoring(WebDriverException.class)
            .until(ExpectedConditions.presenceOfAllElementsLocatedBy(by));

            if (driver.getPageSource().contains(text)){
                printf( ANSI_YELLOW+"visible!!!\n");

            }
        } catch(Exception e){
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
        if ( debug ) HighLightElement(driver.findElement(locator));
        try {
            new WebDriverWait(driver, timeOutInSeconds).ignoring(StaleElementReferenceException.class)
            .ignoring(WebDriverException.class).until(ExpectedConditions.visibilityOfElementLocated(locator));
            driver.findElement(locator).sendKeys(keys);
            println("done!");
        } catch (Exception e) {
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
            //WaitForAlertPresent();
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
        WebDriverWait wdWait = new WebDriverWait(driver, timeOutInSeconds);
        try {
            wdWait.until(ExpectedConditions.alertIsPresent());
            printf( ANSI_YELLOW+"Alert is present.\n");
        } catch (Exception e) {
            printf( ANSI_RED+"Alert is not present.\n");
            throw new WebDriverException(e.getMessage());
        }
    }
    /**
     * Highlights an element id debug mode is ON.
     * 
     * @param element
     */
    public void HighLightElement(WebElement element) {
        ExecuteJsScript("arguments[0].setAttribute('style', 'background: yellow; border: 3px solid blue;');", element);
    }
    /**
     * Selects an option from by using VisibleText.
     * 
     * @param by
     * @param VisibleText
     */
    public void SelectOptionByVisibleText(By by, String VisibleText) {
        Select dropdown = new Select(driver.findElement(by));
        if ( debug ) HighLightElement(driver.findElement(by));
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
        if ( debug ) HighLightElement(driver.findElement(by));
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
        if ( debug ) HighLightElement(driver.findElement(by));
        dropdown.selectByValue(value);
        printf( ANSI_YELLOW+"SelectOptionByValue from %s value %s\n", by, value);
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


    public void WaitForinvisibilityElement(WebElement element) {
        printf( ANSI_YELLOW+"Waiting for invisibility of element %s...", element.toString());
        try {
            new WebDriverWait(driver, timeOutInSeconds).until(ExpectedConditions.invisibilityOf(element));
            printf( ANSI_YELLOW+"invisible!!!\n");
        } catch (Exception e) {
            printf( ANSI_RED+"failed!!! %s\n", e.getMessage());
            throw new WebDriverException( e.getMessage());
        }
    } 

    public void WaitForElementInteractuable(WebElement element) {
        printf( ANSI_YELLOW+"Waiting for Clickeable of element %s...", element.toString());
        try {
            new WebDriverWait(driver, timeOutInSeconds).until(ExpectedConditions.elementToBeClickable(element));
            printf( ANSI_YELLOW+"Clickeable!!!\n");
        } catch (Exception e) {
            printf( ANSI_RED+"failed!!! %s\n", e.getMessage());
            throw new WebDriverException( e.getMessage());
        }
    } 


    public boolean  WaitForWebElementSelected(WebElement element, long timeOut) {
        printf( ANSI_YELLOW+"Waiting for element %s...", element.toString());
        try {
            new WebDriverWait(driver, timeOut).until(ExpectedConditions.elementSelectionStateToBe(element,true));
            printf( ANSI_YELLOW+"Seleccionado!!!\n");
            return true;
        } catch (Exception e) {
            printf( ANSI_RED+"failed!!! %s\n", e.getMessage());
            return false;
        }
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
 }
