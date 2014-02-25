package jredis;

/**
 * Bit response type.
 * 
 * @author anoopelias
 *
 */
public class ResponseBit implements Response<Boolean> {
    
    private Boolean bit;
    
    /**
     * Construct a response of type bit with the value specified.
     * 
     * @param bit
     */
    public ResponseBit(Boolean bit) {
        this.bit = bit;
    }

    @Override
    public Boolean value() {
        return bit;
    }

    @Override
    public String toProtocolString() {
        return ":" + (bit ? "1" : "0");
    }

}
