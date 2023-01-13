package cl.set.markito.framework.devices;

public class Windows10 implements Device {
    public String providerURL = "http://hub-cloud.browserstack.com/wd/hub";
    public String name = "Browserstack Windows 10";
    public OS platform = OS.WINDOWS;
    public String platform_version = "10";
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
