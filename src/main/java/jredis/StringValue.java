package jredis;

/**
 * Represents a value to be stored in the system.
 * 
 * @author anoopelias
 *
 */
public class StringValue {
    
    private String value;
    
    private Long expiryTime;

    public StringValue(String value) {
        this.value = value;
    }

    public StringValue(String value, Long expiry) {
        this.value = value;
        this.expiryTime = expiry + System.currentTimeMillis();
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
        return expiryTime == null || expiryTime > System.currentTimeMillis();
    }

}
