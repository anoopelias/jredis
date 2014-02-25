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
     * Contruct a writer using output stream.
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
        if (response != null)
            out.println(response.toProtocolString() + "\r");
        else
            out.println("$-1\r");
    }

}
