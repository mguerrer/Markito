package cl.set.markito.devices;

public final class iPhone11Pro implements Device {
    public final String providerURL = "http://hub-cloud.browserstack.com/wd/hub";
    public final String name = "iPhone 11 Pro";
    public final String OS = "iOS";
    public final String OS_version = "13";
    public String getProviderURL() {
        return providerURL;
    }
    public String getName() {
        return name;
    }
    public String getOS() {
        return OS;
    }
    public String getOS_version() {
        return OS_version;
    }
}
