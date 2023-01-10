package cl.set.markito.devices;

public final class LocalComputer implements Device {
    public final String providerURL = "";
    public final String name = "Local Computer";
    public final String OS = System.getProperty("os.name");
    public final String OS_version = "";
    public String getName() {
        return name;
    }
    public String getOS() {
        return OS;
    }
    public String getOS_version() {
        return OS_version;
    }
    @Override
    public String getProviderURL() {
        return providerURL;
    }
    
}
