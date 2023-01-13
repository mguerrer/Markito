package cl.set.markito.framework.devices;

public final class GooglePixel3 implements Device {
    public final String providerURL = "http://hub-cloud.browserstack.com/wd/hub";
    public final String name = "Google Pixel 3";
    public OS platform = OS.ANDROID;
    public final String platform_version = "10.0";
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
    }    public String getPlatform_version() {
        return platform_version;
    }
}
