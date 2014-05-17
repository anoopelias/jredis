package jredis;

import static jredis.RdfTestUtil.NUMBERS;
import static jredis.RdfTestUtil.NUMBERS_INVALID_STRING_SCORE;
import static jredis.RdfTestUtil.NUMBERS_NO_END;
import static jredis.RdfTestUtil.NUMBERS_STRING_SCORE;
import static jredis.RdfTestUtil.THREE_ENTRIES;
import static jredis.RdfTestUtil.largeMember;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Iterator;

import jredis.domain.Element;
import jredis.domain.ElementSet;
import jredis.exception.InvalidFileFormat;

import org.junit.Test;

public class RdfReaderTest {

    @Test
    public void test_load_integer() throws IOException, InvalidFileFormat {
        RdfReader reader = new RdfReader(new ByteArrayInputStream(NUMBERS));

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
    public void test_load_string() throws IOException, InvalidFileFormat {
        RdfReader reader = new RdfReader(new ByteArrayInputStream(
                NUMBERS_STRING_SCORE));

        ElementSet elementSet = reader.readElementSet();
        assertNotNull(elementSet);

        Iterator<Element> iter = elementSet.iterator();
        assertTrue(iter.hasNext());
        Element elem = iter.next();
        assertEquals("Fourties", elem.getMember());
        assertEquals(new Double(47.32), elem.getScore());

        assertFalse(iter.hasNext());

    }

    @Test(expected = InvalidFileFormat.class)
    public void tesst_load_invalid_string() throws IOException,
            InvalidFileFormat {
        RdfReader reader = new RdfReader(new ByteArrayInputStream(
                NUMBERS_INVALID_STRING_SCORE));
        reader.readElementSet();
    }

    @Test
    public void test_load_large_prev() throws IOException, InvalidFileFormat {

        byte[] input = largeMember();

        RdfReader reader = new RdfReader(new ByteArrayInputStream(input));

        ElementSet elementSet = reader.readElementSet();
        assertNotNull(elementSet);

        Iterator<Element> iter = elementSet.iterator();
        assertTrue(iter.hasNext());
        Element elem = iter.next();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 200; i++)
            sb.append("Fourties");

        assertEquals(sb.toString(), elem.getMember());
        assertEquals(new Double(-1112779024), elem.getScore());

        assertFalse(iter.hasNext());

    }

    @Test(expected = InvalidFileFormat.class)
    public void test_read_odd_entries() throws IOException, InvalidFileFormat {
        RdfReader reader = new RdfReader(
                new ByteArrayInputStream(THREE_ENTRIES));
        reader.readElementSet();
    }

    @Test(expected = InvalidFileFormat.class)
    public void test_no_end() throws IOException, InvalidFileFormat {
        RdfReader reader = new RdfReader(new ByteArrayInputStream(
                NUMBERS_NO_END));
        reader.readElementSet();
    }

    @Test
    public void test_read_short() throws IOException, InvalidFileFormat {
        byte[] str = { 0x15, 0x42 };
        RdfReader reader = new RdfReader(new ByteArrayInputStream(str));
        assertEquals(16917, reader.readNumber(0xc5));
    }

    @Test
    public void test_read_short_negative() throws IOException,
            InvalidFileFormat {
        byte[] str = { ~0x15, ~0x42 };
        RdfReader reader = new RdfReader(new ByteArrayInputStream(str));
        assertEquals(-16918, reader.readNumber(0xc5));
    }

    @Test
    public void test_read_int() throws IOException, InvalidFileFormat {
        byte[] str = { 0x0f, (byte) 0xa9, 0x53, 0x42 };
        RdfReader reader = new RdfReader(new ByteArrayInputStream(str));
        assertEquals(1112779023, reader.readNumber(0xd5));
    }

    @Test
    public void test_read_int_negative() throws IOException, InvalidFileFormat {
        byte[] str = { ~0x0f, ~(byte) 0xa9, ~0x53, ~0x42 };
        RdfReader reader = new RdfReader(new ByteArrayInputStream(str));
        assertEquals(-1112779024, reader.readNumber(0xd5));
    }

    @Test
    public void test_read_long() throws IOException, InvalidFileFormat {
        byte[] str = { (byte) 0x17, (byte) 0x89, 0x27, (byte) 0x85, 0x17,
                (byte) 0x91, 0x29, 0x40 };
        RdfReader reader = new RdfReader(new ByteArrayInputStream(str));
        assertEquals(4623386022676760855L, reader.readNumber(0xe5));
    }

    @Test
    public void test_read_long_negative() throws IOException, InvalidFileFormat {
        byte[] str = { ~(byte) 0xa7, ~(byte) 0xe9, ~0x27, ~(byte) 0x85, ~0x17,
                ~(byte) 0x91, ~0x29, ~0x40 };
        RdfReader reader = new RdfReader(new ByteArrayInputStream(str));
        assertEquals(-4623386022676785576L, reader.readNumber(0xe5));
    }

    @Test
    public void test_read_3bytes() throws IOException, InvalidFileFormat {
        byte[] str = { (byte) 0xa7, (byte) 0xe9, 0x40 };
        RdfReader reader = new RdfReader(new ByteArrayInputStream(str));
        assertEquals(4254119, reader.readNumber(0xF0));
    }

    @Test
    public void test_read_3bytes_negative() throws IOException,
            InvalidFileFormat {
        byte[] str = { ~(byte) 0xa7, ~(byte) 0xE9, ~0x40 };

        RdfReader reader = new RdfReader(new ByteArrayInputStream(str));
        assertEquals(-4254120, reader.readNumber(0xf0));
    }

    @Test
    public void test_read_1byte() throws IOException, InvalidFileFormat {
        byte[] str = { 0x15 };

        RdfReader reader = new RdfReader(new ByteArrayInputStream(str));
        assertEquals(21, reader.readNumber(0xfe));
    }

    @Test
    public void test_read_1byte_negative() throws IOException,
            InvalidFileFormat {
        byte[] str = { (byte) 0xff };

        RdfReader reader = new RdfReader(new ByteArrayInputStream(str));
        assertEquals(-1, reader.readNumber(0xfe));
    }

    @Test
    public void test_read_inline_half_byte() throws IOException,
            InvalidFileFormat {
        byte[] str = {};

        RdfReader reader = new RdfReader(new ByteArrayInputStream(str));
        assertEquals(9, reader.readNumber(0xfa));
    }

    /*
     * TODO : Other test cases, 1. zlbytes do not represent the actual length
     * 
     * 2. zltail do not represent the tail entry.
     * 
     * 4. Invalid previous length.
     */

}
