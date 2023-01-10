package cl.set.markito.cloud;

import java.util.Map;

import java.net.URL;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import cl.set.markito.utils.DebugManager;
import io.appium.java_client.MobileDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;

public class BrowserStack extends DebugManager {
    
    DesiredCapabilities capabilities = new DesiredCapabilities();
    public DesiredCapabilities getCapabilities() {
        return capabilities;
    }

    MobileDriver<MobileElement> mobiledriver;
    WebDriver webdriver;
    /**
     * Get BrowserStack's username from environment variable BSUSERNAME
     * 
     * @return
     */
    public String getBsUsername() {
        String username = System.getenv("BSUSERNAME");
        if (username == null) {
            printf(ANSI_RED
                    + "ERROR: BrowserStack user name not found.  Please add to environment variable BSUSERNAME.");
        }
        return username;
    }

    /**
     * Get BrowserStack's password (KEY) from environment variable BSPASSWORD
     * 
     * @return
     */
    public String getBsPassword() {
        String username = System.getenv("BSPASSWORD");
        if (username == null) {
            printf(ANSI_RED
                    + "ERROR: BrowserStack user KEY not found.  Please add to environment variable BSPASSWORD.");
        }
        return username;
    }

    /**
     * Logs in debug console the list of capabilities set with a pretty format.
     * 
     * @param caps
     */
    public void LogCapabilities(DesiredCapabilities caps) {
        Map<String, Object> jsoncaps = caps.toJson();
        println("\nCapabilities: ");
        for (String key : jsoncaps.keySet()) {
            if (!key.equals("browserstack.key") && !key.equals("browserstack.user")) {
                printf("-%s:%s\n", ANSI_WHITE + key, ANSI_YELLOW + jsoncaps.get(key));
            }
        }
    }

    public MobileDriver<MobileElement> openBrowserStackMobileSession() throws Exception {
        URL UrlAppiumServer = new URL("https://hub-cloud.browserstack.com/wd/hub");
        String platform = capabilities.getCapability("platformName").toString();
        String app;
        printf(ANSI_WHITE + "Opening %s Session in Browserstack in device %s...", platform,
                capabilities.getCapability("device"));
        if (capabilities.getCapability("app") != null) {
            app = capabilities.getCapability("app").toString();
            printf(ANSI_WHITE + " on app " + app);
        }

        try {
            switch (platform) {
                case "android":
                    mobiledriver = new AndroidDriver<MobileElement>(UrlAppiumServer, capabilities);
                    break;
                case "iOS":
                    mobiledriver = new IOSDriver<MobileElement>(UrlAppiumServer, capabilities);
                    break;
                default:
                    throw new Exception(ANSI_RED + "\nERROR on getting " + platform + " session, unknown platfom.");
            }
            if (this.mobiledriver == null) {
                throw new Exception(ANSI_RED + "\nERROR on getting " + platform + " session, value is null.");
            }
        } catch (Exception e) {
            throw new Exception(ANSI_RED + "\nERROR on getting " + platform + " session. \nStack: " + e.getMessage());
        }
        return mobiledriver;
    }

    public WebDriver openBrowserStackWebSession() throws Exception {
        URL UrlAppiumServer = new URL("https://hub-cloud.browserstack.com/wd/hub");
        String platform = capabilities.getCapability("platformName").toString();
        String browser;
        printf(ANSI_WHITE + "Opening %s Web Session in Browserstack in device %s...", platform,
                capabilities.getCapability("device"));
        if (capabilities.getCapability("browserName") != null) {
            browser = capabilities.getCapability("browserName").toString();
            printf(ANSI_WHITE + " on browser " + browser);
        }
        LogCapabilities(capabilities);

        try {
            switch (platform) {
                case "android":
                    webdriver = new RemoteWebDriver(UrlAppiumServer, capabilities);
                    break;
                case "iOS":
                    webdriver = new RemoteWebDriver(UrlAppiumServer, capabilities);
                    break;
                default:
                    throw new Exception(ANSI_RED + "\nERROR on getting " + platform + " session, unknown platfom.");
            }
            if (this.webdriver == null) {
                throw new Exception(ANSI_RED + "\nERROR on getting " + platform + " session, value is null.");
            }
        } catch (Exception e) {
            throw new Exception(ANSI_RED + "\nERROR on getting " + platform + " session. \nStack: " + e.getMessage());
        }
        return webdriver;
    }

    public void setDesiredTechnicalCapabilities(String bsAppUrl, String browser, String deviceName, String platform,
            String os_version) {
        BrowserStack bs = new BrowserStack();
        // Set your access credentials
        SetBsCredentials(bs.getBsUsername(), bs.getBsPassword());

        // Set URL of the application under test or browser to use
        if (bsAppUrl != null)
            SetAppUnderTesting(bsAppUrl);
        else if (browser != null) {
            capabilities.setBrowserName(browser);
            capabilities.setCapability(CapabilityType.BROWSER_NAME, browser);
            capabilities.setCapability(CapabilityType.VERSION, "108");
        }

        // Specify device and os_version for testing
        SetDeviceAndOsVersion(deviceName, platform, os_version);
        capabilities.setCapability("browserstack.appium_version", "1.21.0");
        capabilities.setCapability("browserstack.idleTimeout", "30");

    }

    public void SetProjectInformation(String projectName, String buildName, String testName) {
        // Set other BrowserStack capabilities

        this.capabilities.setCapability("project", projectName);
        this.capabilities.setCapability("build", buildName);
        this.capabilities.setCapability("name", testName);
    }

    public void SetDeviceAndOsVersion(String device, String platform, String os_version) {
        this.capabilities.setCapability("device", device);
        this.capabilities.setCapability("os_version", os_version);
        this.capabilities.setCapability("platformName", platform);
    }

    public void SetAppUnderTesting(String appUrlInBs) {
        this.capabilities.setCapability("app", appUrlInBs);
    }

    public void SetBsCredentials(String username, String key) {
        this.capabilities.setCapability("browserstack.user", username);
        this.capabilities.setCapability("browserstack.key", key);
    }
}
