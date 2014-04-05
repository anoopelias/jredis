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

    private void writeLen(long unsignedInt) throws IOException {
        if (unsignedInt >>> 6 == 0) // If the number is only 6 bits
            os.write((byte) unsignedInt);
        else if (unsignedInt >>> 14 == 0) { // If it is only 14 bits
            os.write((int) (unsignedInt >>> 8 | 0x40));
            os.write((int) (unsignedInt));
        } else {
            os.write(0x80);
            os.write(toBytes(unsignedInt));
        }
    }

    private byte[] toBytes(long unsignedInt) {
        byte[] by = new byte[4];
        by[0] = (byte) (unsignedInt >>> 24);
        by[1] = (byte) (unsignedInt >>> 16);
        by[2] = (byte) (unsignedInt >>> 8);
        by[3] = (byte) (unsignedInt);

        return by;
    }

}
