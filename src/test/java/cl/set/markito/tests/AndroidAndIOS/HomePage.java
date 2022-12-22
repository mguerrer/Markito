package cl.set.markito.tests.AndroidAndIOS;

import org.openqa.selenium.support.PageFactory;

import io.appium.java_client.MobileDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;

/**
 * POM class for demo BrowserStack app.
 */
public class HomePage {
    MobileDriver<MobileElement> driver;
    
    @AndroidFindBy(id = "com.example.android.basicnetworking:id/test_action" ) // XPATH=//*/android.widget.Button[@text='Test Browserstack Local Connection']
    @iOSXCUITFindBy(accessibility = "TestBrowserStackLocal") // @iOSXCUITFindBy( xpath= //XCUIElementTypeButton[@name="TestBrowserStackLocal"] )
    MobileElement textButton;

    @AndroidFindBy (id = "com.example.android.basicnetworking:id/textView") // XPATH=//*/android.widget.TextView[@text='Unable to connect. Local connection failed']
    @iOSXCUITFindBy(accessibility = "ResultBrowserStackLocal") // @iOSXCUITFindBy( xpath=//XCUIElementTypeTextField[@name="ResultBrowserStackLocal"] )
    MobileElement textMsgBox;
    
	public HomePage(MobileDriver<MobileElement> driver) {
        this.driver = driver;
           PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    public void clickTestBrowserStackLocalButton(){
        textButton.click();
    }

    public String getTestResultMessage(){
        if (textMsgBox.isDisplayed()) {
            return textMsgBox.getText().toString();
        } else {
            return null;
        }
    }
}
