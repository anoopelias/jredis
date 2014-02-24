package jredis;

/**
 * Represents a value to be stored in the system.
 * 
 * @author anoopelias
 *
 */
public class StringValue {
    
    private String value;
    
    private Long expiry;

    public StringValue(String value) {
        this.value = value;
    }

    public StringValue(String value, Long expiry) {
        this.value = value;
        this.expiry = expiry;
    }
    
    /**
     * Get the value.
     * 
     * @return
     */
    public String value() {
        return value;
    }
    
    /**
     * Check if the value is still valid.
     * 
     * @return
     */
    public boolean isValid() {
        return expiry == null || expiry > System.currentTimeMillis();
    }

}
