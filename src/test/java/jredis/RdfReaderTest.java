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

public class RdfReaderTest {
    
    private static final byte[] NUMBERS = {
       0x10, //len 
       0x10, 0x00, 0x00, 0x00, //zlbytes
       0x0e, 0x00, 0x00, 0x00, //zltail
       0x02, 0x00, //zllen
       
       // e1
       0x00, // prev len
       0x03, // spl flag
       0x4f, 0x6e, 0x65, // raw 'One'
       
       // e2
       0x06, // prev len
       (byte) 0xf9, // special flag
       
       (byte) 0xff // end
    };

    private static final byte[] NUMBERS_STRING_SCORE = {
        0x1c, //len 
        0x1c, 0x00, 0x00, 0x00, //zlbytes 4
        0x14, 0x00, 0x00, 0x00, //zltail 4
        0x02, 0x00, //zllen 2
        
        // e1
        0x00, // prev len 1
        0x08, // spl flag 1
        0x46, 0x6f, 0x75, 0x72, 0x74, 0x69, 0x65, 0x73,  // raw 'Fourties' 8
        
        // e2
        0x06, // prev len 1
        (byte) 0x05, // special flag 1
        0x34, 0x37, 0x2e, 0x33, 0x32, // raw '47.32' 5
        
        (byte) 0xff // end 1
     };

    private static final byte[] NUMBERS_INVALID_STRING_SCORE = {
        0x1c, //len 
        0x1c, 0x00, 0x00, 0x00, //zlbytes 4
        0x14, 0x00, 0x00, 0x00, //zltail 4
        0x02, 0x00, //zllen 2
        
        // e1
        0x00, // prev len 1
        0x08, // spl flag 1
        0x46, 0x6f, 0x75, 0x72, 0x74, 0x69, 0x65, 0x73,  // raw 'Fourties' 8
        
        // e2
        0x06, // prev len 1
        (byte) 0x05, // special flag 1
        0x34, 0x37, 0x2e, 0x6d, 0x32, // raw '47.m2' 5
        
        (byte) 0xff // end 1
     };

    private static final byte[] THREE_ENTRIES = {
        0x26, //len 
        0x26, 0x00, 0x00, 0x00, //zlbytes 4
        0x14, 0x00, 0x00, 0x00, //zltail 4
        0x03, 0x00, //zllen 2
        
        // e1
        0x00, // prev len 1
        0x08, // spl flag 1
        0x46, 0x6f, 0x75, 0x72, 0x74, 0x69, 0x65, 0x73,  // raw 'Fourties' 8
        
        // e2
        0x06, // prev len 1
        (byte) 0x05, // special flag 1
        0x34, 0x37, 0x2e, 0x6d, 0x32, // raw '47.m2' 5

        // e3
        0x00, // prev len 1
        0x08, // spl flag 1
        0x46, 0x6f, 0x75, 0x72, 0x74, 0x69, 0x65, 0x46,  // raw 'FourtieF' 8

        (byte) 0xff // end 1
     };
    
    private static final byte[] NUMBERS_NO_END = {
        0x0f, //len 
        0x0f, 0x00, 0x00, 0x00, //zlbytes
        0x0e, 0x00, 0x00, 0x00, //zltail
        0x02, 0x00, //zllen
        
        // e1
        0x00, // prev len
        0x03, // spl flag
        0x4f, 0x6e, 0x65, // raw 'One'
        
        // e2
        0x06, // prev len
        (byte) 0xf9, // special flag
     };


    private static final byte[] NUMBERS_LARGE_KEY_START = {
        0x1c, //len 
        0x58, 0x06, 0x00, 0x00, //zlbytes 4
        0x14, 0x00, 0x00, 0x00, //zltail 4
        0x02, 0x00, //zllen 2
        
        // e1
        0x00, // prev len 1
        (byte) 0x46, 0x40, // special flag 2
    };
    
    private static final byte[] NUMBERS_LARGE_KEY = { 0x46, 0x6f, 0x75, 0x72, 0x74, 0x69, 0x65, 0x73 }; // raw 'Fourties' 1600
        
    private static final byte[] NUMBERS_LARGE_KEY_END = {
        // e2
        (byte) 0xfe, // prev len 1
        0x43, 0x06, 0x00, 0x00, // prev len 4
        (byte) 0xd0, // special flag 1
        ~0x0f, ~(byte) 0xa9, ~0x53, ~0x42, // -1112779024 signed integer 4
        
        (byte) 0xff // end 1
     };

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
        RdfReader reader = new RdfReader(new ByteArrayInputStream(NUMBERS_STRING_SCORE));

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
    public void tesst_load_invalid_string() throws IOException, InvalidFileFormat {
        RdfReader reader = new RdfReader(new ByteArrayInputStream(NUMBERS_INVALID_STRING_SCORE));
        reader.readElementSet();
    }

    @Test
    public void test_load_large_prev() throws IOException, InvalidFileFormat {
        
        byte[] key = {};
        for(int i=0; i<200; i++)
            key = c(key, NUMBERS_LARGE_KEY);
        
        byte[] input = c(NUMBERS_LARGE_KEY_START, key, NUMBERS_LARGE_KEY_END);
        
        RdfReader reader = new RdfReader(new ByteArrayInputStream(input));

        ElementSet elementSet = reader.readElementSet();
        assertNotNull(elementSet);

        Iterator<Element> iter = elementSet.iterator();
        assertTrue(iter.hasNext());
        Element elem = iter.next();
        
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<200; i++)
            sb.append("Fourties");

        
        assertEquals(sb.toString(), elem.getMember());
        assertEquals(new Double(-1112779024), elem.getScore());

        assertFalse(iter.hasNext());

    }

    @Test(expected = InvalidFileFormat.class)
    public void test_read_odd_entries() throws IOException, InvalidFileFormat {
        RdfReader reader = new RdfReader(new ByteArrayInputStream(THREE_ENTRIES));
        reader.readElementSet();
    }

    @Test(expected = InvalidFileFormat.class)
    public void test_no_end() throws IOException, InvalidFileFormat {
        RdfReader reader = new RdfReader(new ByteArrayInputStream(NUMBERS_NO_END));
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
     * 
     */

}
