package jredis.domain;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

import jredis.Protocol;

/**
 * A ByteArray which can handle subsize in constant time.
 * 
 * @author anoopelias
 * 
 */
public class ByteArray {

    private byte[] content;
    private int length;

    /**
     * Construct byte array from content.
     * 
     * @param content
     */
    public ByteArray(byte[] content) {
        this.content = content;
        this.length = content.length;
    }

    /**
     * Construct a byte array with a size less than or equal to the content. The
     * content will be truncated by the less number of bytes.
     * 
     * @param content
     * @param length
     * @throws IllegalArgumentException
     *             if length is bigger than the length of the content.
     */
    public ByteArray(byte[] content, int length)
            throws IllegalArgumentException {
        if (length > content.length)
            throw new IllegalArgumentException(
                    "length is greater than content length");

        this.content = content;
        this.length = length;
    }

    /**
     * To create a subArray of given length.
     * 
     * @param length
     * @return
     * @throws IllegalArgumentException
     *             if length is greater than current length.
     */
    public ByteArray subArray(int length) throws IllegalArgumentException {
        if (length > this.length)
            throw new IllegalArgumentException(
                    "SubArray size is bigger than original array");

        return new ByteArray(content, length);
    }

    /**
     * Get the length of the byte array.
     * 
     * @return
     */
    public int length() {
        return length;
    }

    /**
     * Write the bytes to an output stream.
     * 
     * @param os
     * @throws IOException
     */
    public void write(OutputStream os) throws IOException {
        for (int i = 0; i < length; i++)
            os.write(content[i]);
    }

    @Override
    public String toString() {
        return Protocol.toString(content, 0, length);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(content);
        result = prime * result + length;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ByteArray other = (ByteArray) obj;
        if (length != other.length)
            return false;

        if (!sameContent(other))
            return false;

        return true;
    }

    private boolean sameContent(ByteArray other) {
        for (int i = 0; i < length; i++)
            if (content[i] != other.content[i])
                return false;
        return true;
    }

}
