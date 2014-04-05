package jredis;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.Test;

public class StreamWriterTest {

    private static final String LOREM_IPSUM_UNDER_63 = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.";

    @Test
    public void test_write_byte_string_1_char() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        StreamWriter sw = new StreamWriter(baos);
        sw.write(new ByteString("A"));
        byte[] by = baos.toByteArray();

        assertEquals(2, by.length);
        assertEquals(0x01, by[0]);
        assertEquals(0x41, by[1]);
    }

    @Test
    public void test_write_byte_string_under_63_char() throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        StreamWriter sw = new StreamWriter(baos);
        sw.write(new ByteString(LOREM_IPSUM_UNDER_63));
        byte[] by = baos.toByteArray();

        assertEquals(57, by.length);
        assertEquals(0x38, by[0]);
        
        byte[] ex = TestUtil.toBytes(LOREM_IPSUM_UNDER_63);
        for (int i = 0; i < ex.length; i++)
            assertEquals(ex[i], by[i + 1]);
    }

}
