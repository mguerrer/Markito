package cl.set.markito.devices;

public final class iPhone11Pro implements Device {
    public final String providerURL = "http://hub-cloud.browserstack.com/wd/hub";
    public final String name = "iPhone 11 Pro";
    public final String platform = "iOS";
    public final String platform_version = "13";
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
