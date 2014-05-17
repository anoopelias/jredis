package jredis;

import static jredis.RdfTestUtil.NUMBERS_STRING_SCORE;
import static jredis.RdfTestUtil.NUMBERS;
import static jredis.RdfTestUtil.largeMember;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import jredis.domain.BinaryString;
import jredis.domain.ByteArray;
import jredis.domain.Element;
import jredis.domain.ElementSet;
import jredis.domain.TreeElementSet;

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
        sw.write(new BinaryString("A"));
        byte[] by = baos.toByteArray();

        assertEquals(2, by.length);
        assertEquals(0x01, by[0]);
        assertEquals(0x41, by[1]);
    }

    @Test
    public void test_write_byte_string_under_63_char() throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        RdfWriter sw = new RdfWriter(baos);
        sw.write(new BinaryString(LOREM_IPSUM_UNDER_63));
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
        sw.write(new BinaryString(LOREM_IPSUM_UNDER_256));
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
        sw.write(new BinaryString(LOREM_IPSUM_UNDER_16383));
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
        sw.write(new BinaryString(sb.toString()));
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
    public void test_write_element_set_string_score() throws IOException {
        ElementSet elementSet = new TreeElementSet();
        Element e = new Element("Fourties", 47.32);
        elementSet.insert(e);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        RdfWriter sw = new RdfWriter(baos);
        sw.write(elementSet);
        byte[] by = baos.toByteArray();

        assertArrayEquals(NUMBERS_STRING_SCORE, by);
    }

    @Test
    public void test_write_element_set_score_under_12() throws IOException {
        ElementSet elementSet = new TreeElementSet();
        Element e = new Element("One", 8.0);
        elementSet.insert(e);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        RdfWriter sw = new RdfWriter(baos);
        sw.write(elementSet);
        byte[] by = baos.toByteArray();

        assertArrayEquals(NUMBERS, by);
    }

    @Test
    public void test_write_large_member() throws IOException {
        
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 200; i++)
            sb.append("Fourties");

        ElementSet elementSet = new TreeElementSet();
        Element e = new Element(sb.toString(), new Double(-1112779024));
        elementSet.insert(e);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        RdfWriter sw = new RdfWriter(baos);
        sw.write(elementSet);

        assertArrayEquals(largeMember(), baos.toByteArray());
    }

    @Test
    public void test_int_less_than_byte() {
        ByteArray ba = RdfWriter.toBytes(25);
        assertEquals(new ByteArray(new byte[] { (byte) 0xfe, 0x19 }), ba);
    }

    @Test
    public void test_int_less_than_byte_negative() {
        ByteArray ba = RdfWriter.toBytes(-25);
        assertEquals(new ByteArray(new byte[] { (byte) 0xfe, (byte) 0xe7 }), ba);
    }

    @Test
    public void test_int_less_than_2_bytes() {
        ByteArray ba = RdfWriter.toBytes(187);
        assertEquals(new ByteArray(
                new byte[] { (byte) 0xc0, (byte) 0xbb, 0x00 }), ba);
    }

    @Test
    public void test_int_less_than_2_bytes_large() {
        ByteArray ba = RdfWriter.toBytes(9876);
        assertEquals(new ByteArray(
                new byte[] { (byte) 0xc0, (byte) 0x94, 0x26 }), ba);
    }

    @Test
    public void test_int_less_than_2_bytes_large_negative() {
        ByteArray ba = RdfWriter.toBytes(-9876);
        assertEquals(new ByteArray(new byte[] { (byte) 0xc0, (byte) 0x6c,
                (byte) 0xd9 }), ba);
    }

    @Test
    public void test_int_less_than_3_bytes() {
        ByteArray ba = RdfWriter.toBytes(56789);
        assertEquals(new ByteArray(new byte[] { (byte) 0xf0, (byte) 0xd5,
                (byte) 0xdd, 0x00 }), ba);
    }

    @Test
    public void test_int_less_than_3_bytes_negative() {
        ByteArray ba = RdfWriter.toBytes(-56789);
        assertEquals(new ByteArray(new byte[] { (byte) 0xf0, (byte) 0x2b,
                (byte) 0x22, (byte) 0xff }), ba);
    }

    @Test
    public void test_int_less_than_4_bytes() {
        ByteArray ba = RdfWriter.toBytes(10389000);
        assertEquals(new ByteArray(new byte[] { (byte) 0xd0, (byte) 0x08,
                (byte) 0x86, (byte) 0x9e, 0x00 }), ba);
    }

    @Test
    public void test_8_bytes() {
        ByteArray ba = RdfWriter.toBytes(435519079242l);
        assertEquals(new ByteArray(new byte[] { (byte) 0xe0, (byte) 0x4a,
                (byte) 0xc3, (byte) 0xf5, 0x66, 0x65, 0x00, 0x00, 0x00 }), ba);
    }

}
