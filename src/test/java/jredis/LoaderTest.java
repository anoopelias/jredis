package jredis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import jredis.exception.InvalidFileFormat;

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

    private InputStream toStream(byte[] keyValue) {
        int len = INIT.length + keyValue.length + END.length;
        byte[] stream = new byte[len];

        for (int i = 0; i < INIT.length; i++)
            stream[i] = INIT[i];

        for (int i = INIT.length; i < INIT.length + keyValue.length; i++)
            stream[i] = keyValue[i - INIT.length];

        for (int i = INIT.length + keyValue.length; i < len; i++)
            stream[i] = END[i - (INIT.length + keyValue.length)];

        return new ByteArrayInputStream(stream);
    }
    
    /*
     * TODO : CRC check.
     * 
     */

}
