package cl.set.markito.samples;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import cl.set.markito.BrowserStack;
import cl.set.markito.MarkitoiOS;
import io.appium.java_client.MobileBy;
import io.appium.java_client.ios.IOSElement;

/**
 * This test is an example of testing Wikipedia App running on Browserstack.
 * NOTE: In order to run this test you have to set your BrowserStack credentials by setting their values on environment variables named BSUSERNAME and BSPASSWORD.
 */
public class iOSBrowserStackSample2 extends MarkitoiOS {

	@Test
	public void SampleAndroid() throws MalformedURLException, InterruptedException {
		
    	DesiredCapabilities caps = new DesiredCapabilities();
    	
		setDesiredCapabilities(caps);
       
    	OpeniOSDriver( new URL("https://hub-cloud.browserstack.com/wd/hub"), caps );
		Click( MobileBy.AccessibilityId("Search Wikipedia"));
		SendKeys( MobileBy.id("org.wikipedia.alpha:id/search_src_text"), "BrowserStack");
        List<IOSElement> allProductsName = FindElements( MobileBy.className("android.widget.TextView"));
		assert(allProductsName.size() > 0);
		for (WebElement webElement : allProductsName) {
			printf("[%s]\n", webElement.getText());
		}
        
		CloseiOSDriver();
	}

	private void setDesiredCapabilities(DesiredCapabilities caps) {
		BrowserStack bs = new BrowserStack();
		// Set your access credentials
		caps.setCapability("browserstack.user", bs.getBsUsername());
		caps.setCapability("browserstack.key", bs.getBsPassword());
		caps.setCapability("browserstack.appium_version", "1.17.0");
		
		// Set URL of the application under test
		//caps.setCapability("app", "bs://c700ce60cf13ae8ed97705a55b8e022f13c5827c");
    	
   		// Specify device and os_version for testing
		   caps.setCapability("device", "iPhone 11 Pro");
		   caps.setCapability("os_version", "15");
        
    	// Set other BrowserStack capabilities
    	caps.setCapability("project", "Project name");
    	caps.setCapability("build", "Build name");
    	caps.setCapability("name", "Prueba iPhone");
	}

}