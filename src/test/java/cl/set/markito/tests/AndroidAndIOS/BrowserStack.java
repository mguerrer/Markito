package cl.set.markito.tests.AndroidAndIOS;

import java.net.URL;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import cl.set.markito.MarkitoBaseUtils;
import io.appium.java_client.MobileDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;

public class BrowserStack extends MarkitoBaseUtils {
    DesiredCapabilities caps = new DesiredCapabilities();
    MobileDriver<MobileElement> mobiledriver;
    WebDriver webdriver;

    	
    public MobileDriver<MobileElement> openBrowserStackMobileSession() throws Exception {
        URL UrlAppiumServer = new URL("https://hub-cloud.browserstack.com/wd/hub");
        String platform = caps.getCapability("platformName").toString();
        String app;
        printf(ANSI_WHITE+"Opening %s Session in Browserstack in device %s...", platform, caps.getCapability("device"));
        if (caps.getCapability("app") != null){
            app = caps.getCapability("app").toString();
            printf(ANSI_WHITE+" on app "+ app );
        }

        try {
            switch ( platform ) {
                case "android":
                    mobiledriver = new AndroidDriver<MobileElement>( UrlAppiumServer, caps);
                    break;
                case "iOS":
                    mobiledriver = new IOSDriver<MobileElement>( UrlAppiumServer, caps);
                    break;
                default:
                    throw new Exception(ANSI_RED+"\nERROR on getting "+platform+" session, unknown platfom.");
            }
            if ( this.mobiledriver == null ) {
                throw new Exception(ANSI_RED+"\nERROR on getting "+platform+" session, value is null.");
            }
        } catch (Exception e) {
            throw new Exception(ANSI_RED+"\nERROR on getting "+platform+" session. \nStack: "+ e.getMessage());
        }
        return mobiledriver;
    }
    public WebDriver openBrowserStackWebSession() throws Exception {
        URL UrlAppiumServer = new URL("https://hub-cloud.browserstack.com/wd/hub");
        String platform = caps.getCapability("platformName").toString();
        String browser;
        printf(ANSI_WHITE+"Opening %s Web Session in Browserstack in device %s...", platform, caps.getCapability("device"));
        if (caps.getCapability("browserName")!= null){
            browser = caps.getCapability("browserName").toString();
            printf(ANSI_WHITE+" on browser "+ browser );
        }
        LogCapabilities( caps );

        try {
            switch ( platform ) {
                case "android":
                    webdriver = new RemoteWebDriver( UrlAppiumServer, caps);
                    break;
                case "iOS":
                    webdriver = new RemoteWebDriver( UrlAppiumServer, caps);
                    break;
                default:
                    throw new Exception(ANSI_RED+"\nERROR on getting "+platform+" session, unknown platfom.");
            }
            if ( this.webdriver == null ) {
                throw new Exception(ANSI_RED+"\nERROR on getting "+platform+" session, value is null.");
            }
        } catch (Exception e) {
            throw new Exception(ANSI_RED+"\nERROR on getting "+platform+" session. \nStack: "+ e.getMessage());
        }
        return webdriver;
    }

    public void setDesiredTechnicalCapabilities( String bsAppUrl, String browser,  String deviceName, String platform, String os_version ) {
		// Set your access credentials
		SetBsCredentials(getBsUsername(), getBsPassword());

		// Set URL of the application under test or browser to use
        if ( bsAppUrl != null)
		    SetAppUnderTesting(bsAppUrl);
        else if ( browser != null ) {
            caps.setBrowserName( browser );
            caps.setCapability(CapabilityType.BROWSER_NAME, browser); 
            caps.setCapability(CapabilityType.VERSION, "108");
        }
    	
    	// Specify device and os_version for testing
    	SetDeviceAndOsVersion(deviceName, platform, os_version);
        caps.setCapability("browserstack.appium_version", "1.21.0");
        caps.setCapability("browserstack.idleTimeout", "30");
        
	}
    public void SetProjectInformation( String projectName, String buildName, String testName) {
        // Set other BrowserStack capabilities

    	this.caps.setCapability("project", projectName);
    	this.caps.setCapability("build", buildName);
    	this.caps.setCapability("name", testName);
    }
    private void SetDeviceAndOsVersion(String device, String platform, String os_version) {
        this.caps.setCapability("device", device);
    	this.caps.setCapability("os_version", os_version);
        this.caps.setCapability("platformName", platform );
    }
    private void SetAppUnderTesting( String appUrlInBs ) {
        this.caps.setCapability("app", appUrlInBs);
    }
    private void SetBsCredentials(String username, String key ) {
        this.caps.setCapability("browserstack.user", username);
		this.caps.setCapability("browserstack.key", key);
    }
}
