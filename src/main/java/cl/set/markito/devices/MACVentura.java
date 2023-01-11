package cl.set.markito.devices;

public final class MACVentura implements Device {
    public final String providerURL = "http://hub-cloud.browserstack.com/wd/hub";
    public final String name = "Browserstack MAC Ventura";
    public final String platform = "OS X";
    public final String platform_version = "Ventura";
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
