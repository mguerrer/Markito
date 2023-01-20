package cl.set.markito.framework.devices;

public final class iPhone11Pro extends Device {
    public iPhone11Pro(){
        providerURL = "http://hub-cloud.browserstack.com/wd/hub";
        name = "iPhone 11 Pro";
        platform = OS.IOS;
        platform_version = "15";
    }
}
