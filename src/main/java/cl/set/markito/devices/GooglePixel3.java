package cl.set.markito.devices;

public final class GooglePixel3 implements Device {
    public final String providerURL = "http://hub-cloud.browserstack.com/wd/hub";
    public final String name = "Google Pixel 3";
    public final String OS = "Android";
    public final String OS_version = "9.0";
    public String getProviderURL() {
        return providerURL;
    }
    public String getName() {
        return name;
    }
    public String getOS() {
        return OS;
    }
    public String getOS_version() {
        return OS_version;
    }
}
