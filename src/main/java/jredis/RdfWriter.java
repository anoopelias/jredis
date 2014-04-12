package jredis;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Write data into stream in RDF format.
 * 
 * @author anoopelias
 * 
 */
public class RdfWriter {

    private OutputStream os = null;

    /**
     * Construct with a stream.
     * 
     * @param os
     */
    public RdfWriter(OutputStream os) {
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
        lenToBytes(by.length()).write(os);
        by.write(os);
        os.flush();
    }

    /**
     * Write the number in length encoding.
     * 
     * @param unsignedInt
     * @throws IOException
     */
    private ByteArray lenToBytes(long unsignedInt) throws IOException {
        byte[] bytes = null;
        if (unsignedInt >>> 6 == 0) {// If the number is only 6 bits
            bytes = new byte[1];
            bytes[0] = (byte) unsignedInt;
        } else if (unsignedInt >>> 14 == 0) { // If it is only 14 bits
            bytes = new byte[2];
            bytes[0] = (byte) (unsignedInt >>> 8 | 0x40);
            bytes[1] = (byte) unsignedInt;
        } else { // Means the length can be expressed only in 4 bytes
            bytes = new byte[5];
            bytes[0] = (byte) (0x80);

            // Big endian conversion
            bytes[1] = (byte) (unsignedInt >>> 24);
            bytes[2] = (byte) (unsignedInt >>> 16);
            bytes[3] = (byte) (unsignedInt >>> 8);
            bytes[4] = (byte) (unsignedInt);
        }
        return new ByteArray(bytes);
    }

    /**
     * String to bytes.
     * 
     * @param s
     * @throws IOException
     */
    private List<ByteArray> toBytes(String s) throws IOException {
        ByteArray strBytes = new ByteArray(Protocol.toBytes(s));

        List<ByteArray> bytes = new ArrayList<>();
        bytes.add(lenToBytes(strBytes.length()));
        bytes.add(strBytes);

        return bytes;
    }

    /**
     * Write a byte string.
     * 
     * @param b
     * @throws IOException
     */
    public void write(ElementSet es) throws IOException {

        long prevLength = 0;
        int tail = -1;
        List<ByteArray> bytes = new ArrayList<>();

        // Start with zllen
        bytes.add(0, to2Bytes(es.size() * 2));

        for (Element e : es) {

            // Add member
            List<ByteArray> entry = toBytes(e.getMember());
            entry.add(0, new ByteArray(new byte[] { (byte) prevLength }));
            bytes.addAll(entry);
            prevLength = len(entry);

            // Add score
            tail = bytes.size() + 1;
            entry = toBytes(e.getScore());
            entry.add(0, new ByteArray(new byte[] { (byte) prevLength }));
            bytes.addAll(entry);
            prevLength = len(entry);
        }

        // Adding zlend to the end
        bytes.add(new ByteArray(new byte[]{(byte)0xff}));

        /*
         * Adding zltail
         * 
         * offset = 4 zlbytes + 4 zltail = 8
         */
        bytes.add(0, to4Bytes(len(bytes.subList(0, tail)) + 8));
        
        long len = len(bytes) + 4;

        // Adding zlbytes
        bytes.add(0, to4Bytes(len));

        // Adding length
        bytes.add(0, lenToBytes(len));

        for (ByteArray byt : bytes)
            byt.write(os);

        os.flush();
    }

    /**
     * Find the total length of the list.
     * 
     * @param bytes
     * @return
     */
    private long len(List<ByteArray> bytes) {
        long len = 0;
        for (ByteArray b : bytes)
            len += b.length();
        return len;
    }

    /**
     * Convert double to bytes.
     * 
     * @param d
     * @return
     * @throws IOException
     */
    private List<ByteArray> toBytes(double d) throws IOException {
        double df = Math.floor(d);
        if ((d == df) && !Double.isInfinite(d)) {
            return Arrays.asList(toBytes((int) df));
        } else {
            return toBytes(String.valueOf(d));
        }
    }

    /**
     * Convert unsigned integer to 4 bytes. Little endian.
     * 
     * @param d
     * @return
     * @throws IOException
     */
    private ByteArray to4Bytes(long unsignedInt) throws IOException {
        byte[] bytes = new byte[4];
        // Big endian conversion
        bytes[0] = (byte) (unsignedInt);
        bytes[1] = (byte) (unsignedInt >>> 8);
        bytes[2] = (byte) (unsignedInt >>> 16);
        bytes[3] = (byte) (unsignedInt >>> 24);

        return new ByteArray(bytes);
    }

    /**
     * Convert integer to bytes.
     * 
     * @param i
     * @return
     */
    private ByteArray toBytes(int i) {

        // TODO: Implement.

        return null;
    }

    /**
     * Convert unsigned integer to 2 bytes. Little endian.
     * 
     * @param i
     *            unsigned integer not greater than 2 bytes.
     * @return
     */
    private ByteArray to2Bytes(int i) {
        byte[] bytes = new byte[2];
        bytes[0] = (byte) i;
        bytes[1] = (byte) (i >>> 8);

        return new ByteArray(bytes);
    }

}
