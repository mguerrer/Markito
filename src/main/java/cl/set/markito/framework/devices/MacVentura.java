package cl.set.markito.framework.devices;

public final class MacVentura implements Device {
    public final String providerURL = "http://hub-cloud.browserstack.com/wd/hub";
    public final String name = "Browserstack MAC Ventura";
    public OS platform = OS.OSX;
    public final String platform_version = "Ventura";
    public String getProviderURL() {
        return providerURL;
    }
    public String getName() {
        return name;
    }
    public void setPlatform( OS os ) {
        platform = os;
    }
    public OS getPlatform() {
        return platform;
    }
    public String getPlatform_version() {
        return platform_version;
    }
}
