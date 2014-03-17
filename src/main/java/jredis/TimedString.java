package jredis;

/**
 * Represents a value to be stored in the system.
 * 
 * @author anoopelias
 *
 */
public class TimedString {
    
    private String value;
    
    private Long expiryTime;

    public TimedString(String value) {
        this.value = value;
    }

    public TimedString(String value, Long expiry) {
        this.value = value;
        this.expiryTime = expiry + System.currentTimeMillis();
    }

    public TimedString(String value, long expiryTime) {
        this.value = value;
        this.expiryTime = expiryTime;
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
