// Markito web main class.
// Marcos Guerrero
// 20-10-2020
package cl.set.markito;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class MarkitoWeb extends MarkitoWebdriver{
    // private Map<String, Object> vars;
    private boolean headless = false;

    public MarkitoWeb() {
    }

    public MarkitoWeb(WebDriver driverObject) {
        driver = driverObject;
        js = (JavascriptExecutor) driver;
    }
    /**
     * Default webdriver constructor that uses Chrome in normal execution mode.
     */
    public void OpenWebDriver() {
        if (headless) {
            ChromeOptions options = new ChromeOptions();
            printf("Opening Android Session in Chrome with desired %s\n", options);
            options.addArguments("--headless", "--no-sandbox");
            driver = new ChromeDriver(options);
        } else {
            println("Opening Android Session in Chrome all by default.");
            driver = new ChromeDriver();
        }
        js = (JavascriptExecutor) driver;
        // vars = new HashMap<String, Object>();
        driver.manage().timeouts().implicitlyWait(timeOutInSeconds, TimeUnit.SECONDS); // Timeouts de waitFor*
        driver.manage().timeouts().pageLoadTimeout(timeOutInSeconds, TimeUnit.SECONDS); // TImeout de espera de página
        driver.manage().timeouts().setScriptTimeout(timeOutInSeconds, TimeUnit.SECONDS); // Timeout de ejecución
                                                                                         // javascript
    }
    /**
     * Default webdriver constructor that uses Chrome in headless execution mode.
     */
    public void OpenHeadlessWebDriver() {
        headless = true;
        OpenWebDriver();
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
        WebElement element = driver.findElement(by);
        highLightElement(element);
        return element;
    }    
    /** 
    * Find elements in DOM using By locator.  When debug mode is ON highlights the element to help visual debug.
    * @param by
    * @return WebElement
    */
    public List <WebElement> FindElements(By by) {
        List <WebElement> elements = driver.findElements(by);
        elements.forEach(element -> { highLightElement(element);} ); // Highlights on debug mode.
        return elements;
    }
 }
