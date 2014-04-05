package jredis;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Write data into stream in RDF format.
 * 
 * @author anoopelias
 * 
 */
public class StreamWriter {

    private OutputStream os = null;

    /**
     * Construct with a stream.
     * 
     * @param os
     */
    public StreamWriter(OutputStream os) {
        this.os = new BufferedOutputStream(os);
    }

    /**
     * Write a byte string.
     * 
     * @param b
     * @throws IOException
     */
    public void write(ByteString b) throws IOException {
        ByteArray by = b.toByteArray();
        writeLen(by.length());
        by.write(os);
        os.flush();
    }

    private void writeLen(long num) throws IOException {
        if (num >>> 6 == 0) // If the number is only 6 bits
            os.write((byte) num);
        else if (num >>> 14 == 0) {
            os.write((int) (num >>> 8 | 0x40));
            os.write((int) (num));
        }
    }

}
