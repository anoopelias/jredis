package jredis;

import java.io.OutputStream;
import java.io.PrintWriter;

/**
 * Response writer to handle response protocol.
 * 
 * @author anoopelias
 *
 */
public class ResponseWriter {

    private PrintWriter out;

    /**
     * Construct a writer using output stream.
     * 
     * @param os
     */
    public ResponseWriter(OutputStream os) {
        out = new PrintWriter(os, true);
    }

    /**
     * Write a response back to the client.
     * 
     * @param output
     */
    public void write(Response<?> response) {
        out.println(response.encode() + "\r");
    }

}
