package cl.set.markito.devices;

public final class GooglePixel3 implements Device {
    public final String providerURL = "http://hub-cloud.browserstack.com/wd/hub";
    public final String name = "Google Pixel 3";
    public final String platform = "Android";
    public final String platform_version = "9.0";
    public String getProviderURL() {
        return providerURL;
    }
    public String getName() {
        return name;
    }
    public String getPlatform() {
        return platform;
    }
    public String getPlatform_version() {
        return platform_version;
    }
}
