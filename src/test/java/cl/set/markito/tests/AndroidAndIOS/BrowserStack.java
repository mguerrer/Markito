package cl.set.markito.tests.AndroidAndIOS;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.remote.DesiredCapabilities;

import cl.set.markito.MarkitoBaseUtils;
import io.appium.java_client.MobileDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;

public class BrowserStack extends MarkitoBaseUtils {
    DesiredCapabilities caps = new DesiredCapabilities();
    MobileDriver<MobileElement> driver;
    	
    public MobileDriver<MobileElement> openBrowserStackMobileSession(String platform) throws MalformedURLException {
        URL UrlAppiumServer = new URL("https://hub-cloud.browserstack.com/wd/hub");
        //String platform = caps.getCapability("platformName").toString();
        printf(ANSI_WHITE+"Opening %s Session in appium URL %s in device %s...", platform, UrlAppiumServer, caps.getCapability("device"));

        switch ( platform ) {
            case "android":
                try {
                    driver = new AndroidDriver<MobileElement>( UrlAppiumServer, caps);
                    if ( this.driver == null )
                    {
                        println(ANSI_RED+"\nERROR on getting Android session, value is null.");
                        System.exit(-1);                
                    }
                } catch (Exception e) {
                    printf(ANSI_RED+"\nERROR on getting Android session. \nStack: %s", e.getMessage());
                    System.exit(-1);
                }        
                break;
            case "iOS":
                try {
                    driver = new IOSDriver<MobileElement>( UrlAppiumServer, caps);
                    if ( this.driver == null )
                    {
                        println(ANSI_RED+"\nERROR on getting iOS session, value is null.");
                        System.exit(-1);                
                    }
                } catch (Exception e) {
                    printf(ANSI_RED+"\nERROR on getting iOS session. \nStack: %s", e.getMessage());
                    System.exit(-1);
                }        
                break;
        }
        return driver;
    }
    public void setDesiredTechnicalCapabilities( String bsAppUrl, String deviceName, String platform, String os_version ) {
		// Set your access credentials
		SetBsCredentials(getBsUsername(), getBsPassword());

		// Set URL of the application under test
		SetAppUnderTesting(bsAppUrl);
    	
    	// Specify device and os_version for testing
    	SetDeviceAndOsVersion(deviceName, platform, os_version);
        
	}
    public void SetProjectInformation( String projectName, String buildName, String testName) {
        // Set other BrowserStack capabilities
        this.caps.setCapability("browserstack.appium_version", "1.17.0");
    	this.caps.setCapability("project", projectName);
    	this.caps.setCapability("build", buildName);
    	this.caps.setCapability("name", testName);
    }
    private void SetDeviceAndOsVersion(String device, String platform, String os_version) {
        this.caps.setCapability("device", device);
    	this.caps.setCapability("os_version", os_version);
        //this.caps.setCapability("platformName", platform );
    }
    private void SetAppUnderTesting( String appUrlInBs ) {
        this.caps.setCapability("app", appUrlInBs);
    }
    private void SetBsCredentials(String username, String key ) {
        this.caps.setCapability("browserstack.user", username);
		this.caps.setCapability("browserstack.key", key);
    }
}
