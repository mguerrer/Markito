/*
All the code that follow is
Copyright (c) 2022, Marcos Guerrero. All Rights Reserved.
Not for reuse without permission.
*/

package cl.set.markito.tests.MarkitoWeb.framework.TheInternet.pages;
import java.util.List;
import java.util.Map;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class DragAndDrop {
    private Map<String, String> data;
    private WebDriver driver;
    private int timeout = 15;

    @FindBy(css = "a[href='http://elementalselenium.com/']")
    @CacheLookup
    private WebElement elementalSelenium;

    @FindBy(css = "a[href='https://github.com/tourdedave/the-internet']")
    @CacheLookup
    private WebElement forkMeOnGithub;

    private final String pageLoadedText = "";

    private final String pageUrl = "/drag_and_drop";

    public DragAndDrop() {
    }

    public DragAndDrop(WebDriver driver) {
        this();
        this.driver = driver;
    }

    public DragAndDrop(WebDriver driver, Map<String, String> data) {
        this(driver);
        this.data = data;
    }

    public DragAndDrop(WebDriver driver, Map<String, String> data, int timeout) {
        this(driver, data);
        this.timeout = timeout;
    }

    /**
     * Click on Elemental Selenium Link.
     *
     * @return the DragAndDrop class instance.
     */
    public DragAndDrop clickElementalSeleniumLink() {
        elementalSelenium.click();
        return this;
    }

    /**
     * Click on Fork Me On Github Link.
     *
     * @return the DragAndDrop class instance.
     */
    public DragAndDrop clickForkMeOnGithubLink() {
        forkMeOnGithub.click();
        return this;
    }

    /**
     * Verify that the page loaded completely.
     *
     * @return the DragAndDrop class instance.
     */
    public DragAndDrop verifyPageLoaded() {
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
    public DragAndDrop verifyPageUrl() {
        (new WebDriverWait(driver, timeout)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.getCurrentUrl().contains(pageUrl);
            }
        });
        return this;
    }
}
