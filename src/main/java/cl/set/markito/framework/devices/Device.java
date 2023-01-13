package cl.set.markito.framework.devices;

public interface Device {
    public String getProviderURL();
    public String getName();
    public void setPlatform(OS os);
    public OS getPlatform();
    public String getPlatform_version();
}