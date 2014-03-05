package jredis;

/**
 * String response type.
 * 
 * @author anoopelias
 *
 */
public class ResponseString implements Response<String> {
    
    private String value;
    
    /**
     * ResponseString constructor to be used for 'nil' response.
     * 
     */
    public ResponseString() {
        
    }
    
    /**
     * Construct a String response.
     * 
     * @param value
     */
    public ResponseString(String value) {
        this.value = value;
    }

    @Override
    public String value() {
        return value;
    }

    @Override
    public byte[] getBytes() {
        if(value == null)
            return "$-1\r\n".getBytes();
        
        return ("+" + value + "\r\n").getBytes();
    }

}
