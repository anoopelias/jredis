package jredis;

/**
 * A byte string wrapped with an expiry time.
 * 
 * @author anoopelias
 *
 */
public class TimedByteString {
    private BinaryString value;
    
    private Long expiryTime;

    /**
     * Default constructor with infinite expiry.
     * 
     * @param value
     */
    public TimedByteString(BinaryString value) {
        this.value = value;
    }

    /**
     * Constructor with value and its expiry.
     * 
     * @param value
     * @param expiryTime
     */
    public TimedByteString(BinaryString value, long expiryTime) {
        this.value = value;
        this.expiryTime = expiryTime;
    }

    /**
     * Get the value.
     * 
     * @return
     */
    public BinaryString value() {
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
    
    /**
     * Expiry time. null if no expiry.
     * 
     * @return
     */
    public Long expiryTime() {
        return expiryTime;
    }
    
}
