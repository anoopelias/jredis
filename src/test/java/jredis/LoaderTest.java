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

    private static final byte[] STRING = { 0x00, 0x05, 'R', 'A', 'L', 'P', 'H',
            0x06, 'F', 'I', 'N', 'N', 'E', 'S' };

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
        new Loader(toStream(STRING)).load();
        TimedString val = DataMap.INSTANCE.get("RALPH", TimedString.class);
        assertEquals("FINNES", val.value());
        assertTrue(val.isValid());
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
        new Loader(toTimedStream(STRING, 100)).load();
        TimedString val = DataMap.INSTANCE.get("RALPH", TimedString.class);
        assertEquals("FINNES", val.value());
        assertTrue(val.isValid());

        Thread.sleep(101);
        assertFalse(val.isValid());
    }

    @Test
    public void test_loader_invalid_timed_string() throws InvalidFileFormat,
            InterruptedException {
        InputStream stream = toTimedStream(STRING, 100);
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
         * Difficult to get a positive test case here. At least this proves, the file
         * can be read without errors.
         */
        assertNull(val);

        // An untimed key already there in the file.
        val = DataMap.INSTANCE.get("Anoop", TimedString.class);
        assertNotNull(val);
    }

    private InputStream toTimedStream(byte[] keyValue, long mils) {
        byte[] num = numToBytes(System.currentTimeMillis() + mils);
        byte[] stream = combine(combine(combine(INIT, num), keyValue), END);
        return new ByteArrayInputStream(stream);
    }

    private byte[] numToBytes(long time) {
        ByteBuffer bb = ByteBuffer.allocate(9);
        bb.putLong(time);
        bb.put((byte) 0xfc);
        byte[] num = reverse(bb.array());
        return combine(num, new byte[9 - num.length]);
    }

    private InputStream toStream(byte[] keyValue) {
        return new ByteArrayInputStream(combine(combine(INIT, keyValue), END));
    }

    private byte[] combine(byte[] b1, byte[] b2) {
        byte[] ret = new byte[b1.length + b2.length];

        for (int i = 0; i < b1.length; i++)
            ret[i] = b1[i];

        for (int i = b1.length; i < ret.length; i++)
            ret[i] = b2[i - b1.length];

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
