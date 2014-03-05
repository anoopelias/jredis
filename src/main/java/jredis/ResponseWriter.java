package jredis;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Response writer to handle response protocol.
 * 
 * @author anoopelias
 * 
 */
public class ResponseWriter {

    private OutputStream out;
    
    private static byte[] CRLF = {'\r', '\n'};

    /**
     * Construct a writer using output stream.
     * 
     * @param os
     */
    public ResponseWriter(OutputStream os) {
        out = new BufferedOutputStream(os);
    }

    /**
     * Write a string back to the client.
     * 
     * @param output
     * @throws IOException 
     */
    public void write(String value) throws IOException {
        if(value == null)
            out.write("$-1".getBytes());
        else 
            out.write(("+" + value).getBytes());
        
        out.write(CRLF);
        out.flush();
    }

    /**
     * Write a number back to the client.
     * 
     * @param output
     * @throws IOException 
     */
    public void write(int number) throws IOException {
        out.write((":" + number).getBytes());        
        out.write(CRLF);
        out.flush();
    }

}
