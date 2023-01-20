package cl.set.markito.framework.devices;

public class Windows10 extends Device {
    public Windows10(){
        providerURL = "http://hub-cloud.browserstack.com/wd/hub";
        name = "Browserstack Windows 10";
        platform = OS.WINDOWS;
        platform_version = "10";
    }
}
