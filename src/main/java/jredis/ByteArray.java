package jredis;

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
     * @throws IllegalArgumentException if length is bigger than the length of the content.
     */
    public ByteArray(byte[] content, int length)
            throws IllegalArgumentException {
        if(length > content.length)
            throw new IllegalArgumentException("length is greater than content length");
        
        this.content = content;
        this.length = length;
    }
    
    /**
     * To create a subArray of given length.
     * 
     * @param length
     * @return
     * @throws IllegalArgumentException if length is greater than current length.
     */
    public ByteArray subArray(int length) throws IllegalArgumentException {
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

    @Override
    public String toString() {
        return Protocol.toString(content, 0, length);
    }

}
