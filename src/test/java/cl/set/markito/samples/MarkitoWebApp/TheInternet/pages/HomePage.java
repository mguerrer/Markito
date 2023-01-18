/*
All the code that follow is
Copyright (c) 2022, Marcos Guerrero. All Rights Reserved.
Not for reuse without permission.
*/

package cl.set.markito.samples.MarkitoWebApp.TheInternet.pages;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import cl.set.markito.framework.MarkitoWebApp;

public class HomePage extends MarkitoWebApp {

    @BeforeAll
    static void beforeClass() {
        
    }

    @BeforeEach
    void setUp() {
        
    }

    @AfterEach
    void tearDown() {
        
    }

    @AfterAll
    static void afterClass() {
        
    }

    private int timeout = 15;

    @FindBy(css = "a[href='/abtest']")
    @CacheLookup
    private WebElement abTesting;

    @FindBy(css = "a[href='/add_remove_elements/']")
    @CacheLookup
    private WebElement addremoveElements;

    @FindBy(css = "a[href='/basic_auth']")
    @CacheLookup
    private WebElement basicAuth;

    @FindBy(css = "a[href='/broken_images']")
    @CacheLookup
    private WebElement brokenImages;

    @FindBy(css = "a[href='/challenging_dom']")
    @CacheLookup
    private WebElement challengingDom;

    @FindBy(css = "a[href='/checkboxes']")
    @CacheLookup
    private WebElement checkboxes;

    @FindBy(css = "a[href='/context_menu']")
    @CacheLookup
    private WebElement contextMenu;

    @FindBy(css = "a[href='/digest_auth']")
    @CacheLookup
    private WebElement digestAuthentication;

    @FindBy(css = "a[href='/disappearing_elements']")
    @CacheLookup
    private WebElement disappearingElements;

    @FindBy(css = "a[href='/drag_and_drop']")
    @CacheLookup
    private WebElement dragAndDrop;

    @FindBy(css = "a[href='/dropdown']")
    @CacheLookup
    private WebElement dropdown;

    @FindBy(css = "a[href='/dynamic_content']")
    @CacheLookup
    private WebElement dynamicContent;

    @FindBy(css = "a[href='/dynamic_controls']")
    @CacheLookup
    private WebElement dynamicControls;

    @FindBy(css = "a[href='/dynamic_loading']")
    @CacheLookup
    private WebElement dynamicLoading;

    @FindBy(css = "a[href='http://elementalselenium.com/']")
    @CacheLookup
    private WebElement elementalSelenium;

    @FindBy(css = "a[href='/entry_ad']")
    @CacheLookup
    private WebElement entryAd;

    @FindBy(css = "a[href='/exit_intent']")
    @CacheLookup
    private WebElement exitIntent;

    @FindBy(css = "a[href='/download']")
    @CacheLookup
    private WebElement fileDownload;

    @FindBy(css = "a[href='/upload']")
    @CacheLookup
    private WebElement fileUpload;

    @FindBy(css = "a[href='/floating_menu']")
    @CacheLookup
    private WebElement floatingMenu;

    @FindBy(css = "a[href='/forgot_password']")
    @CacheLookup
    private WebElement forgotPassword;

    @FindBy(css = "a[href='https://github.com/tourdedave/the-internet']")
    @CacheLookup
    private WebElement forkMeOnGithub;

    @FindBy(css = "a[href='/login']")
    @CacheLookup
    private WebElement formAuthentication;

    @FindBy(css = "a[href='/frames']")
    @CacheLookup
    private WebElement frames;

    @FindBy(css = "a[href='/geolocation']")
    @CacheLookup
    private WebElement geolocation;

    @FindBy(css = "a[href='/horizontal_slider']")
    @CacheLookup
    private WebElement horizontalSlider;

    @FindBy(css = "a[href='/hovers']")
    @CacheLookup
    private WebElement hovers;

