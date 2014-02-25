package jredis;

public class ResponseNumber implements Response<Integer> {
    
    private Integer number;
    
    public ResponseNumber(Integer number) {
        this.number = number;
    }

    @Override
    public Integer value() {
        return number;
    }

    @Override
    public String toProtocolString() {
        return ":" + number;
    }

}
