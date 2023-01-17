package cl.set.markito.framework.devices;

public final class LocalComputer implements Device {
    public final String providerURL = "";
    public final String name = "Local Computer";
    public OS platform = OS.CURRENT;
    public final String platform_version = "";
    public final String getName() {
        return name;
    }
    public void setPlatform( OS os ) {
        platform = os;
    }
    public OS getPlatform() {
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
