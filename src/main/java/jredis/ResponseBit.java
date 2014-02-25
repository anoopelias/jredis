package jredis;

public class ResponseBit implements Response<Boolean> {
    
    private Boolean bit;
    
    public ResponseBit(Boolean bit) {
        this.bit = bit;
    }

    @Override
    public Boolean value() {
        return bit;
    }

    @Override
    public String toProtocolString() {
        // TODO Auto-generated method stub
        return null;
    }

}