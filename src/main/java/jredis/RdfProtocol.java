package jredis;

public class RdfProtocol {
    
    private RdfProtocol() {
    }
    
    /**
     * Initialization structure of the file.
     * 
     * Version 6, database 00 is supported at the moment.
     * 
     * 'REDIS0006' 0xFE 0x00
     * 
     */
    public static final byte[] INIT = { 0x52, 0x45, 0x44, 0x49, 0x53, 0x30,
            0x30, 0x30, 0x36, (byte) 0xfe, 0x00 };


    /**
     * Convert an unsigned integer to bytes. Big Endian.
     * 
     * @param unsignedInt
     * @return
     */
    public static byte[] unsignedIntToBytes(long unsignedInt) {
        byte[] by = new byte[4];
        by[0] = (byte) (unsignedInt >>> 24);
        by[1] = (byte) (unsignedInt >>> 16);
        by[2] = (byte) (unsignedInt >>> 8);
        by[3] = (byte) (unsignedInt);

        return by;
    }

}
