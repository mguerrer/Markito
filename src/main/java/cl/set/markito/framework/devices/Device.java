package cl.set.markito.framework.devices;

public abstract class Device {
    public String providerURL = "http://hub-cloud.browserstack.com/wd/hub";
    public String name = "Google Pixel 3";
    public OS platform = OS.ANDROID;
    public String platform_version = "10.0";
    public String getProviderURL() {
        return providerURL;
    }
    public String getName() {
        return name;
    }
    public void setPlatform( OS os ) {
        this.platform = os;
    }
    public OS getPlatform() {
        return platform;
    }    
    public String getPlatform_version() {
        return platform_version;
    }
    public Boolean isIOS() {
        return getPlatform().toString().equals("iOS");
    }
    public Boolean isAndroid() {
        return getPlatform().toString().equals("Android");
    }
    public Boolean isMobile() {
        return getPlatform().toString().equals("iOS") || getPlatform().toString().equals("Android");
    }

}