package jredis;

import static org.junit.Assert.assertEquals;

import java.io.UnsupportedEncodingException;

import org.junit.Test;

public class ByteStringTest {

    private static final byte[] BYTE = { 0x50, 0x51, 0x52, 0x53, 0x54, 0x55, 0x56, 0x57, 0x58, 0x59 };
    private static final String BIT = "01010000010100010101001001010011010101000101010101010110010101110101100001011001";

    @Test
    public void test_string() {
        ByteString byteString = new ByteString("Mathew Perry");
        assertEquals("Mathew Perry", byteString.toString());
    }

    @Test
    public void test_bytes() throws UnsupportedEncodingException {
        ByteString byteString = new ByteString(
                "Mathew Perry".getBytes(Protocol.CHARSET));
        assertEquals("Mathew Perry", byteString.toString());
    }

    @Test
    public void test_getbit_null() throws UnsupportedEncodingException {
        ByteString byteString = new ByteString();

        assertEquals(0, byteString.getBit(0));
        assertEquals(0, byteString.getBit(10));
        assertEquals(0, byteString.getBit(100));
        assertEquals(0, byteString.getBit(1000));
        assertEquals(0, byteString.getBit(10000));
    }

    @Test
    public void test_getbit() throws UnsupportedEncodingException {
        ByteString byteString = new ByteString(BYTE);
        
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<80; i++) 
            sb.append(byteString.getBit(i));
        
        assertEquals(BIT, sb.toString());
        
        // Verify beyond init range as well.
        assertEquals(0, byteString.getBit(10000));
    }

    @Test
    public void test_setbit_small_offset() throws UnsupportedEncodingException {
        ByteString byteString = new ByteString(BYTE);
        assertEquals(0,byteString.getBit(0));
        byteString.setBit(0, true);
        assertEquals(1,byteString.getBit(0));
    }

}
