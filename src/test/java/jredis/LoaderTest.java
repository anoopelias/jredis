package jredis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;

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

    private static final String LONGER_STRING = LONG_STRING + "Fiennes' portrayal of Nazi war criminal Amon "
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
        DataMap.INSTANCE.clear();
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

    @Test
    public void test_loader_string() throws InvalidFileFormat {
        new Loader(toStream(c(STRING_INIT, STRING_VAL))).load();
        TimedString val = DataMap.INSTANCE.get("RALPH", TimedString.class);
        assertEquals("FINNES", val.value());
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
                LONG_STRING.getBytes()))).load();
        TimedString val = DataMap.INSTANCE.get("RALPH", TimedString.class);
        assertEquals(LONG_STRING, val.value());
    }

    @Test
    public void test_loader_string_greater_than_256_bytes()
            throws InvalidFileFormat {
        new Loader(toStream(c(STRING_INIT, LONGER_STRING_SIZE,
                LONGER_STRING.getBytes()))).load();
        TimedString val = DataMap.INSTANCE.get("RALPH", TimedString.class);
        assertEquals(LONGER_STRING, val.value());
    }

    @Test
    public void test_loader_string_greater_than_16383_bytes()
            throws InvalidFileFormat {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 100; i++)
            sb.append(LONG_STRING);

        new Loader(toStream(c(STRING_INIT, LONGEST_STRING_SIZE, sb.toString()
                .getBytes()))).load();
        TimedString val = DataMap.INSTANCE.get("RALPH", TimedString.class);
        assertEquals(sb.toString(), val.value());
    }

    @Test
    public void test_loader_file() throws InvalidFileFormat {
        InputStream stream = this.getClass().getClassLoader()
                .getResourceAsStream("dump.rdb");
        new Loader(stream).load();
        TimedString val = DataMap.INSTANCE.get("Anoop", TimedString.class);
        assertEquals("Elias", val.value());
    }

    @Test
    public void test_loader_mil_timed_string() throws InvalidFileFormat,
            InterruptedException {
        new Loader(toTimedStream(c(STRING_INIT, STRING_VAL), 100)).load();
        TimedString val = DataMap.INSTANCE.get("RALPH", TimedString.class);
        assertEquals("FINNES", val.value());
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
        TimedString val = DataMap.INSTANCE.get("RALPH", TimedString.class);
        assertNull(val);
    }

    @Test
    public void test_loader_timer_file() throws InvalidFileFormat {
        InputStream stream = this.getClass().getClassLoader()
                .getResourceAsStream("dump_timer.rdb");
        new Loader(stream).load();
        TimedString val = DataMap.INSTANCE.get("John", TimedString.class);

        /*
         * Timer would have timed out already. This is a negative test case.
         * Difficult to get a positive test case here. At least this proves, the
         * file can be read without errors.
         */
        assertNull(val);

        // An untimed key already there in the file.
        val = DataMap.INSTANCE.get("Anoop", TimedString.class);
        assertNotNull(val);
    }

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

    private byte[] c(byte[]... byt) {
        int size = 0;
        for (byte[] by : byt)
            size += by.length;

        byte[] ret = new byte[size];
        int index = 0;
        for (int i = 0; i < byt.length; i++) {
            for (int j = 0; j < byt[i].length; j++) {
                ret[index++] = byt[i][j];
            }
        }

        return ret;
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
     * TODO : CRC check.
     */

}
