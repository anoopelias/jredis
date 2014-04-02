package jredis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static jredis.TestUtil.c;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Iterator;

import jredis.exception.InvalidFileFormat;

import org.junit.Before;
import org.junit.Test;

public class LoaderTest {

    /**
     * Represents the value, 'REDIS0006' FE 00
     */
    private static final byte[] INIT = { 0x52, 0x45, 0x44, 0x49, 0x53, 0x30,
            0x30, 0x30, 0x36, (byte) 0xfe, 0x00 };
    private static final byte[] INIT_INVALID = { 0x52, 0x45, 0x44, 0x49, 0x53,
            0x30, 0x30, 0x30, 0x36, (byte) 0xfd, 0x00 };

    private static final byte[] STRING_INIT = { 0x00, 0x05, 'R', 'A', 'L', 'P',
            'H' };

    private static final byte[] STRING_VAL = { 0x06, 'F', 'I', 'N', 'N', 'E',
            'S' };

    private static final String LONG_STRING = "Ralph Nathaniel Twisleton-Wykeham-Fiennes "
            + "(/ˈreɪf ˈfaɪnz/;[2] born 22 December 1962), is an English actor. "
            + "A noted Shakespeare interpreter, he first achieved success onstage "
            + "at the Royal National Theatre. (Source : Wikipedia)";

    private static final String LONGER_STRING = LONG_STRING
            + "Fiennes' portrayal of Nazi war criminal Amon "
            + "Goeth in Schindler's List (1993) earned him a nomination for the Academy "
            + "Award for Best Supporting Actor and the Golden Globe Award for "
            + "Best Supporting Actor, and won the BAFTA Award for Best Actor in a "
            + "Supporting Role. His performance as Count Almásy in The English Patient "
            + "(1996) garnered him a second Academy Award nomination, for Best Actor, "
            + "as well as BAFTA and Golden Globe nominations.";

    private static final byte[] LONG_STRING_SIZE = { 0x40, (byte) 0xe5 };
    private static final byte[] LONGER_STRING_SIZE = { 0x42, (byte) 0x9b };
    private static final byte[] LONGEST_STRING_SIZE = { (byte) 0x80, 0x00,
            0x00, 0x59, 0x74 };

    private static final byte[] END = { (byte) 0xff };

    @Before
    public void setup() {
        DB.INSTANCE.clear();
    }

    @Test
    public void test_loader_init() throws InvalidFileFormat {
        new Loader(toStream(END)).load();
    }

    @Test(expected = InvalidFileFormat.class)
    public void test_loader_init_error() throws InvalidFileFormat {
        InputStream is = new ByteArrayInputStream(INIT_INVALID);
        new Loader(is).load();
    }

    /*
     * Test cases for loading ByetString.
     */

    @Test
    public void test_loader_string() throws InvalidFileFormat {
        new Loader(toStream(c(STRING_INIT, STRING_VAL))).load();
        TimedByteString val = DB.INSTANCE.get("RALPH",
                TimedByteString.class);
        assertEquals("FINNES", val.value().toString());
        assertTrue(val.isValid());
    }

    @Test(expected = InvalidFileFormat.class)
    public void test_loader_only_init() throws InvalidFileFormat {
        InputStream is = new ByteArrayInputStream(INIT);
        new Loader(is).load();
    }

    @Test(expected = InvalidFileFormat.class)
    public void test_loader_only_string_init() throws InvalidFileFormat {
        InputStream is = new ByteArrayInputStream(c(INIT, STRING_INIT));
        new Loader(is).load();
    }

    @Test(expected = InvalidFileFormat.class)
    public void test_loader_no_eof() throws InvalidFileFormat {
        InputStream is = new ByteArrayInputStream(c(INIT, STRING_INIT,
                STRING_VAL));
        new Loader(is).load();
    }

    @Test(expected = InvalidFileFormat.class)
    public void test_loader_no_length() throws InvalidFileFormat {
        InputStream is = new ByteArrayInputStream(c(INIT, STRING_INIT, END));
        new Loader(is).load();
    }

    @Test(expected = InvalidFileFormat.class)
    public void test_loader_invalid_length() throws InvalidFileFormat {
        byte[] invalidLength = { 0x40 };
        InputStream is = new ByteArrayInputStream(c(INIT, STRING_INIT,
                invalidLength));
        new Loader(is).load();
    }

    @Test
    public void test_loader_string_greater_than_63_bytes()
            throws InvalidFileFormat {
        new Loader(toStream(c(STRING_INIT, LONG_STRING_SIZE,
                Protocol.toBytes(LONG_STRING)))).load();
        TimedByteString val = DB.INSTANCE.get("RALPH",
                TimedByteString.class);
        assertEquals(LONG_STRING, val.value().toString());
    }

    @Test
    public void test_loader_string_greater_than_256_bytes()
            throws InvalidFileFormat {
        new Loader(toStream(c(STRING_INIT, LONGER_STRING_SIZE,
                Protocol.toBytes(LONGER_STRING)))).load();
        TimedByteString val = DB.INSTANCE.get("RALPH",
                TimedByteString.class);
        assertEquals(LONGER_STRING, val.value().toString());
    }

    @Test
    public void test_loader_file_long_string() throws InvalidFileFormat {
        InputStream stream = this.getClass().getClassLoader()
                .getResourceAsStream("dump_long.rdb");
        new Loader(stream).load();
        TimedByteString val = DB.INSTANCE.get("RALPH",
                TimedByteString.class);
        assertEquals(LONG_STRING, val.value().toString());
    }

