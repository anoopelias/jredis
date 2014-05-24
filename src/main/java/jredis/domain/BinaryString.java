package jredis.domain;

import java.util.Arrays;

import jredis.Protocol;

/**
 * ByteString data structure to handle both string and bit string.
 * 
 * @author anoopelias
 * 
 */
public class BinaryString {

    private static final int BYTE_SIZE = 8;
    private static final int INIT_SIZE = 256;
    private byte[] value;
    private int length;

    /**
     * Default constructor.
     * 
     * @param value
     */
    public BinaryString() {
        this.value = new byte[INIT_SIZE];
        length = 0;
    }

    /**
     * String constructor.
     * 
     * @param value
     */
    public BinaryString(String value) {
        this.value = Protocol.toBytes(value);
        length = this.value.length;
    }

    /**
     * Byte array constructor.
     * 
     * @param value
     */
    public BinaryString(byte[] value) {
        this.value = value;
        length = value.length;
    }

    /**
     * Get bit value at offset.
     * 
     * @param offset
     * @return bit value (0 or 1). 0 if offset is bigger than current size.
     */
    public boolean getBit(int offset) {
        int bytePos = offset / BYTE_SIZE;
        int bitPos = offset % BYTE_SIZE;

        if (value.length <= bytePos)
            return false;

        return isBitSet(bytePos, bitPos);
    }

    /**
     * Set bit value at offset.
     * 
     * @param offset
     * @return the original value of this bit before setting it.
     */
    public boolean setBit(int offset, boolean bit) {
        boolean ret = getBit(offset);

        int bytePos = offset / BYTE_SIZE;
        int bitPos = offset % BYTE_SIZE;
        setBitValue(bytePos, bitPos, bit);

        return ret;
    }

    /**
     * Check if a bit inside a byte is set.
     * 
     * Ref :
     * http://stackoverflow.com/questions/1034473/java-iterate-bits-in-byte
     * -array
     * 
     * @param b
     * @param bit
     * @return
     */
    private boolean isBitSet(int bytePos, int bitPos) {
        return (value[bytePos] & (1 << lsb(bitPos))) != 0;
    }

    /**
     * 
     * @param b
     * @param bytePos
     * @return
     */
    private void setBitValue(int bytePos, int bitPos, boolean bit) {
        int lsb = lsb(bitPos);
        if (bytePos >= value.length)
            expand(bytePos);

        byte curr = value[bytePos];

        value[bytePos] = (byte) (bit ? (curr | (1 << lsb))
                : (curr & ~(1 << lsb)));

        // Increase the length if required.
        if (length < (bytePos + 1))
            length = bytePos + 1;
    }

    /**
     * Expand the size of the value to accommodate minSize.
     * 
     * @param minSize
     */
    private void expand(int minSize) {
        // http://stackoverflow.com/questions/22573483/efficient-array-expansion-in-java
        value = Arrays.copyOf(value, Integer.highestOneBit(minSize) * 2);
    }

    /**
     * Get position from lsb side.
     * 
     * @param bitPos
     * @return
     */
    private int lsb(int bitPos) {
        return BYTE_SIZE - (bitPos + 1);
    }

    /**
     * Get the value in terms of byte array.
     * 
     * @return
     */
    public ByteArray toByteArray() {
        return new ByteArray(value, length);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return Protocol.toString(value, 0, length);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + length;
        result = prime * result + Arrays.hashCode(value);
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
        BinaryString other = (BinaryString) obj;
        if (length != other.length)
            return false;
        if (!Arrays.equals(value, other.value))
            return false;
        return true;
    }

}
