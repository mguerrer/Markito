package cl.set.markito.devices;

public final class LocalComputer implements Device {
    public final String providerURL = "";
    public final String name = "Local Computer";
    public final String platform = System.getProperty("os.name");
    public final String platform_version = "";
    public String getName() {
        return name;
    }
    public String getPlatform() {
        return platform;
    }
    public String getPlatform_version() {
        return platform_version;
    }
    @Override
    public String getProviderURL() {
        return providerURL;
    }
    
}