    @Test
    public void test_loader_file_longest_string() throws InvalidFileFormat {
        InputStream stream = this.getClass().getClassLoader()
                .getResourceAsStream("dump_longest.rdb");
        new Loader(stream).load();
        TimedByteString val = DB.INSTANCE.get("RALPH",
                TimedByteString.class);
        assertEquals(longestString(), val.value().toString());
    }

    @Test
    public void test_loader_string_greater_than_16383_bytes()
            throws InvalidFileFormat {
        String longest = longestString();

        new Loader(toStream(c(STRING_INIT, LONGEST_STRING_SIZE,
                Protocol.toBytes(longest)))).load();
        TimedByteString val = DB.INSTANCE.get("RALPH",
                TimedByteString.class);
        assertEquals(longest.toString(), val.value().toString());
    }

    private String longestString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 100; i++)
            sb.append(LONG_STRING);
        return sb.toString();
    }

    @Test
    public void test_loader_file() throws InvalidFileFormat {
        InputStream stream = this.getClass().getClassLoader()
                .getResourceAsStream("dump.rdb");
        new Loader(stream).load();
        TimedByteString val = DB.INSTANCE.get("Anoop",
                TimedByteString.class);
        assertEquals("Elias", val.value().toString());
    }

    @Test
    public void test_loader_mil_timed_string() throws InvalidFileFormat,
            InterruptedException {
        new Loader(toTimedStream(c(STRING_INIT, STRING_VAL), 100)).load();
        TimedByteString val = DB.INSTANCE.get("RALPH",
                TimedByteString.class);
        assertEquals("FINNES", val.value().toString());
        assertTrue(val.isValid());

        Thread.sleep(101);
        assertFalse(val.isValid());
    }

    @Test(expected = InvalidFileFormat.class)
    public void test_loader_no_type_after_time() throws InvalidFileFormat {
        byte[] time = numToBytes(System.currentTimeMillis() + 100);
        InputStream is = new ByteArrayInputStream(c(INIT, time, END));
        new Loader(is).load();
    }

    @Test
    public void test_loader_invalid_timed_string() throws InvalidFileFormat,
            InterruptedException {
        InputStream stream = toTimedStream(c(STRING_INIT, STRING_VAL), 100);
        Thread.sleep(101);

        new Loader(stream).load();
        TimedByteString val = DB.INSTANCE.get("RALPH",
                TimedByteString.class);
        assertNull(val);
    }

    @Test
    public void test_loader_timer_file() throws InvalidFileFormat {
        InputStream stream = this.getClass().getClassLoader()
                .getResourceAsStream("dump_timer.rdb");
        new Loader(stream).load();
        TimedByteString val = DB.INSTANCE.get("John",
                TimedByteString.class);

        /*
         * Timer would have timed out already. This is a negative test case.
         * Difficult to get a positive test case here. At least this proves, the
         * file can be read without errors.
         */
        assertNull(val);

        // An untimed key already there in the file.
        val = DB.INSTANCE.get("Anoop", TimedByteString.class);
        assertNotNull(val);
    }

    /*
     * Test cases for loading Zset.
     */

    private static final byte[] NUMBERS_INIT = { 0x0c, 0x07, 'N', 'u', 'm',
            'b', 'e', 'r', 's' };

    // 16
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
    public void test_load_value_score_entry() throws InvalidFileFormat {
        InputStream stream = toStream(c(NUMBERS_INIT, NUMBERS_LEN, NUMBERS_ZLBYTES,
                NUMBERS_ZLTAIL, NUMBERS_ZLLEN, NUMBERS_ENTRY1_PREV_LEN,
                NUMBERS_ENTRY1_SPECIAL_FLAG, NUMBERS_ENTRY1_SPECIAL_RAW,
                NUMBERS_ENTRY2_PREV_LEN, NUMBERS_ENTRY2_SPECIAL_FLAG,
                NUMBERS_ENTRY2_SPECIAL_RAW, NUMBERS_ZLEND));
        
        new Loader(stream).load();
        ElementSet val = DB.INSTANCE.get("Numbers",
                ElementSet.class);
        assertNotNull(val);
        
        Iterator<Element> iter = val.iterator();
        assertTrue(iter.hasNext());
        Element elem = iter.next();
        assertEquals("One", elem.getMember());
        assertEquals(new Double(8.0), elem.getScore());

        assertFalse(iter.hasNext());
    }
    
    /*
     * Helper methods.
     */

    private InputStream toTimedStream(byte[] keyValue, long mils) {
        byte[] num = numToBytes(System.currentTimeMillis() + mils);
        byte[] stream = c(INIT, num, keyValue, END);
        return new ByteArrayInputStream(stream);
    }

    private byte[] numToBytes(long time) {
        ByteBuffer bb = ByteBuffer.allocate(9);
        bb.putLong(time);
        bb.put((byte) 0xfc);
        byte[] num = reverse(bb.array());
        return c(num, new byte[9 - num.length]);
    }

    private InputStream toStream(byte[] keyValue) {
        return new ByteArrayInputStream(c(INIT, keyValue, END));
    }

    /**
     * Reverse a byte array.
     * 
     * @param b
     */
    public static byte[] reverse(byte[] b) {
        for (int i = 0; i < b.length / 2; i++) {
            byte temp = b[i];
            b[i] = b[b.length - i - 1];
            b[b.length - i - 1] = temp;
        }

        return b;
    }

    /*
     * TODO : 
     * 1. CRC check. 
     * 
     * 2. Test cases for bit.
     * 
     * 3. Test case to read dump_zset
     */
    
    

}
