package cl.set.markito.framework.devices;

public final class GooglePixel3 extends Device {
    public GooglePixel3() {
        providerURL = "http://hub-cloud.browserstack.com/wd/hub";
        name = "Google Pixel 3";
        platform = OS.ANDROID;
        platform_version = "10.0";
    }
}
