/*
All the code that follow is
Copyright (c) 2022, Marcos Guerrero. All Rights Reserved.
Not for reuse without permission.
*/

package cl.set.markito.samples.MarkitoWebApp.TheInternet.pages;

import java.util.Map;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import cl.set.markito.IMarkitoWeb;
import cl.set.markito.MarkitoWeb;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class DragAndDropPage {
    private WebDriver driver;
    private int timeout = 15;

    @FindBy(css = "a[href='http://elementalselenium.com/']")
    @CacheLookup
    private WebElement elementalSelenium;

    @FindBy(css = "a[href='https://github.com/tourdedave/the-internet']")
    @CacheLookup
    private WebElement forkMeOnGithub;

    @FindBy(id = "column-a")
    @CacheLookup
    public WebElement squareA;

    @FindBy(id = "column-b")
    @CacheLookup
    public WebElement squareB;

    private final String pageLoadedText = "";

    private final String pageUrl = "/drag_and_drop";

    public DragAndDropPage() {
    }

    public DragAndDropPage(WebDriver driver) {
        this();
        this.driver = driver;
    }

    public DragAndDropPage(WebDriver driver, Map<String, String> data) {
        this(driver);
    }

    public DragAndDropPage(WebDriver driver, Map<String, String> data, int timeout) {
        this(driver, data);
        this.timeout = timeout;
    }

    /**
     * Do drag and drop of squareA over squareB.
     */
    public void DragObjectAOverObjectB() {
        //Actions act = new Actions(this.driver);
        IMarkitoWeb markito = new MarkitoWeb(driver);

        // Dragged and dropped.
        squareA = driver.findElement(By.id("column-a"));
        squareB = driver.findElement(By.id("column-b"));

        markito.dragAndDrop(squareA, squareB);

        /*final String java_script =  "var src=arguments[0],tgt=arguments[1];var dataTransfer={dropEffe" +
                                    "ct:'',effectAllowed:'all',files:[],items:{},types:[],setData:fun" +
                                    "ction(format,data){this.items[format]=data;this.types.append(for" +
                                    "mat);},getData:function(format){return this.items[format];},clea" +
                                    "rData:function(format){}};var emit=function(event,target){var ev" +
                                    "t=document.createEvent('Event');evt.initEvent(event,true,false);" +
                                    "evt.dataTransfer=dataTransfer;target.dispatchEvent(evt);};emit('" +
                                    "dragstart',src);emit('dragenter',tgt);emit('dragover',tgt);emit(" +
                                    "'drop',tgt);emit('dragend',src);";
                        
        ((JavascriptExecutor) driver).executeScript(java_script, squareA, squareB);*/
     }

    /**
     * Click on Elemental Selenium Link.
     *
     * @return the DragAndDrop class instance.
     */
    public DragAndDropPage clickElementalSeleniumLink() {
        elementalSelenium.click();
        return this;
    }

    /**
     * Click on Fork Me On Github Link.
     *
     * @return the DragAndDrop class instance.
     */
    public DragAndDropPage clickForkMeOnGithubLink() {
        forkMeOnGithub.click();
        return this;
    }

    /**
     * Verify that the page loaded completely.
     *
     * @return the DragAndDrop class instance.
     */
    public DragAndDropPage verifyPageLoaded() {
        (new WebDriverWait(driver, timeout)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.getPageSource().contains(pageLoadedText);
            }
        });
        return this;
    }

    /**
     * Verify that current page URL matches the expected URL.
     *
     * @return the DragAndDrop class instance.
     */
    public DragAndDropPage verifyPageUrl() {
        (new WebDriverWait(driver, timeout)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.getCurrentUrl().contains(pageUrl);
            }
        });
        return this;
    }
}
