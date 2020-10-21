// Markito main class.
// Marcos Guerrero
// 20-10-2020
package cl.set.markito;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class Markito {
    public WebDriver driver = null;
    // private Map<String, Object> vars;
    private JavascriptExecutor js;
    private long timeOutInSeconds = 60;
    private boolean debug=true;
    private boolean headless = false;

    public Markito() {
    }

    public Markito(WebDriver driverObject) {
        driver = driverObject;
        js = (JavascriptExecutor) driver;
    }

    /**
     * Establish a unified timeout parameter for implicit waits, page loads and javascripts.
     * @param timeOutInSeconds
     */
    public void SetTimeouts(long timeOutInSeconds) {
        this.timeOutInSeconds = timeOutInSeconds;
        driver.manage().timeouts().implicitlyWait(timeOutInSeconds, TimeUnit.SECONDS); // Timeouts de waitFor*
        driver.manage().timeouts().pageLoadTimeout(timeOutInSeconds, TimeUnit.SECONDS); // Timeout de espera de página
        driver.manage().timeouts().setScriptTimeout(timeOutInSeconds, TimeUnit.SECONDS); // Timeout de ejecución javascript
    }
    /**
     * Prints an string to console when debug mode is ON.
     */
    void println(String x){
        if ( debug ) System.out.println(x);
    }    /**
    * Prints an string using format string to console when debug mode is ON.
    */
   void printf(String format, Object ... args){
       if ( debug ) System.out.printf(format, args);
   }
    /**
     * Close current webdriver session and collects possible garbage.
     */
    public void CloseDriver() {
        if (driver != null)
            driver.quit();
        println("Markito is destroyed.");
        OsUtils util = new OsUtils();
        // Kill Windows processes if they are running.
        util.killProcessIfRunning(util.CHROME_EXE);
        util.killProcessIfRunning(util.EDGE_EXE);
        util.killProcessIfRunning(util.FIREFOX_EXE);
        util.killProcessIfRunning(util.CHROMEDRIVER_EXE);
        util.killProcessIfRunning(util.EDGEDRIVER_EXE);
        util.killProcessIfRunning(util.FIREFOXDRIVER_EXE);
    }
    /**Close the selected window.
     * Please refer to <a href="#
     * {@webdriver}">"https://www.selenium.dev/selenium/docs/api/java/com/thoughtworks/selenium/webdriven/commands/Close.html"</a>
     */
    public void CloseCurrentWindow() {
        driver.close();
    }
    /**
     * Please refer to <a href="#
     * {@webdriver}">"https://www.selenium.dev/selenium/docs/api/java/org/openqa/selenium/remote/server/handler/ExecuteScript.html"</a>
     * 
     * @param script
     * @param element
     */
    public void ExecuteJsScript(String script, java.lang.Object... args) {
        js.executeScript(script, args);
    }

    /**
     * @param script
     * @param args
     */
    public void ExecuteAsynchromousJsScript(String script, String args) {
        js.executeAsyncScript(script, args);
    }
    /**
     * Default webdriver constructor that uses headless property.
     */
    public void OpenDriver() {
        if (headless) {
            ChromeOptions opciones = new ChromeOptions();
            opciones.addArguments("--headless", "--no-sandbox");
            driver = new ChromeDriver(opciones);
        } else
            driver = new ChromeDriver();
        js = (JavascriptExecutor) driver;
        // vars = new HashMap<String, Object>();
        driver.manage().timeouts().implicitlyWait(timeOutInSeconds, TimeUnit.SECONDS); // Timeouts de waitFor*
        driver.manage().timeouts().pageLoadTimeout(timeOutInSeconds, TimeUnit.SECONDS); // TImeout de espera de página
        driver.manage().timeouts().setScriptTimeout(timeOutInSeconds, TimeUnit.SECONDS); // Timeout de ejecución
                                                                                         // javascript
    }
    /**
     * @param url
     */
    public void Get(String url) {
        driver.get(url);
    }

    /**
     * Switch to default content and waits for a frame using a frame handle.
     * @param frameHandle
     * @throws InterruptedException
     */
    public void SelectFrameByLocator(int frameHandle) throws InterruptedException {
        driver.switchTo().defaultContent();
        new WebDriverWait(driver, Duration.ofSeconds(timeOutInSeconds)).ignoring(StaleElementReferenceException.class)
                .ignoring(WebDriverException.class)
                .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameHandle));
        Thread.sleep(3000);
        printf("SelectFrame %d\n", frameHandle);
    }
    /**
     * Switch to default content and waits for a frame using a frame locator By.
     * @param by
     * @throws InterruptedException
     */
    public void SelectFrameBy(By by) throws InterruptedException {
        driver.switchTo().defaultContent();
        new WebDriverWait(driver, Duration.ofSeconds(timeOutInSeconds)).ignoring(StaleElementReferenceException.class)
                .ignoring(WebDriverException.class)
                .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt((driver.findElement(by))));
        Thread.sleep(3000);
        printf("SelectFrame %s\n", by);
    }
    /**
     * Click in a clickable element located using By.
     * @param by
     */
    public void Click(By by) {
        new WebDriverWait(driver, Duration.ofSeconds(timeOutInSeconds)).ignoring(StaleElementReferenceException.class)
                .ignoring(WebDriverException.class).until(ExpectedConditions.elementToBeClickable(by));
        driver.findElement(by).click();
        printf("Click %s\n", by);
    }
    /**
     * ClickAt in a clickable element located using By at position (x,y).
     * @param by
     */
    public void ClickAt(By by, int x, int y) {
        new WebDriverWait(driver, Duration.ofSeconds(timeOutInSeconds)).ignoring(StaleElementReferenceException.class)
                .ignoring(WebDriverException.class).until(ExpectedConditions.elementToBeClickable(by));
        new Actions(driver).moveToElement(FindElement(by), x, y).click().perform();
        printf("ClickAt %s x=%d y=%d\n", by, x, y);
    }
    /**
     * @param by
     */
    public void waitForElementVisible(By by) {
        new WebDriverWait(driver, Duration.ofSeconds(timeOutInSeconds)).ignoring(StaleElementReferenceException.class)
                .ignoring(WebDriverException.class).until(ExpectedConditions.visibilityOfElementLocated(by));
    }
    /**
     * Simulates typing Keys over an editable element located By.
     * @param by
     * @param keys
     */
    public void SendKeys(By by, String keys) {
        new WebDriverWait(driver, Duration.ofSeconds(timeOutInSeconds)).ignoring(StaleElementReferenceException.class)
                .ignoring(WebDriverException.class).until(ExpectedConditions.visibilityOfElementLocated(by));
        driver.findElement(by).sendKeys(keys);
    }
    /**
     * Clears console output.
     */
    public void ClearConsole() {
        System.out.print("\033[H\033[2J"); // Borra consola
        System.out.flush();
    }
    /** 
     * Find an element in DOM using By locator.  When debug mode is ON highlights the element to help visual debug.
     * @param by
     * @return WebElement
     */
    WebElement FindElement(By by) {
        WebElement element = driver.findElement(by);
        highLightElement(element);
        return element;
    }    
    /** 
    * Find elements in DOM using By locator.  When debug mode is ON highlights the element to help visual debug.
    * @param by
    * @return WebElement
    */
    List <WebElement> FindElements(By by) {
        List <WebElement> elements = driver.findElements(by);
        elements.forEach(element -> { highLightElement(element);} ); // Highlights on debug mode.
        return elements;
    }
    /** 
     * Highlights an element id debug mode is ON.   
     * @param driver
     * @param element
     */
    public void highLightElement(WebElement element) {
        if (debug)
            ExecuteJsScript("arguments[0].setAttribute('style', 'background: yellow; border: 3px solid blue;');", element);
    }
    /** 
     * Waits for alert present and Clicks OK if present during timeout period.   
     */
    public void ClickOKOnAlert() {
        WaitForAlertPresent();
        Alert alert = driver.switchTo().alert();
        alert.accept();
    }
    /** 
     * Waits for alert present and Clicks CANCEL if present during timeout period.   
     */
    public void ClickCancelOnAlert() {
        WaitForAlertPresent();
        Alert alert = driver.switchTo().alert();
        alert.dismiss();
    }
    /** 
     * Waits for alert present and gets the inner text to a String.
     * @return String
     */
    public String GetTextOfAlert() {
        WaitForAlertPresent();
        Alert alert = driver.switchTo().alert();
        return alert.getText();
    }
    /** 
     * Writes an string on the alert msg.
     * @param text
     */
    public void TypeTextOnAlert(String text) {
        WaitForAlertPresent();
        Alert alert = driver.switchTo().alert();
        alert.sendKeys(text);
    }
    /** 
     * Waits for alert present during timeOutInSeconds period.
     */ 
    public void WaitForAlertPresent(){
        WebDriverWait wdWait = new WebDriverWait(driver, Duration.ofSeconds(timeOutInSeconds));
        wdWait.until(ExpectedConditions.alertIsPresent());
    }
}
