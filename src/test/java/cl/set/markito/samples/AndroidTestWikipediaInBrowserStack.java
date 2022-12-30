package cl.set.markito.samples;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import cl.set.markito.MarkitoAndroid;
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
		Click( MobileBy.AccessibilityId("Search Wikipedia"));
		SendKeys( MobileBy.id("org.wikipedia.alpha:id/search_src_text"), "BrowserStack");
        List<WebElement> allProductsName = FindElements( MobileBy.className("android.widget.TextView"));
		assert(allProductsName.size() > 0);
		for (WebElement webElement : allProductsName) {
			printf("[%s]\n", webElement.getText());
		}
        
		CloseAndroidDriver();
	}

	private void setDesiredCapabilities(DesiredCapabilities caps) {
		// Set your access credentials
		caps.setCapability("browserstack.user", getBsUsername());
		caps.setCapability("browserstack.key", getBsPassword());
		caps.setCapability("browserstack.appium_version", "1.17.0");
		
		// Set URL of the application under test
		caps.setCapability("app", "bs://c700ce60cf13ae8ed97705a55b8e022f13c5827c");
    	
    	// Specify device and os_version for testing
    	caps.setCapability("device", "Google Pixel 3");
    	caps.setCapability("os_version", "9.0");
        
    	// Set other BrowserStack capabilities
    	caps.setCapability("project", "Project name");
    	caps.setCapability("build", "Build name");
    	caps.setCapability("name", "Prueba Google Pixel 3");
	}

}