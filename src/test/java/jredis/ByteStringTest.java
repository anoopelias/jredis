package jredis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.UnsupportedEncodingException;

import org.junit.Test;

public class ByteStringTest {

    private static final String SOME_STRING = "Mathew Perry";
    private static final byte[] BYTE = { 0x50, 0x51, 0x52, 0x53, 0x54, 0x55,
            0x56, 0x57, 0x58, 0x59 };
    private static final String BIT = "01010000010100010101001001010011010101000101010101010110010101110101100001011001";

    private static final String BYTE_128_STRING = "Lorem ipsum dolor sit amet risus nunc "
            + "nulla ipsum. Ut mattis et. Pellentesque ipsum varius quam aenean dolor "
            + "sem dictumst velit.";

    @Test
    public void test_string() {
        BinaryString byteString = new BinaryString(SOME_STRING);
        assertEquals(12, byteString.toByteArray().length());
        assertEquals(SOME_STRING, byteString.toString());
    }

    @Test
    public void test_bytes() throws UnsupportedEncodingException {
        BinaryString byteString = new BinaryString(
                SOME_STRING.getBytes(Protocol.CHARSET));
        assertEquals(SOME_STRING, byteString.toString());
    }

    @Test
    public void test_get_byte_array_default() throws UnsupportedEncodingException {
        BinaryString byteString = new BinaryString();
        assertEquals(0, byteString.toByteArray().length());
    }

    @Test
    public void test_get_byte_array_bytes() throws UnsupportedEncodingException {
        BinaryString byteString = new BinaryString(BYTE);
        assertEquals(10, byteString.toByteArray().length());
    }

    @Test
    public void test_getbit_null() throws UnsupportedEncodingException {
        BinaryString byteString = new BinaryString();

        assertFalse(byteString.getBit(0));
        assertFalse(byteString.getBit(10));
        assertFalse(byteString.getBit(100));
        assertFalse(byteString.getBit(1000));
        assertFalse(byteString.getBit(10000));
    }

    @Test
    public void test_getbit() throws UnsupportedEncodingException {
        BinaryString byteString = new BinaryString(BYTE);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 80; i++)
            sb.append(byteString.getBit(i) ? "1" : "0");

        assertEquals(BIT, sb.toString());

        // Verify beyond init range as well.
        assertFalse(byteString.getBit(10000));
    }

    @Test
    public void test_getbit_larger_offset() {
        byte[] input = { 0x32 };
        BinaryString byteString = new BinaryString(input);

        assertTrue(byteString.getBit(6));
        assertFalse(byteString.getBit(7));
        assertFalse(byteString.getBit(8));
        assertFalse(byteString.getBit(9));
    }

    @Test
    public void test_setbit_set_zero_offset()
            throws UnsupportedEncodingException {
        BinaryString byteString = new BinaryString(BYTE);
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
        BinaryString byteString = new BinaryString(BYTE);
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
        BinaryString byteString = new BinaryString(BYTE);
        byteString.setBit(545, true);
        assertTrue(byteString.getBit(545));

        // Anything near to it should be false.
        for (int i = 500; i < 545; i++)
            assertFalse(byteString.getBit(i));

        for (int i = 546; i < 600; i++)
            assertFalse(byteString.getBit(i));
    }

    @Test
    public void test_setbit_max_offset() throws UnsupportedEncodingException {
        BinaryString byteString = new BinaryString(BYTE);

        // This will take more memory than usual.
        byteString.setBit(Integer.MAX_VALUE, true);
        assertTrue(byteString.getBit(Integer.MAX_VALUE));

        byteString.setBit(Integer.MAX_VALUE, false);
        assertFalse(byteString.getBit(Integer.MAX_VALUE));
    }

    @Test
    public void test_get_byte_array() throws UnsupportedEncodingException {
        BinaryString byteString = new BinaryString(SOME_STRING);

        assertEquals(12, byteString.toByteArray().length());
        assertEquals(SOME_STRING, byteString.toString());
    }

    @Test
    public void test_setbit_get_byte_array()
            throws UnsupportedEncodingException {
        BinaryString byteString = new BinaryString(BYTE_128_STRING);

        int pos = 8 * 128 + 1;
        byteString.setBit(pos++, true);
        assertEquals(129, byteString.toByteArray().length());

        for(int i=0; i<6; i++) {
            byteString.setBit(pos++, true);
        }

        assertEquals(129, byteString.toByteArray().length());
        byteString.setBit(pos++, true);
        
        assertEquals(130, byteString.toByteArray().length());
    }

}
