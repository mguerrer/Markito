// Markito main class.
// Marcos Guerrero
// 20-10-2020
package cl.set.markito;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.support.ui.*;
import java.time.Duration;
import java.util.concurrent.TimeUnit;


public class Markito {
    public WebDriver driver = null;
    // private Map<String, Object> vars;
    private JavascriptExecutor js;
    private long timeOutInSeconds = 60;

    public Markito() {
    }

    public Markito(WebDriver driverObject) {
        driver = driverObject;
        js = (JavascriptExecutor) driver;
    }

    /**
     * 
     * @param timeOutInSeconds
     */
    public void SetTimeouts(long timeOutInSeconds) {
        this.timeOutInSeconds = timeOutInSeconds;
        driver.manage().timeouts().implicitlyWait(timeOutInSeconds, TimeUnit.SECONDS); // Timeouts de waitFor*
        driver.manage().timeouts().pageLoadTimeout(timeOutInSeconds, TimeUnit.SECONDS); // Timeout de espera de página
        driver.manage().timeouts().setScriptTimeout(timeOutInSeconds, TimeUnit.SECONDS); // Timeout de ejecución
                                                                                         // javascript
    }

    public void CloseDriver() {
        if (driver != null)
            driver.quit();
        System.out.println("Markito is destroyed.");
        OsUtils util = new OsUtils();
        // Kill Windows processes if they are running.
        util.killProcessIfRunning(util.CHROME_EXE);
        util.killProcessIfRunning(util.EDGE_EXE);
        util.killProcessIfRunning(util.FIREFOX_EXE);
        util.killProcessIfRunning(util.CHROMEDRIVER_EXE);
        util.killProcessIfRunning(util.EDGEDRIVER_EXE);
        util.killProcessIfRunning(util.FIREFOXDRIVER_EXE);
    }

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

    public void OpenDriver() {
        boolean headless = false;
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
     * @param frameLocator
     * @throws InterruptedException
     */
    public void SelectFrameByLocator(int frameLocator) throws InterruptedException {
        driver.switchTo().defaultContent();
        new WebDriverWait(driver, Duration.ofSeconds(30)).ignoring(StaleElementReferenceException.class)
                .ignoring(WebDriverException.class)
                .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameLocator));
        Thread.sleep(3000);
        System.out.printf("SelectFrame %d\n", frameLocator);
    }

    /**
     * @param by
     * @throws InterruptedException
     */
    public void SelectFrameBy(By by) throws InterruptedException {
        driver.switchTo().defaultContent();
        new WebDriverWait(driver, Duration.ofSeconds(30)).ignoring(StaleElementReferenceException.class)
                .ignoring(WebDriverException.class)
                .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt((driver.findElement(by))));
        Thread.sleep(3000);
        System.out.printf("SelectFrame %s\n", by);
    }

    /**
     * @param by
     */
    public void Click(By by) {
        new WebDriverWait(driver, Duration.ofSeconds(30)).ignoring(StaleElementReferenceException.class)
                .ignoring(WebDriverException.class).until(ExpectedConditions.elementToBeClickable(by));
        driver.findElement(by).click();
        System.out.printf("Click %s\n", by);
    }

    /**
     * @param by
     */
    public void waitForElementVisible(By by) {
        new WebDriverWait(driver, Duration.ofSeconds(30)).ignoring(StaleElementReferenceException.class)
                .ignoring(WebDriverException.class).until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    /**
     * @param by
     * @param keys
     */
    public void SendKeys(By by, String keys) {
        new WebDriverWait(driver, Duration.ofSeconds(30)).ignoring(StaleElementReferenceException.class)
                .ignoring(WebDriverException.class).until(ExpectedConditions.visibilityOfElementLocated(by));
        driver.findElement(by).sendKeys(keys);
    }

    public void ClearConsole() {
        System.out.print("\033[H\033[2J"); // Borra consola
        System.out.flush();
    }

    WebElement FindElement(By by) {
        WebElement element = driver.findElement(by);
        highLightElement(driver, element);
        return element;
    }

    public void highLightElement(WebDriver driver, WebElement element) {
        ExecuteJsScript("arguments[0].setAttribute('style', 'background: yellow; border: 3px solid blue;');", element);
    }

    public void ClickOKOnAlert() {
        WaitForAlertPresent();
        Alert alert = driver.switchTo().alert();
        alert.accept();
    }

    public void ClickCancelOnAlert() {
        WaitForAlertPresent();
        Alert alert = driver.switchTo().alert();
        alert.dismiss();
    }

    public String GetTextOfAlert() {
        WaitForAlertPresent();
        Alert alert = driver.switchTo().alert();
        return alert.getText();
    }

    public void TypeTextOnAlert(String text) {
        WaitForAlertPresent();
        Alert alert = driver.switchTo().alert();
        alert.sendKeys(text);
    }

    public void WaitForAlertPresent(){
        WebDriverWait wdWait = new WebDriverWait(driver, Duration.ofSeconds(timeOutInSeconds));
        wdWait.until(ExpectedConditions.alertIsPresent());
    }
}
