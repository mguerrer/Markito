package cl.set.markito.framework.devices;

public abstract class Device {
    public String providerURL = "";
    public String name = "";
    public OS platform = null;
    public String platform_version = "";
    
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