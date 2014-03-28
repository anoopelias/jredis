package jredis;

import static jredis.TestUtil.c;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Iterator;

import jredis.exception.InvalidFileFormat;

import org.junit.Test;

public class StreamReaderTest {

    private static final byte[] NUMBERS_LEN = { 0x10 };

    // 16
    private static final byte[] NUMBERS_ZLBYTES = { 0x10, 0x00, 0x00, 0x00 }; // 4
                                                                              // bytes

    // Zero as this is the tail
    private static final byte[] NUMBERS_ZLTAIL = { 0x0e, 0x00, 0x00, 0x00 }; // 4
                                                                             // bytes

    // Two entries
    private static final byte[] NUMBERS_ZLLEN = { 0x02, 0x00 }; // 2 bytes

    // Entry 1 - Prev len = 0
    private static final byte[] NUMBERS_ENTRY1_PREV_LEN = { 0x00 };

    // Entry 1 - Special flag - String of 3 chars
    private static final byte[] NUMBERS_ENTRY1_SPECIAL_FLAG = { 0x03 };

    // Entry 1 - Raw 'One'
    private static final byte[] NUMBERS_ENTRY1_SPECIAL_RAW = { 0x4f, 0x6e, 0x65 };

    // Entry 2 - Prev len = 6
    private static final byte[] NUMBERS_ENTRY2_PREV_LEN = { 0x06 };

    // Entry 2 - Special flag - integer between 0 to 12 (8)
    private static final byte[] NUMBERS_ENTRY2_SPECIAL_FLAG = { (byte) 0xf9 };

    // Entry 2 - Raw nothing.
    private static final byte[] NUMBERS_ENTRY2_SPECIAL_RAW = {};

    private static final byte[] NUMBERS_ZLEND = { (byte) 0xff }; // 1 byte

    @Test
    public void test_load_integer() throws IOException, InvalidFileFormat {
        byte[] str = c(NUMBERS_LEN, NUMBERS_ZLBYTES, NUMBERS_ZLTAIL,
                NUMBERS_ZLLEN,

                NUMBERS_ENTRY1_PREV_LEN, NUMBERS_ENTRY1_SPECIAL_FLAG,
                NUMBERS_ENTRY1_SPECIAL_RAW,

                NUMBERS_ENTRY2_PREV_LEN, NUMBERS_ENTRY2_SPECIAL_FLAG,
                NUMBERS_ENTRY2_SPECIAL_RAW,

                NUMBERS_ZLEND);

        StreamReader reader = new StreamReader(new ByteArrayInputStream(str));

        ElementSet elementSet = reader.readElementSet();
        assertNotNull(elementSet);

        Iterator<Element> iter = elementSet.iterator();
        assertTrue(iter.hasNext());
        Element elem = iter.next();
        assertEquals("One", elem.getMember());
        assertEquals(new Double(8.0), elem.getScore());

        assertFalse(iter.hasNext());

    }

    @Test
    public void test_read_short() throws IOException, InvalidFileFormat {
        byte[] str = { 0x15, 0x42 };
        StreamReader reader = new StreamReader(new ByteArrayInputStream(str));
        assertEquals(16917, reader.readNumber(0xc5));
    }

    @Test
    public void test_read_short_negative() throws IOException,
            InvalidFileFormat {
        byte[] str = { ~0x15, ~0x42 };
        StreamReader reader = new StreamReader(new ByteArrayInputStream(str));
        assertEquals(-16918, reader.readNumber(0xc5));
    }

    @Test
    public void test_read_int() throws IOException, InvalidFileFormat {
        byte[] str = { 0x0f, (byte) 0xa9, 0x53, 0x42 };
        StreamReader reader = new StreamReader(new ByteArrayInputStream(str));
        assertEquals(1112779023, reader.readNumber(0xd5));
    }

    @Test
    public void test_read_int_negative() throws IOException, InvalidFileFormat {
        byte[] str = { ~0x0f, ~(byte) 0xa9, ~0x53, ~0x42 };
        StreamReader reader = new StreamReader(new ByteArrayInputStream(str));
        assertEquals(-1112779024, reader.readNumber(0xd5));
    }

    @Test
    public void test_read_long() throws IOException, InvalidFileFormat {
        byte[] str = { (byte) 0x17, (byte) 0x89, 0x27, (byte) 0x85, 0x17,
                (byte) 0x91, 0x29, 0x40 };
        StreamReader reader = new StreamReader(new ByteArrayInputStream(str));
        assertEquals(4623386022676760855L, reader.readNumber(0xe5));
    }

    @Test
    public void test_read_long_negative() throws IOException, InvalidFileFormat {
        byte[] str = { ~(byte) 0xa7, ~(byte) 0xe9, ~0x27, ~(byte) 0x85, ~0x17,
                ~(byte) 0x91, ~0x29, ~0x40 };
        StreamReader reader = new StreamReader(new ByteArrayInputStream(str));
        assertEquals(-4623386022676785576L, reader.readNumber(0xe5));
    }

    @Test
    public void test_read_3bytes() throws IOException, InvalidFileFormat {
        byte[] str = { (byte) 0xa7, (byte) 0xe9, 0x40 };
        StreamReader reader = new StreamReader(new ByteArrayInputStream(str));
        assertEquals(4254119, reader.readNumber(0xF0));
    }

    @Test
    public void test_read_3bytes_negative() throws IOException,
            InvalidFileFormat {
        byte[] str = { ~(byte) 0xa7, ~(byte) 0xE9, ~0x40 };

        StreamReader reader = new StreamReader(new ByteArrayInputStream(str));
        assertEquals(-4254120, reader.readNumber(0xf0));
    }

    @Test
    public void test_read_1byte() throws IOException, InvalidFileFormat {
        byte[] str = { 0x15 };

        StreamReader reader = new StreamReader(new ByteArrayInputStream(str));
        assertEquals(21, reader.readNumber(0xfe));
    }

    @Test
    public void test_read_1byte_negative() throws IOException,
            InvalidFileFormat {
        byte[] str = { (byte) 0xff };

        StreamReader reader = new StreamReader(new ByteArrayInputStream(str));
        assertEquals(-1, reader.readNumber(0xfe));
    }

    @Test
    public void test_read_inline_half_byte() throws IOException,
            InvalidFileFormat {
        byte[] str = {};

        StreamReader reader = new StreamReader(new ByteArrayInputStream(str));
        assertEquals(9, reader.readNumber(0xfa));
    }

    /*
     * TODO : Other test cases, 1. zlbytes do not represent the actual length 2.
     * zltail do not represent the tail entry. 3. Doesn't end in ff. 4. Previous
     * entry length greater than 254. 5. Invalid previous length. 6. Odd number
     * of entries. 8. Negative numbers.
     */

}
