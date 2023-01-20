package cl.set.markito.framework.devices;

public final class Windows11 extends Device {
    public Windows11(){
        providerURL = "http://hub-cloud.browserstack.com/wd/hub";
        name = "Browserstack Windows 11";
        platform = OS.WINDOWS;
        platform_version = "11";
    }
}
