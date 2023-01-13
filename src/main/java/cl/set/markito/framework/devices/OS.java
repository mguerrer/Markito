package cl.set.markito.framework.devices;

/**
 * Enum type for browser names.  Use it to avoid typing errors.
 */
public enum OS {
    WINDOWS("Windows"), LINUX("Linux"), OSX("OS X"), ANDROID("Android"), IOS("iOS"), CURRENT("");
    
    public final String name;
    
    OS(String name) {
        this.name = name;
    }
    
    @Override
    public String toString() {
        return name;
    }
}
    

