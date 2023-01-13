package cl.set.markito.framework.devices;

public final class Windows11 implements Device {
    public final String providerURL = "http://hub-cloud.browserstack.com/wd/hub";
    public final String name = "Browserstack Windows 11";
    public OS platform = OS.WINDOWS;
    public final String platform_version = "11";
    public String getProviderURL() {
        return providerURL;
    }
    public String getName() {
        return name;
    }
    
    public void setPlatform(OS os) {
        platform = os;
    }
    public OS getPlatform() {
        return platform;
    }
    public String getPlatform_version() {
        return platform_version;
    }
}
