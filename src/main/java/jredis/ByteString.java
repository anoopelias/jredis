package jredis;

/**
 * ByteString data structure to handle both string and bit string.
 * 
 * @author anoopelias
 *
 */
public class ByteString {
    
    private byte[] value;
    
    /**
     * String constructor.
     * 
     * @param value
     */
    public ByteString(String value) {
        this.value = Protocol.toBytes(value);
    }

    /**
     * Byte array constructor.
     * 
     * @param value
     */
    public ByteString(byte[] value) {
        this.value = value;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
        if(value == null)
            return null;
        
        return Protocol.toString(value);
    }

}
