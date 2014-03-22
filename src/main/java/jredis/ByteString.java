package jredis;

/**
 * ByteString data structure to handle both string and bit string.
 * 
 * @author anoopelias
 * 
 */
public class ByteString {

    private static final int BYTE_SIZE = 8;
    private static final int INIT_SIZE = 256;
    private byte[] value;

    /**
     * Bit constructor.
     * 
     * @param value
     */
    public ByteString() {
        this.value = new byte[INIT_SIZE];
    }

    /**
     * String constructor.
     * 
     * @param value
     */
    public ByteString(String value) {
        this.value = Protocol.toBytes(value);
    }

    /**
     * Byte array constructor.
     * 
     * @param value
     */
    public ByteString(byte[] value) {
        this.value = value;
    }

    /**
     * Get bit value at offset.
     * 
     * @param offset
     * @return bit value (0 or 1). 0 if offset is bigger than current size.
     */
    public int getBit(int offset) {
        if (value == null)
            return 0;

        int bytePos = offset / BYTE_SIZE;
        int bitPos = offset % BYTE_SIZE;

        if (value.length < bytePos)
            return 0;

        return isBitSet(value[bytePos], bitPos);
    }

    /**
     * Ref :
     * http://stackoverflow.com/questions/1034473/java-iterate-bits-in-byte
     * -array
     * 
     * @param b
     * @param bit
     * @return
     */
    private static int isBitSet(byte b, int bit) {
        return (b & (1 << BYTE_SIZE - (bit + 1))) != 0 ? 1 : 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    public String toString() {
        if (value == null)
            return null;

        return Protocol.toString(value);
    }

}