    @FindBy(css = "a[href='/infinite_scroll']")
    @CacheLookup
    private WebElement infiniteScroll;

    @FindBy(css = "a[href='/inputs']")
    @CacheLookup
    private WebElement inputs;

    @FindBy(css = "a[href='/javascript_alerts']")
    @CacheLookup
    private WebElement javascriptAlerts;

    @FindBy(css = "a[href='/javascript_error']")
    @CacheLookup
    private WebElement javascriptOnloadEventError;

    @FindBy(css = "a[href='/jqueryui/menu']")
    @CacheLookup
    private WebElement jqueryUiMenus;

    @FindBy(css = "a[href='/key_presses']")
    @CacheLookup
    private WebElement keyPresses;

    @FindBy(css = "a[href='/large']")
    @CacheLookup
    private WebElement largeDeepDom;

    @FindBy(css = "a[href='/windows']")
    @CacheLookup
    private WebElement multipleWindows;

    @FindBy(css = "a[href='/nested_frames']")
    @CacheLookup
    private WebElement nestedFrames;

    @FindBy(css = "a[href='/notification_message']")
    @CacheLookup
    private WebElement notificationMessages;

    private final String pageLoadedText = "";

    private final String pageUrl = "/";

    @FindBy(css = "a[href='/redirector']")
    @CacheLookup
    private WebElement redirectLink;

    @FindBy(css = "a[href='/download_secure']")
    @CacheLookup
    private WebElement secureFileDownload;

    @FindBy(css = "a[href='/shadowdom']")
    @CacheLookup
    private WebElement shadowDom;

    @FindBy(css = "a[href='/shifting_content']")
    @CacheLookup
    private WebElement shiftingContent;

    @FindBy(css = "a[href='/slow']")
    @CacheLookup
    private WebElement slowResources;

    @FindBy(css = "a[href='/tables']")
    @CacheLookup
    private WebElement sortableDataTables;

    @FindBy(css = "a[href='/status_codes']")
    @CacheLookup
    private WebElement statusCodes;

    @FindBy(css = "a[href='/typos']")
    @CacheLookup
    private WebElement typos;

    @FindBy(css = "a[href='/tinymce']")
    @CacheLookup
    private WebElement wysiwygEditor;

    private String hostUrl;

    public String getHostUrl(String hostUrl){
        this.hostUrl = hostUrl;
        return this.hostUrl;
    }

    public HomePage() {
    }

    public HomePage(WebDriver driver, String hostUrl) {
        this();
        setDriver( driver );
        this.hostUrl = hostUrl;
        driver.get(hostUrl);
    }

   /**
     * Click on Ab Testing Link.
     *
     * @return the Home class instance.
     */
    public HomePage clickAbTestingLink() {
        click(abTesting);
        return this;
    }

    /**
     * Click on Addremove Elements Link.
     *
     * @return the Home class instance.
     */
    public HomePage clickAddremoveElementsLink() {
        click(addremoveElements);
        return this;
    }

    /**
     * Click on Basic Auth Link.
     *
     * @return the Home class instance.
     */
    public HomePage clickBasicAuthLink() {
        click(basicAuth);
        return this;
    }

    /**
     * Click on Broken Images Link.
     *
     * @return the Home class instance.
     */
    public HomePage clickBrokenImagesLink() {
        click(brokenImages);
        return this;
    }

    /**
     * Click on Challenging Dom Link.
     *
     * @return the Home class instance.
     */
    public HomePage clickChallengingDomLink() {
        click(challengingDom);
        return this;
    }

    /**
     * Click on Checkboxes Link.
     *
     * @return the Home class instance.
     */
    public HomePage clickCheckboxesLink() {
        click(checkboxes);
        return this;
    }

    /**
     * Click on Context Menu Link.
     *
     * @return the Home class instance.
     */
    public HomePage clickContextMenuLink() {
        click(contextMenu);
        return this;
    }

