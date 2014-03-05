package jredis;

import java.io.IOException;

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
    public void write(ResponseWriter writer) throws IOException {
        writer.write(number.intValue());
    }

}
