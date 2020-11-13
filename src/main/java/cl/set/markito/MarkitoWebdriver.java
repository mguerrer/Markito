// Markito webdriver generic commands class.
// Marcos Guerrero
// 20-10-2020
package cl.set.markito;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


public class MarkitoWebdriver extends MarkitoBaseUtils {
    public  WebDriver driver = null;
    // private Map<String, Object> vars;
    public JavascriptExecutor js;
    public long timeOutInSeconds = 60;
    public final int WEB=0, IOS=1, ANDROID=1;
    public int platform = WEB;

    public MarkitoWebdriver() {
        println("Markito has born.");
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
        printf("SetTimeouts in %ld.\n", timeOutInSeconds);
    }
    /**Close the selected window.
     * Please refer to <a href="#
     * {@webdriver}">"https://www.selenium.dev/selenium/docs/api/java/com/thoughtworks/selenium/webdriven/commands/Close.html"</a>
     */
    public void CloseCurrentWindow() {
        println("CloseCurrentWindow.");
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
     * @param url
     */
    public void Get(String url) {
        printf("Get [%s]\n", url);
        driver.get(url);
    }
    /**
     * GetText of an element located by.
     * @param by
     */
    public String GetText(By by) {
        String text =  new WebDriverWait(driver, timeOutInSeconds).until(
            ExpectedConditions.elementToBeClickable(by)).getText();
        printf("GetText [%s] in object %s\n", text, by);
        return text;
    }
  /**
     * Switch to default content and waits for a frame using a frame handle.
     * @param frameHandle
     * @throws InterruptedException
     */
    public void SelectFrameByLocator(int frameHandle) throws InterruptedException {
        driver.switchTo().defaultContent();
        new WebDriverWait(driver, timeOutInSeconds).ignoring(StaleElementReferenceException.class)
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
        new WebDriverWait(driver, timeOutInSeconds).ignoring(StaleElementReferenceException.class)
                .ignoring(WebDriverException.class)
                .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt((driver.findElement(by))));
        Thread.sleep(3000);
        printf("SelectFrame %s\n", by);
    }
    /**
     * Click in a clickable element located using By.
     * @param by
     */
    public void Click(org.openqa.selenium.By by) {
        new WebDriverWait(driver, timeOutInSeconds).ignoring(StaleElementReferenceException.class)
                .ignoring(WebDriverException.class).until(ExpectedConditions.elementToBeClickable(by));
        driver.findElement(by).click();
        printf("Click %s\n", by);
    }
    /**
     * ClickAt in a clickable element located using By at position (x,y).
     * @param by
     */
    public void ClickAt(By by, int x, int y) {
        new WebDriverWait(driver, timeOutInSeconds).ignoring(StaleElementReferenceException.class)
                .ignoring(WebDriverException.class).until(ExpectedConditions.elementToBeClickable(by));
        new Actions(driver).moveToElement(driver.findElement(by), x, y).click().perform();
        printf("ClickAt %s x=%d y=%d\n", by, x, y);
    }
    /**
     * @param by
     */
    public void waitForElementVisible(By by) {
        printf("Waiting for element %s...", by.toString());
        new WebDriverWait(driver, timeOutInSeconds).ignoring(StaleElementReferenceException.class)
                .ignoring(WebDriverException.class).until(ExpectedConditions.visibilityOfElementLocated(by));
        printf("visible!!!");
    }
    /**
     * Simulates typing Keys over an editable element located By.
     * @param by
     * @param keys
     */
    public void SendKeys(By by, String keys) {
        printf("SendKeys %s to object %s\n", keys, by);
        new WebDriverWait(driver, timeOutInSeconds).ignoring(StaleElementReferenceException.class)
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
     * Waits for alert present and Clicks OK if present during timeout period.   
     */
    public void ClickOKOnAlert() {
        println("Clicking OK on alert.");
        WaitForAlertPresent();
        Alert alert = driver.switchTo().alert();
        alert.accept();
    }
    /** 
     * Waits for alert present and Clicks CANCEL if present during timeout period.   
     */
    public void ClickCancelOnAlert() {
        println("Clicking CANCEL on alert.");
        WaitForAlertPresent();
        Alert alert = driver.switchTo().alert();
        alert.dismiss();
    }
    /** 
     * Waits for alert present and gets the inner text to a String.
     * @return String
     */
    public String GetTextOfAlert() {
        printf("GetTextOfAlert: Waiting for alert present...");
        WaitForAlertPresent();
        printf("Present!  Now getting text!!");
        Alert alert = driver.switchTo().alert();
        String text = alert.getText();
        printf("Text retrieved=%s\n", text);
        return text;
    }
    /** 
     * Writes an string on the alert msg.
     * @param text
     */
    public void TypeTextOnAlert(String text) {
        printf("TypeTextOnAlert: Waiting for alert present...");
        WaitForAlertPresent();
        Alert alert = driver.switchTo().alert();
        alert.sendKeys(text);
        printf("Text sent=%s\n", text);
    }
    /** 
     * Waits for alert present during timeOutInSeconds period.
     */ 
    public void WaitForAlertPresent(){
        printf("WaitForAlertPresent: Waiting for alert present...");
        WebDriverWait wdWait = new WebDriverWait(driver, timeOutInSeconds);
        wdWait.until(ExpectedConditions.alertIsPresent());
        printf("Alert is present.\n");
    }
    /** 
     * Highlights an element id debug mode is ON.   
     * @param driver
     * @param element
     */
    public void highLightElement(WebElement element) {
        if (debug && platform==WEB)
            ExecuteJsScript("arguments[0].setAttribute('style', 'background: yellow; border: 3px solid blue;');", element);
    }
}
