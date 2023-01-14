package cl.set.markito.samples.MarkitoMobileApp;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import cl.set.markito.MarkitoAndroid;
import cl.set.markito.framework.cloud.BrowserStack;
import io.appium.java_client.MobileBy;

/**
 * This test is an example of testing Wikipedia App running on Browserstack.
 * NOTE: In order to run this test you have to set your BrowserStack credentials by setting their values on environment variables named BSUSERNAME and BSPASSWORD.
 */
public class AndroidTestWikipediaInBrowserStack extends MarkitoAndroid {

	@Test
	public void TestWikipediaInBrowserStack() throws MalformedURLException, InterruptedException {
		
    	DesiredCapabilities caps = new DesiredCapabilities();
    	
		setDesiredCapabilities(caps);
       
    	OpenAndroidDriver( new URL("http://hub.browserstack.com/wd/hub"), caps );
		click( MobileBy.AccessibilityId("Search Wikipedia"));
		sendKeys( MobileBy.id("org.wikipedia.alpha:id/search_src_text"), "BrowserStack");
        List<WebElement> allProductsName = findElements( MobileBy.className("android.widget.TextView"));
		assert(allProductsName.size() > 0);
		for (WebElement webElement : allProductsName) {
			printf("[%s]\n", webElement.getText());
		}
        
		CloseAndroidDriver();
	}

	private void setDesiredCapabilities(DesiredCapabilities caps) {
		BrowserStack bs = new BrowserStack();
		// Set your access credentials
		caps.setCapability("browserstack.user", bs.getBsUsername());
		caps.setCapability("browserstack.key", bs.getBsPassword());
		caps.setCapability("browserstack.appium_version", "1.17.0");
		
		// Set URL of the application under test
		caps.setCapability("app", "bs://656f787d6d3ed56439877c14724cd626f5f6bbff");
    	
    	// Specify device and os_version for testing
    	caps.setCapability("device", "Google Pixel 3");
    	caps.setCapability("os_version", "9.0");
        
    	// Set other BrowserStack capabilities
    	caps.setCapability("project", "Project name");
    	caps.setCapability("build", "Build name");
    	caps.setCapability("name", "Prueba Google Pixel 3");
	}

}