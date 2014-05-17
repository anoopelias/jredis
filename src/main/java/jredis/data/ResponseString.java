package jredis.data;

import java.io.IOException;

import jredis.ResponseWriter;
import jredis.domain.BinaryString;

/**
 * String response type.
 * 
 * @author anoopelias
 *
 */
public class ResponseString implements Response<String> {
    
    private BinaryString value;
    
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
    public ResponseString(BinaryString value) {
        this.value = value;
    }

    @Override
    public String value() {
        return value == null ? null : value.toString();
    }

    @Override
    public void write(ResponseWriter writer) throws IOException {
        writer.write(value);
    }

}
