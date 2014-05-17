package jredis.data;

import java.io.IOException;

import jredis.ResponseWriter;

/**
 * An OK response.
 * 
 * @author anoopelias
 *
 */
public class ResponseOk implements Response<String> {
    
    public static final String OK = "OK";

    @Override
    public String value() {
        return OK;
    }

    @Override
    public void write(ResponseWriter writer) throws IOException {
        writer.writeOk();
    }

}
