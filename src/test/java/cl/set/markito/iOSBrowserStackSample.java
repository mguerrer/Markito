package cl.set.markito;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.remote.DesiredCapabilities;

import io.appium.java_client.MobileBy;


public class iOSBrowserStackSample extends MarkitoiOS{

	@Disabled("not implemented yet")
	@Test
	public void SampleiOS() throws MalformedURLException, InterruptedException {
		
    	DesiredCapabilities caps = new DesiredCapabilities();
    	
		// Set your access credentials
		caps.setCapability("browserstack.user", "marcos150");
		caps.setCapability("browserstack.key", "uGotcDU7y8nn9V8tnJcS");
		
		// Set URL of the application under test
		caps.setCapability("app", "bs://444bd0308813ae0dc236f8cd461c02d3afa7901d");
    	
   		// Specify device and os_version for testing
		caps.setCapability("device", "iPhone 11 Pro");
		caps.setCapability("os_version", "13");
		
		// Set other BrowserStack capabilities
		caps.setCapability("project", "Pruebas moviles");
		caps.setCapability("build", "GasConnect v1.15 iOS");
		caps.setCapability("name", "Prueba IPhone 11 Pro");
		
		// Initialise the remote Webdriver using BrowserStack remote URL
		// and desired capabilities defined above
		OpeniOSDriver(new URL("http://hub-cloud.browserstack.com/wd/hub"), caps);

		// Test case for the BrowserStack sample iOS app. 
		// If you have uploaded your app, update the test case here. 
		//Click((MobileBy) MobileBy.AccessibilityId("Text Button"));
		String texts[] = { "hello@markito1.cl","hello@markito2.cl","hello@markito3.cl","hello@markito4.cl","hello@markito5.cl",
						   "hello@markito6.cl","hello@markito7.cl","hello@markito8.cl","hello@markito9.cl","hello@markito10.cl"};
		for (String text : texts) {
			//SendKeys((MobileBy) MobileBy.AccessibilityId("Text Input"), text);
			Thread.sleep(5000);
			//String textOutput = GetText((MobileBy) MobileBy.AccessibilityId("Text Input"));
			/*if(textOutput != null && textOutput.equals(text))
				assert(true);
			else
				assert(false);  */
		}

	
		// Invoke driver.quit() after the test is done to indicate that the test is completed.
		CloseiOSDriver();

	}

}
