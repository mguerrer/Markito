package cl.set.markito.devices;

public final class Windows11 implements Device {
    public final String providerURL = "http://hub-cloud.browserstack.com/wd/hub";
    public final String name = "Browserstack Windows 11";
    public final String platform = "Windows";
    public final String platform_version = "11";
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
