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

    /**
     * Construct a writer using output stream.
     * 
     * @param os
     */
    public ResponseWriter(OutputStream os) {
        out = new BufferedOutputStream(os);
    }

    /**
     * Write a response back to the client.
     * 
     * @param output
     * @throws IOException 
     */
    public void write(Response<?> response) throws IOException {
        out.write(response.getBytes());
        out.flush();
    }

}
