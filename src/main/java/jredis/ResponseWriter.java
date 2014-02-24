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
    public void write(String output) {
        if (output != null)
            out.println("+" + output + "\r");
        else
            out.println("$-1\r");
    }

}
