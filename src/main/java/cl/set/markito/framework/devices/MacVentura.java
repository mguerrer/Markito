package cl.set.markito.framework.devices;

public final class MacVentura extends Device {
    public MacVentura(){
        providerURL = "http://hub-cloud.browserstack.com/wd/hub";
        name = "Browserstack MAC Ventura";
        platform = OS.OSX;
        platform_version = "Ventura";
    }
}
