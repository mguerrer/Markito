package cl.set.markito.framework.devices;

public final class LocalComputer extends Device {
    public LocalComputer(){
        providerURL = "";
        name = "Local Computer";
        platform = OS.CURRENT;
        platform_version = "";
    }
}
