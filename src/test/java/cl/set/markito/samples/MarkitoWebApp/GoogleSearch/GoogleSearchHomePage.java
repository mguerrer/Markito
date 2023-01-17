package cl.set.markito.samples.MarkitoWebApp.GoogleSearch;



/*
All the code that follow is
Copyright (c) 2022, Marcos Guerrero. All Rights Reserved.
Not for reuse without permission.
*/


import java.util.Map;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class GoogleSearchHomePage {
    private Map<String, String> data;
    private WebDriver driver;
    private int timeout = 15;

    @FindBy(css = "a[href='https://about.google/?utm_source=google-CL&utm_medium=referral&utm_campaign=hp-footer&fg=1']")
    @CacheLookup
    private WebElement about;

    @FindBy(css = "a[href='/advanced_search?hl=en-CL&fg=1']")
    @CacheLookup
    private WebElement advancedSearch;

    @FindBy(css = "a[href='https://www.google.cl/intl/en_cl/ads/?subid=ww-ww-et-g-awa-a-g_hpafoot1_1!o2&utm_source=google.com&utm_medium=referral&utm_campaign=google_hpafooter&fg=1']")
    @CacheLookup
    private WebElement advertising;

    @FindBy(css = "a[href='https://www.google.cl/services/?subid=ww-ww-et-g-awa-a-g_hpbfoot1_1!o2&utm_source=google.com&utm_medium=referral&utm_campaign=google_hpbfooter&fg=1']")
    @CacheLookup
    private WebElement business;

    @FindBy(css = "a.gb_e.gb_Xa.gb_o")
    @CacheLookup
    private WebElement comogw;

    @FindBy(css = "a[href='https://www.google.cl/setprefs?sig=0_eI9SeRxx60ofvSRS5ShrHBCH500%3D&hl=es-419&source=homepage&sa=X&ved=0ahUKEwj6zL7t-sf8AhUUqJUCHYwpAZMQ2ZgBCBM']")
    @CacheLookup
    private WebElement espaolLatinoamrica;

    @FindBy(css = "a[href='https://mail.google.com/mail/?authuser=0&ogbl']")
    @CacheLookup
    private WebElement gmail;

    @FindBy(name = "csi")
    @CacheLookup
    private WebElement gmailimagesGoogleOfferedInEspaolLatinoamrica;

    @FindBy(name = "q")
    @CacheLookup
    public WebElement queryTextBox;

    @FindBy(name = "btnK")
    @CacheLookup
    public static WebElement googleSearch1;

    @FindBy(name = "btnK")
    @CacheLookup
    private WebElement googleSearch2;

    @FindBy(css = "a[href='https://google.com/search/howsearchworks/?fg=1']")
    @CacheLookup
    private WebElement howSearchWorks;

    @FindBy(name = "btnI")
    @CacheLookup
    private WebElement imFeelingLucky1;

    @FindBy(name = "btnI")
    @CacheLookup
    private WebElement imFeelingLucky2;

    @FindBy(css = "a[href='https://www.google.cl/imghp?hl=en&authuser=0&ogbl']")
    @CacheLookup
    private WebElement images;

    private final String pageLoadedText = "";

    private final String pageUrl = "/";

    @FindBy(css = "a[href='https://policies.google.com/privacy?hl=en-CL&fg=1']")
    @CacheLookup
    private WebElement privacy;

    @FindBy(id = "sbfblt")
    @CacheLookup
    private WebElement reportInappropriatePredictions;

    @FindBy(css = "a[href='https://support.google.com/websearch/?p=ws_results_help&hl=en-CL&fg=1']")
    @CacheLookup
    private WebElement searchHelp;

    @FindBy(css = "a[href='https://myactivity.google.com/product/search?utm_source=google&hl=en-CL&fg=1']")
    @CacheLookup
    private WebElement searchHistory;

    @FindBy(css = "a[href='https://www.google.cl/preferences?hl=en-CL&fg=1']")
    @CacheLookup
    private WebElement searchSettings;

    @FindBy(css = "a[href='https://policies.google.com/terms?hl=en-CL&fg=1']")
    @CacheLookup
    private WebElement terms;

    @FindBy(css = "a[href='https://myactivity.google.com/privacyadvisor/search?utm_source=googlemenu&fg=1']")
    @CacheLookup
    private WebElement yourDataInSearch;

    public GoogleSearchHomePage() {
    }

    public GoogleSearchHomePage(WebDriver driver) {
        this();
        this.driver = driver;
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    public GoogleSearchHomePage(WebDriver driver, Map<String, String> data) {
        this(driver);
        this.data = data;
    }

    public GoogleSearchHomePage(WebDriver driver, Map<String, String> data, int timeout) {
        this(driver, data);
        this.timeout = timeout;
    }

    /**
     * Click on About Link.
     *
     * @return the GoogleSearch class instance.
     */
    public GoogleSearchHomePage clickAboutLink() {
        about.click();
        return this;
    }

    /**
     * Click on Advanced Search Link.
     *
     * @return the GoogleSearch class instance.
     */
    public GoogleSearchHomePage clickAdvancedSearchLink() {
        advancedSearch.click();
        return this;
    }

    /**
     * Click on Advertising Link.
     *
     * @return the GoogleSearch class instance.
     */
    public GoogleSearchHomePage clickAdvertisingLink() {
        advertising.click();
        return this;
    }

    /**
     * Click on Business Link.
     *
     * @return the GoogleSearch class instance.
     */
    public GoogleSearchHomePage clickBusinessLink() {
        business.click();
        return this;
    }

    /**
     * Click on .Comogw Link.
     *
     * @return the GoogleSearch class instance.
     */
    public GoogleSearchHomePage clickComogwLink() {
        comogw.click();
        return this;
    }

    /**
     * Click on Espaol Latinoamrica Link.
     *
     * @return the GoogleSearch class instance.
     */
    public GoogleSearchHomePage clickEspaolLatinoamricaLink() {
        espaolLatinoamrica.click();
        return this;
    }

    /**
     * Click on Gmail Link.
     *
     * @return the GoogleSearch class instance.
     */
    public GoogleSearchHomePage clickGmailLink() {
        gmail.click();
        return this;
    }

    /**
     * Click on Google Search Button.
     *
     * @return the GoogleSearch class instance.
     */
    public GoogleSearchHomePage clickGoogleSearch1Button() {
        googleSearch1.click();
        return this;
    }

    /**
     * Click on Google Search Button.
     *
     * @return the GoogleSearch class instance.
     */
    public GoogleSearchHomePage clickGoogleSearch2Button() {
        googleSearch2.click();
        return this;
    }

    /**
     * Click on How Search Works Link.
     *
     * @return the GoogleSearch class instance.
     */
    public GoogleSearchHomePage clickHowSearchWorksLink() {
        howSearchWorks.click();
        return this;
    }

    /**
     * Click on Im Feeling Lucky Button.
     *
     * @return the GoogleSearch class instance.
     */
    public GoogleSearchHomePage clickImFeelingLucky1Button() {
        imFeelingLucky1.click();
        return this;
    }

    /**
     * Click on Im Feeling Lucky Button.
     *
     * @return the GoogleSearch class instance.
     */
    public GoogleSearchHomePage clickImFeelingLucky2Button() {
        imFeelingLucky2.click();
        return this;
    }

    /**
     * Click on Images Link.
     *
     * @return the GoogleSearch class instance.
     */
    public GoogleSearchHomePage clickImagesLink() {
        images.click();
        return this;
    }

    /**
     * Click on Privacy Link.
     *
     * @return the GoogleSearch class instance.
     */
    public GoogleSearchHomePage clickPrivacyLink() {
        privacy.click();
        return this;
    }

    /**
     * Click on Report Inappropriate Predictions Link.
     *
     * @return the GoogleSearch class instance.
     */
    public GoogleSearchHomePage clickReportInappropriatePredictionsLink() {
        reportInappropriatePredictions.click();
        return this;
    }

    /**
     * Click on Search Help Link.
     *
     * @return the GoogleSearch class instance.
     */
    public GoogleSearchHomePage clickSearchHelpLink() {
        searchHelp.click();
        return this;
    }

    /**
     * Click on Search History Link.
     *
     * @return the GoogleSearch class instance.
     */
    public GoogleSearchHomePage clickSearchHistoryLink() {
        searchHistory.click();
        return this;
    }

    /**
     * Click on Search Settings Link.
     *
     * @return the GoogleSearch class instance.
     */
    public GoogleSearchHomePage clickSearchSettingsLink() {
        searchSettings.click();
        return this;
    }

    /**
     * Click on Terms Link.
     *
     * @return the GoogleSearch class instance.
     */
    public GoogleSearchHomePage clickTermsLink() {
        terms.click();
        return this;
    }

    /**
     * Click on Your Data In Search Link.
     *
     * @return the GoogleSearch class instance.
     */
    public GoogleSearchHomePage clickYourDataInSearchLink() {
        yourDataInSearch.click();
        return this;
    }


    /**
     * Set default value to Gmailimages Google Offered In Espaol Latinoamrica Chileaboutadvertisingbusiness How Search Works Privacytermssettings Textarea field.
     *
     * @return the GoogleSearch class instance.
     */
    public GoogleSearchHomePage setGmailimagesGoogleOfferedInEspaolLatinoamricaTextareaField() {
        return setGmailimagesGoogleOfferedInEspaolLatinoamricaTextareaField(data.get("GMAILIMAGES_GOOGLE_OFFERED_IN_ESPAOL_LATINOAMRICA"));
    }

    /**
     * Set value to Gmailimages Google Offered In Espaol Latinoamrica Chileaboutadvertisingbusiness How Search Works Privacytermssettings Textarea field.
     *
     * @return the GoogleSearch class instance.
     */
    public GoogleSearchHomePage setGmailimagesGoogleOfferedInEspaolLatinoamricaTextareaField(String gmailimagesGoogleOfferedInEspaolLatinoamricaValue) {
        gmailimagesGoogleOfferedInEspaolLatinoamrica.sendKeys(gmailimagesGoogleOfferedInEspaolLatinoamricaValue);
        return this;
    }

    /**
     * Submit the form to target page.
     *
     * @return the GoogleSearch class instance.
     */
    public GoogleSearchHomePage submit() {
        clickImFeelingLucky1Button();
        return this;
    }

    /**
     * Verify that the page loaded completely.
     *
     * @return the GoogleSearch class instance.
     */
    public GoogleSearchHomePage verifyPageLoaded() {
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
     * @return the GoogleSearch class instance.
     */
    public GoogleSearchHomePage verifyPageUrl() {
        (new WebDriverWait(driver, timeout)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.getCurrentUrl().contains(pageUrl);
            }
        });
        return this;
    }
}

