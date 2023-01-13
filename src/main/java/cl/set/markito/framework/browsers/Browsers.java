package cl.set.markito.framework.browsers;

/**
 * Enum type for browser names.  Use it to avoid typing errors.
 */
public enum Browsers {
    CHROME("Chrome"), EDGE("Edge"), FIREFOX("Firefox"), IE("Internet Explorer"), SAFARI("Safari");
    
    public final String name;
    
    Browsers(String name) {
        this.name = name;
    }
    
    @Override
    public String toString() {
        return name;
    }
}
    