    /**
     * Click on Digest Authentication Link.
     *
     * @return the Home class instance.
     */
    public HomePage clickDigestAuthenticationLink() {
        click(digestAuthentication);
        return this;
    }

    /**
     * Click on Disappearing Elements Link.
     *
     * @return the Home class instance.
     */
    public HomePage clickDisappearingElementsLink() {
        click(disappearingElements);
        return this;
    }

    /**
     * Click on Drag And Drop Link.
     *
     * @return the Home class instance.
     */
    public HomePage clickDragAndDropLink() {
        click(dragAndDrop);
        return this;
    }

    /**
     * Click on Dropdown Link.
     *
     * @return the Home class instance.
     */
    public HomePage clickDropdownLink() {
        click(dropdown);
        return this;
    }

    /**
     * Click on Dynamic Content Link.
     *
     * @return the Home class instance.
     */
    public HomePage clickDynamicContentLink() {
        click(dynamicContent);
        return this;
    }

    /**
     * Click on Dynamic Controls Link.
     *
     * @return the Home class instance.
     */
    public HomePage clickDynamicControlsLink() {
        click(dynamicControls);
        return this;
    }

    /**
     * Click on Dynamic Loading Link.
     *
     * @return the Home class instance.
     */
    public HomePage clickDynamicLoadingLink() {
        click(dynamicLoading);
        return this;
    }

    /**
     * Click on Elemental Selenium Link.
     *
     * @return the Home class instance.
     */
    public HomePage clickElementalSeleniumLink() {
        click(elementalSelenium);
        return this;
    }

    /**
     * Click on Entry Ad Link.
     *
     * @return the Home class instance.
     */
    public HomePage clickEntryAdLink() {
        click(entryAd);
        return this;
    }

    /**
     * Click on Exit Intent Link.
     *
     * @return the Home class instance.
     */
    public HomePage clickExitIntentLink() {
        click(exitIntent);
        return this;
    }

    /**
     * Click on File Download Link.
     *
     * @return the Home class instance.
     */
    public HomePage clickFileDownloadLink() {
        click(fileDownload);
        return this;
    }

    /**
     * Click on File Upload Link.
     *
     * @return the Home class instance.
     */
    public HomePage clickFileUploadLink() {
        click(fileUpload);
        return this;
    }

    /**
     * Click on Floating Menu Link.
     *
     * @return the Home class instance.
     */
    public HomePage clickFloatingMenuLink() {
        click(floatingMenu);
        return this;
    }

    /**
     * Click on Forgot Password Link.
     *
     * @return the Home class instance.
     */
    public HomePage clickForgotPasswordLink() {
        click(forgotPassword);
        return this;
    }

    /**
     * Click on Fork Me On Github Link.
     *
     * @return the Home class instance.
     */
    public HomePage clickForkMeOnGithubLink() {
        click(forkMeOnGithub);
        return this;
    }

    /**
     * Click on Form Authentication Link.
     *
     * @return the Home class instance.
     */
    public HomePage clickFormAuthenticationLink() {
        click(formAuthentication);
        return this;
    }

    /**
     * Click on Frames Link.
     *
     * @return the Home class instance.
     */
    public HomePage clickFramesLink() {
        click(frames);
        return this;
    }

    /**
     * Click on Geolocation Link.
     *
     * @return the Home class instance.
     */
    public HomePage clickGeolocationLink() {
        click(geolocation);
        return this;
    }

    /**
     * Click on Horizontal Slider Link.
     *
     * @return the Home class instance.
     */
    public HomePage clickHorizontalSliderLink() {
        click(horizontalSlider);
        return this;
    }

    /**
     * Click on Hovers Link.
     *
     * @return the Home class instance.
     */
    public HomePage clickHoversLink() {
        click(hovers);
        return this;
    }

    /**
     * Click on Infinite Scroll Link.
     *
     * @return the Home class instance.
     */
    public HomePage clickInfiniteScrollLink() {
        click(infiniteScroll);
        return this;
    }

