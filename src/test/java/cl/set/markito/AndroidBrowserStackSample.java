package cl.set.markito;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import io.appium.java_client.MobileBy;


public class AndroidBrowserStackSample extends MarkitoAndroid {

	@Test
	public void SampleAndroid() throws MalformedURLException, InterruptedException {
		
    	DesiredCapabilities caps = new DesiredCapabilities();
    	
		// Set your access credentials
		caps.setCapability("browserstack.user", "marcos150");
		caps.setCapability("browserstack.key", "uGotcDU7y8nn9V8tnJcS");
		caps.setCapability("browserstack.appium_version", "1.17.0");
		
		// Set URL of the application under test
		caps.setCapability("app", "bs://c700ce60cf13ae8ed97705a55b8e022f13c5827c");
    	
    	// Specify device and os_version for testing
    	caps.setCapability("device", "Google Pixel 3");
    	caps.setCapability("os_version", "9.0");
        
    	// Set other BrowserStack capabilities
    	caps.setCapability("project", "Pruebas moviles");
    	caps.setCapability("build", "GasConnect v1.15 Android");
    	caps.setCapability("name", "Prueba Google Pixel 3");
       
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

}