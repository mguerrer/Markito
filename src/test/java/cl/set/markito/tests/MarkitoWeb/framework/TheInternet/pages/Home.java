/*
All the code that follow is
Copyright (c) 2022, Marcos Guerrero. All Rights Reserved.
Not for reuse without permission.
*/

package cl.set.markito.tests.MarkitoWeb.framework.TheInternet.pages;
import java.util.Map;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class Home {
    private Map<String, String> data;
    private WebDriver driver;
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

    public Home() {
    }

    public Home(WebDriver driver) {
        this();
        this.driver = driver;
    }

    public Home(WebDriver driver, Map<String, String> data) {
        this(driver);
        this.data = data;
    }

    public Home(WebDriver driver, Map<String, String> data, int timeout) {
        this(driver, data);
        this.timeout = timeout;
    }

    /**
     * Click on Ab Testing Link.
     *
     * @return the Home class instance.
     */
    public Home clickAbTestingLink() {
        abTesting.click();
        return this;
    }

    /**
     * Click on Addremove Elements Link.
     *
     * @return the Home class instance.
     */
    public Home clickAddremoveElementsLink() {
        addremoveElements.click();
        return this;
    }

    /**
     * Click on Basic Auth Link.
     *
     * @return the Home class instance.
     */
    public Home clickBasicAuthLink() {
        basicAuth.click();
        return this;
    }

    /**
     * Click on Broken Images Link.
     *
     * @return the Home class instance.
     */
    public Home clickBrokenImagesLink() {
        brokenImages.click();
        return this;
    }

    /**
     * Click on Challenging Dom Link.
     *
     * @return the Home class instance.
     */
    public Home clickChallengingDomLink() {
        challengingDom.click();
        return this;
    }

    /**
     * Click on Checkboxes Link.
     *
     * @return the Home class instance.
     */
    public Home clickCheckboxesLink() {
        checkboxes.click();
        return this;
    }

    /**
     * Click on Context Menu Link.
     *
     * @return the Home class instance.
     */
    public Home clickContextMenuLink() {
        contextMenu.click();
        return this;
    }

    /**
     * Click on Digest Authentication Link.
     *
     * @return the Home class instance.
     */
    public Home clickDigestAuthenticationLink() {
        digestAuthentication.click();
        return this;
    }

    /**
     * Click on Disappearing Elements Link.
     *
     * @return the Home class instance.
     */
    public Home clickDisappearingElementsLink() {
        disappearingElements.click();
        return this;
    }

    /**
     * Click on Drag And Drop Link.
     *
     * @return the Home class instance.
     */
    public Home clickDragAndDropLink() {
        dragAndDrop.click();
        return this;
    }

    /**
     * Click on Dropdown Link.
     *
     * @return the Home class instance.
     */
    public Home clickDropdownLink() {
        dropdown.click();
        return this;
    }

    /**
     * Click on Dynamic Content Link.
     *
     * @return the Home class instance.
     */
    public Home clickDynamicContentLink() {
        dynamicContent.click();
        return this;
    }

    /**
     * Click on Dynamic Controls Link.
     *
     * @return the Home class instance.
     */
    public Home clickDynamicControlsLink() {
        dynamicControls.click();
        return this;
    }

    /**
     * Click on Dynamic Loading Link.
     *
     * @return the Home class instance.
     */
    public Home clickDynamicLoadingLink() {
        dynamicLoading.click();
        return this;
    }

    /**
     * Click on Elemental Selenium Link.
     *
     * @return the Home class instance.
     */
    public Home clickElementalSeleniumLink() {
        elementalSelenium.click();
        return this;
    }

    /**
     * Click on Entry Ad Link.
     *
     * @return the Home class instance.
     */
    public Home clickEntryAdLink() {
        entryAd.click();
        return this;
    }

    /**
     * Click on Exit Intent Link.
     *
     * @return the Home class instance.
     */
    public Home clickExitIntentLink() {
        exitIntent.click();
        return this;
    }

    /**
     * Click on File Download Link.
     *
     * @return the Home class instance.
     */
    public Home clickFileDownloadLink() {
        fileDownload.click();
        return this;
    }

    /**
     * Click on File Upload Link.
     *
     * @return the Home class instance.
     */
    public Home clickFileUploadLink() {
        fileUpload.click();
        return this;
    }

    /**
     * Click on Floating Menu Link.
     *
     * @return the Home class instance.
     */
    public Home clickFloatingMenuLink() {
        floatingMenu.click();
        return this;
    }

    /**
     * Click on Forgot Password Link.
     *
     * @return the Home class instance.
     */
    public Home clickForgotPasswordLink() {
        forgotPassword.click();
        return this;
    }

    /**
     * Click on Fork Me On Github Link.
     *
     * @return the Home class instance.
     */
    public Home clickForkMeOnGithubLink() {
        forkMeOnGithub.click();
        return this;
    }

    /**
     * Click on Form Authentication Link.
     *
     * @return the Home class instance.
     */
    public Home clickFormAuthenticationLink() {
        formAuthentication.click();
        return this;
    }

    /**
     * Click on Frames Link.
     *
     * @return the Home class instance.
     */
    public Home clickFramesLink() {
        frames.click();
        return this;
    }

    /**
     * Click on Geolocation Link.
     *
     * @return the Home class instance.
     */
    public Home clickGeolocationLink() {
        geolocation.click();
        return this;
    }

    /**
     * Click on Horizontal Slider Link.
     *
     * @return the Home class instance.
     */
    public Home clickHorizontalSliderLink() {
        horizontalSlider.click();
        return this;
    }

    /**
     * Click on Hovers Link.
     *
     * @return the Home class instance.
     */
    public Home clickHoversLink() {
        hovers.click();
        return this;
    }

    /**
     * Click on Infinite Scroll Link.
     *
     * @return the Home class instance.
     */
    public Home clickInfiniteScrollLink() {
        infiniteScroll.click();
        return this;
    }

    /**
     * Click on Inputs Link.
     *
     * @return the Home class instance.
     */
    public Home clickInputsLink() {
        inputs.click();
        return this;
    }

    /**
     * Click on Javascript Alerts Link.
     *
     * @return the Home class instance.
     */
    public Home clickJavascriptAlertsLink() {
        javascriptAlerts.click();
        return this;
    }

    /**
     * Click on Javascript Onload Event Error Link.
     *
     * @return the Home class instance.
     */
    public Home clickJavascriptOnloadEventErrorLink() {
        javascriptOnloadEventError.click();
        return this;
    }

    /**
     * Click on Jquery Ui Menus Link.
     *
     * @return the Home class instance.
     */
    public Home clickJqueryUiMenusLink() {
        jqueryUiMenus.click();
        return this;
    }

    /**
     * Click on Key Presses Link.
     *
     * @return the Home class instance.
     */
    public Home clickKeyPressesLink() {
        keyPresses.click();
        return this;
    }

    /**
     * Click on Large Deep Dom Link.
     *
     * @return the Home class instance.
     */
    public Home clickLargeDeepDomLink() {
        largeDeepDom.click();
        return this;
    }

    /**
     * Click on Multiple Windows Link.
     *
     * @return the Home class instance.
     */
    public Home clickMultipleWindowsLink() {
        multipleWindows.click();
        return this;
    }

    /**
     * Click on Nested Frames Link.
     *
     * @return the Home class instance.
     */
    public Home clickNestedFramesLink() {
        nestedFrames.click();
        return this;
    }

    /**
     * Click on Notification Messages Link.
     *
     * @return the Home class instance.
     */
    public Home clickNotificationMessagesLink() {
        notificationMessages.click();
        return this;
    }

    /**
     * Click on Redirect Link Link.
     *
     * @return the Home class instance.
     */
    public Home clickRedirectLinkLink() {
        redirectLink.click();
        return this;
    }

    /**
     * Click on Secure File Download Link.
     *
     * @return the Home class instance.
     */
    public Home clickSecureFileDownloadLink() {
        secureFileDownload.click();
        return this;
    }

    /**
     * Click on Shadow Dom Link.
     *
     * @return the Home class instance.
     */
    public Home clickShadowDomLink() {
        shadowDom.click();
        return this;
    }

    /**
     * Click on Shifting Content Link.
     *
     * @return the Home class instance.
     */
    public Home clickShiftingContentLink() {
        shiftingContent.click();
        return this;
    }

    /**
     * Click on Slow Resources Link.
     *
     * @return the Home class instance.
     */
    public Home clickSlowResourcesLink() {
        slowResources.click();
        return this;
    }

    /**
     * Click on Sortable Data Tables Link.
     *
     * @return the Home class instance.
     */
    public Home clickSortableDataTablesLink() {
        sortableDataTables.click();
        return this;
    }

    /**
     * Click on Status Codes Link.
     *
     * @return the Home class instance.
     */
    public Home clickStatusCodesLink() {
        statusCodes.click();
        return this;
    }

    /**
     * Click on Typos Link.
     *
     * @return the Home class instance.
     */
    public Home clickTyposLink() {
        typos.click();
        return this;
    }

    /**
     * Click on Wysiwyg Editor Link.
     *
     * @return the Home class instance.
     */
    public Home clickWysiwygEditorLink() {
        wysiwygEditor.click();
        return this;
    }

    /**
     * Verify that the page loaded completely.
     *
     * @return the Home class instance.
     */
    public Home verifyPageLoaded() {
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
     * @return the Home class instance.
     */
    public Home verifyPageUrl() {
        (new WebDriverWait(driver, timeout)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.getCurrentUrl().contains(pageUrl);
            }
        });
        return this;
    }
}
