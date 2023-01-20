/*
All the code that follow is
Copyright (c) 2022, Marcos Guerrero. All Rights Reserved.
Not for reuse without permission.
*/

package cl.set.markito.samples.MarkitoWebApp.TheInternet;

import java.util.Map;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import cl.set.markito.framework.MarkitoWebApp;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class DragAndDropPage extends MarkitoWebApp {
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
        setDriver( driver );
    }

    public DragAndDropPage(WebDriver driver, Map<String, String> data) {
        setDriver( driver );
    }

    public DragAndDropPage(WebDriver driver, Map<String, String> data, int timeout) {
        this(driver, data);
        this.timeout = timeout;
    }

    /**
     * Do drag and drop of squareA over squareB.
     */
    public void DragObjectAOverObjectB() {
        // Dragged and dropped.
        squareA = findElement(By.id("column-a"));
        squareB = findElement(By.id("column-b"));

        dragAndDrop(squareA, squareB);

     }

    /**
     * Click on Elemental Selenium Link.
     *
     * @return the DragAndDrop class instance.
     */
    public DragAndDropPage clickElementalSeleniumLink() {
        click( elementalSelenium );
        return this;
    }

    /**
     * Click on Fork Me On Github Link.
     *
     * @return the DragAndDrop class instance.
     */
    public DragAndDropPage clickForkMeOnGithubLink() {
        click( forkMeOnGithub );
        return this;
    }

    /**
     * Verify that the page loaded completely.
     *
     * @return the DragAndDrop class instance.
     */
    public DragAndDropPage verifyPageLoaded() {
        (new WebDriverWait(getDriver(), timeout)).until(new ExpectedCondition<Boolean>() {
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
        (new WebDriverWait(getDriver(), timeout)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.getCurrentUrl().contains(pageUrl);
            }
        });
        return this;
    }
}
