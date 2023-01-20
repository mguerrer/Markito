package cl.set.markito.samples.MarkitoWebApp.TheInternet;

/*
All the code that follow is
Copyright (c) 2022, Marcos Guerrero. All Rights Reserved.
Not for reuse without permission.
*/

import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import cl.set.markito.framework.MarkitoWebApp;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class AddRemoveElementsPage extends MarkitoWebApp {
    private int timeout = 15;

    @FindBy(css = "#content div.example button")
    @CacheLookup
    public WebElement addElement;

    @FindBy(css = "button.added-manually")
    @CacheLookup
    public WebElement deleteElement;

    @FindBy(css = "a[href='http://elementalselenium.com/']")
    @CacheLookup
    private WebElement elementalSelenium;

    @FindBy(css = "a[href='https://github.com/tourdedave/the-internet']")
    @CacheLookup
    private WebElement forkMeOnGithub;

    private final String pageLoadedText = "";

    private String pageUrl = "/add_remove_elements/";

    public String getPageUrl() {
        return pageUrl;
    }
    public AddRemoveElementsPage(){

    }

    public AddRemoveElementsPage(WebDriver driver, String hostUrl) {
        this();
        setDriver( driver );
    }

    /**
     * Click on Add Element Button.
     *
     * @return the AddRemoveElementsPage class instance.
     */
    public AddRemoveElementsPage clickAddElementButton() {
        click(addElement);
        return this;
    }

    /**
     * Click on Delete Button.
     *
     * @return the AddRemoveElementsPage class instance.
     */
    public AddRemoveElementsPage clickDeleteButton() {
        click(deleteElement);
        return this;
    }

    /**
     * Click on Elemental Selenium Link.
     *
     * @return the AddRemoveElementsPage class instance.
     */
    public AddRemoveElementsPage clickElementalSeleniumLink() {
        click(elementalSelenium);
        return this;
    }

    /**
     * Click on Fork Me On Github Link.
     *
     * @return the AddRemoveElementsPage class instance.
     */
    public AddRemoveElementsPage clickForkMeOnGithubLink() {
        click(forkMeOnGithub);
        return this;
    }

    /**
     * Submit the form to target page.
     *
     * @return the AddRemoveElementsPage class instance.
     */
    public AddRemoveElementsPage submitPage() {
        clickAddElementButton();
        return this;
    }

    /**
     * Verify that the page loaded completely.
     *
     * @return the AddRemoveElementsPage class instance.
     */
    public AddRemoveElementsPage verifyPageLoaded() {
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
     * @return the AddRemoveElementsPage class instance.
     */
    public AddRemoveElementsPage verifyPageUrl() {
        (new WebDriverWait(getDriver(), timeout)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.getCurrentUrl().contains(pageUrl);
            }
        });
        return this;
    }
}