    /**
     * Click on Inputs Link.
     *
     * @return the Home class instance.
     */
    public HomePage clickInputsLink() {
        click(inputs);
        return this;
    }

    /**
     * Click on Javascript Alerts Link.
     *
     * @return the Home class instance.
     */
    public HomePage clickJavascriptAlertsLink() {
        click(javascriptAlerts);
        return this;
    }

    /**
     * Click on Javascript Onload Event Error Link.
     *
     * @return the Home class instance.
     */
    public HomePage clickJavascriptOnloadEventErrorLink() {
        click(javascriptOnloadEventError);
        return this;
    }

    /**
     * Click on Jquery Ui Menus Link.
     *
     * @return the Home class instance.
     */
    public HomePage clickJqueryUiMenusLink() {
        click(jqueryUiMenus);
        return this;
    }

    /**
     * Click on Key Presses Link.
     *
     * @return the Home class instance.
     */
    public HomePage clickKeyPressesLink() {
        click(keyPresses);
        return this;
    }

    /**
     * Click on Large Deep Dom Link.
     *
     * @return the Home class instance.
     */
    public HomePage clickLargeDeepDomLink() {
        click(largeDeepDom);
        return this;
    }

    /**
     * Click on Multiple Windows Link.
     *
     * @return the Home class instance.
     */
    public HomePage clickMultipleWindowsLink() {
        click(multipleWindows);
        return this;
    }

    /**
     * Click on Nested Frames Link.
     *
     * @return the Home class instance.
     */
    public HomePage clickNestedFramesLink() {
        click(nestedFrames);
        return this;
    }

    /**
     * Click on Notification Messages Link.
     *
     * @return the Home class instance.
     */
    public HomePage clickNotificationMessagesLink() {
        click(notificationMessages);
        return this;
    }

    /**
     * Click on Redirect Link Link.
     *
     * @return the Home class instance.
     */
    public HomePage clickRedirectLinkLink() {
        click(redirectLink);
        return this;
    }

    /**
     * Click on Secure File Download Link.
     *
     * @return the Home class instance.
     */
    public HomePage clickSecureFileDownloadLink() {
        click(secureFileDownload);
        return this;
    }

    /**
     * Click on Shadow Dom Link.
     *
     * @return the Home class instance.
     */
    public HomePage clickShadowDomLink() {
        click(shadowDom);
        return this;
    }

    /**
     * Click on Shifting Content Link.
     *
     * @return the Home class instance.
     */
    public HomePage clickShiftingContentLink() {
        click(shiftingContent);
        return this;
    }

    /**
     * Click on Slow Resources Link.
     *
     * @return the Home class instance.
     */
    public HomePage clickSlowResourcesLink() {
        click(slowResources);
        return this;
    }

    /**
     * Click on Sortable Data Tables Link.
     *
     * @return the Home class instance.
     */
    public HomePage clickSortableDataTablesLink() {
        click(sortableDataTables);
        return this;
    }

    /**
     * Click on Status Codes Link.
     *
     * @return the Home class instance.
     */
    public HomePage clickStatusCodesLink() {
        click(statusCodes);
        return this;
    }

    /**
     * Click on Typos Link.
     *
     * @return the Home class instance.
     */
    public HomePage clickTyposLink() {
        click(typos);
        return this;
    }

    /**
     * Click on Wysiwyg Editor Link.
     *
     * @return the Home class instance.
     */
    public HomePage clickWysiwygEditorLink() {
        click(wysiwygEditor);
        return this;
    }

    /**
     * Verify that the page loaded completely.
     *
     * @return the Home class instance.
     */
    public HomePage verifyPageLoaded() {
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
     * @return the Home class instance.
     */
    public HomePage verifyPageUrl() {
        (new WebDriverWait(getDriver(), timeout)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.getCurrentUrl().contains(pageUrl);
            }
        });
        return this;
    }
}
