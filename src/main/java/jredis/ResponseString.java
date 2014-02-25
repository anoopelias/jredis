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
    public String toProtocolString() {
        return "+" + value;
    }

}
