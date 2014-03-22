package jredis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.UnsupportedEncodingException;

import org.junit.Test;

public class ByteStringTest {

    private static final byte[] BYTE = { 0x50, 0x51, 0x52, 0x53, 0x54, 0x55,
            0x56, 0x57, 0x58, 0x59 };
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

        assertFalse(byteString.getBit(0));
        assertFalse(byteString.getBit(10));
        assertFalse(byteString.getBit(100));
        assertFalse(byteString.getBit(1000));
        assertFalse(byteString.getBit(10000));
    }

    @Test
    public void test_getbit() throws UnsupportedEncodingException {
        ByteString byteString = new ByteString(BYTE);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 80; i++)
            sb.append(byteString.getBit(i) ? "1" : "0");

        assertEquals(BIT, sb.toString());

        // Verify beyond init range as well.
        assertFalse(byteString.getBit(10000));
    }

    @Test
    public void test_setbit_set_zero_offset()
            throws UnsupportedEncodingException {
        ByteString byteString = new ByteString(BYTE);
        assertFalse(byteString.getBit(0));

        assertFalse(byteString.setBit(0, true));
        assertTrue(byteString.getBit(0));

        assertTrue(byteString.setBit(0, true));
        assertTrue(byteString.getBit(0));

        assertTrue(byteString.setBit(0, false));
        assertFalse(byteString.getBit(0));

    }

    @Test
    public void test_setbit_set_small_offsets() {
        ByteString byteString = new ByteString(BYTE);
        assertTrue(byteString.getBit(1));

        assertTrue(byteString.setBit(1, true));
        assertTrue(byteString.getBit(1));

        assertTrue(byteString.setBit(1, false));
        assertFalse(byteString.getBit(1));

        assertFalse(byteString.setBit(1, false));
        assertFalse(byteString.getBit(1));

        assertFalse(byteString.setBit(1, true));
        assertTrue(byteString.getBit(1));

        assertFalse(byteString.setBit(10, false));
        assertFalse(byteString.getBit(10));

        assertFalse(byteString.setBit(10, true));
        assertTrue(byteString.getBit(10));

        assertTrue(byteString.setBit(10, false));
        assertFalse(byteString.getBit(10));

        assertTrue(byteString.setBit(22, false));
        assertFalse(byteString.getBit(22));

        assertTrue(byteString.setBit(51, false));
        assertFalse(byteString.getBit(51));

        assertFalse(byteString.setBit(51, true));
        assertTrue(byteString.getBit(51));

    }

    @Test
    public void test_setbit_large_offset() throws UnsupportedEncodingException {
        ByteString byteString = new ByteString(BYTE);
        byteString.setBit(545, true);
        assertTrue(byteString.getBit(545));

        // Anything near to it should be false.
        for (int i = 500; i < 545; i++)
            assertFalse(byteString.getBit(i));
    }

    @Test
    public void test_setbit_max_offset() throws UnsupportedEncodingException {
        ByteString byteString = new ByteString(BYTE);

        byteString.setBit(Integer.MAX_VALUE, true);
        assertTrue(byteString.getBit(Integer.MAX_VALUE));

        byteString.setBit(Integer.MAX_VALUE, false);
        assertFalse(byteString.getBit(Integer.MAX_VALUE));
    }

}
