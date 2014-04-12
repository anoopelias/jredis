package jredis;

import static jredis.RdfTestUtil.NUMBERS_STRING_SCORE;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.Test;

public class RdfWriterTest {

    private static final String LOREM_IPSUM_UNDER_63 = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.";
    private static final String LOREM_IPSUM_UNDER_256 = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. "
            + "Integer rutrum odio quis tortor sodales, in rutrum tortor gravida. "
            + "Proin feugiat porttitor nunc nec consequat. "
            + "Nulla aliquam consequat lorem, vitae luctus dolor gravida ut.";

    private static final String LOREM_IPSUM_UNDER_16383 = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. "
            + "Integer rutrum odio quis tortor sodales, in rutrum tortor gravida. "
            + "Proin feugiat porttitor nunc nec consequat. "
            + "Nulla aliquam consequat lorem, vitae luctus dolor gravida ut. "
            + "Pellentesque ornare sem a consectetur auctor. ";

    @Test
    public void test_write_byte_string_1_char() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        RdfWriter sw = new RdfWriter(baos);
        sw.write(new ByteString("A"));
        byte[] by = baos.toByteArray();

        assertEquals(2, by.length);
        assertEquals(0x01, by[0]);
        assertEquals(0x41, by[1]);
    }

    @Test
    public void test_write_byte_string_under_63_char() throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        RdfWriter sw = new RdfWriter(baos);
        sw.write(new ByteString(LOREM_IPSUM_UNDER_63));
        byte[] by = baos.toByteArray();

        assertEquals(57, by.length);
        assertEquals(0x38, by[0]);

        byte[] ex = TestUtil.toBytes(LOREM_IPSUM_UNDER_63);
        for (int i = 0; i < ex.length; i++)
            assertEquals(ex[i], by[i + 1]);
    }

    @Test
    public void test_write_byte_string_under_256_char() throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        RdfWriter sw = new RdfWriter(baos);
        sw.write(new ByteString(LOREM_IPSUM_UNDER_256));
        byte[] by = baos.toByteArray();

        assertEquals(231, by.length);
        assertEquals(0x40, by[0]);
        assertEquals((byte) 0xe5, by[1]);

        byte[] ex = TestUtil.toBytes(LOREM_IPSUM_UNDER_256);
        for (int i = 0; i < ex.length; i++)
            assertEquals(ex[i], by[i + 2]);
    }

    @Test
    public void test_write_byte_string_under_16383_char() throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        RdfWriter sw = new RdfWriter(baos);
        sw.write(new ByteString(LOREM_IPSUM_UNDER_16383));
        byte[] by = baos.toByteArray();

        assertEquals(278, by.length);
        assertEquals(0x41, by[0]);
        assertEquals(0x14, by[1]);

        byte[] ex = TestUtil.toBytes(LOREM_IPSUM_UNDER_16383);
        for (int i = 0; i < ex.length; i++)
            assertEquals(ex[i], by[i + 2]);
    }

    @Test
    public void test_write_byte_string_above_16383_char() throws IOException {

        int size = 16384;

        String sb = build(size);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        RdfWriter sw = new RdfWriter(baos);
        sw.write(new ByteString(sb.toString()));
        byte[] by = baos.toByteArray();

        assertEquals(16389, by.length);
        assertEquals(0x80, by[0] & 0xc0);
        assertEquals(0x00, by[1]);
        assertEquals(0x00, by[2]);
        assertEquals(0x40, by[3]);
        assertEquals(0x00, by[4]);

        for (int i = 0; i < size; i++)
            assertEquals('A', by[i + 5]);
    }

    private String build(long size) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++)
            sb.append('A');
        return sb.toString();
    }
    
    @Test
    public void test_write_element_set() throws IOException {
        ElementSet elementSet = new TreeElementSet();
        Element e = new Element("Fourties", 47.32);
        elementSet.insert(e);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        RdfWriter sw = new RdfWriter(baos);
        sw.write(elementSet);
        byte[] by = baos.toByteArray();
        
        assertArrayEquals(NUMBERS_STRING_SCORE, by);
    }
}
