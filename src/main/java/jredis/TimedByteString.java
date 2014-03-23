package jredis;

/**
 * A byte string wrapped with an expiry time.
 * 
 * @author anoopelias
 *
 */
public class TimedByteString {
    private ByteString value;
    
    private Long expiryTime;

    /**
     * Default constructor with infinite expiry.
     * 
     * @param value
     */
    public TimedByteString(ByteString value) {
        this.value = value;
    }

    /**
     * Constructor with value and its expiry.
     * 
     * @param value
     * @param expiryTime
     */
    public TimedByteString(ByteString value, long expiryTime) {
        this.value = value;
        this.expiryTime = expiryTime;
    }

    /**
     * Get the value.
     * 
     * @return
     */
    public ByteString value() {
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
