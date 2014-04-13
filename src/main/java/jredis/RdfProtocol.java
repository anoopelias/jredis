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
     * End of file. (Checksum follows) 
     */
    public static final byte[] END = { (byte) 0xff };
    
    /**
     * Types of values.
     * 
     * @author anoopelias
     *
     */
    public static enum ValueType {
        STRING(0),
        SORTED_SET_ZIPLIST(12);
        
        private final int value;
        
        private ValueType(int i) {
            this.value = i;
        }
        
        public int value() {
            return value;
        }
    }


}
