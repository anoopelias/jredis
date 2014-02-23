package jredis;

import java.io.OutputStream;
import java.io.PrintWriter;

public class ResponseWriter {

    private PrintWriter out;

    public ResponseWriter(OutputStream os) {
        out = new PrintWriter(os, true);
    }

    public void write(String output) {
        if (output != null)
            out.println("+" + output + "\r");
        else
            out.println("$-1\r");
    }

}
