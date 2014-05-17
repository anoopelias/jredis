package jredis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Iterator;

import jredis.exception.InvalidFileFormat;

import org.junit.Before;
import org.junit.Test;

public class RdfSaverLoaderTest {

    @Before
    public void setup() {
        DB.INSTANCE.clear();
    }

    @Test
    public void test_save_load_string() throws InvalidFileFormat {
        DB.INSTANCE.put("John", new TimedByteString(new BinaryString("Nash")));

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Saver saver = new Saver(os);
        saver.save();

        DB.INSTANCE.clear();
        assertNull(DB.INSTANCE.get("John", TimedByteString.class));

        InputStream is = new ByteArrayInputStream(os.toByteArray());
        Loader loader = new Loader(is);
        loader.load();

        TimedByteString john = DB.INSTANCE.get("John", TimedByteString.class);
        assertEquals("Nash", john.value().toString());
    }

    @Test
    public void test_save_load_timed_string() throws InvalidFileFormat,
            InterruptedException {
        DB.INSTANCE.put("John", new TimedByteString(new BinaryString("Nash"),
                System.currentTimeMillis() + 200));

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Saver saver = new Saver(os);
        saver.save();

        DB.INSTANCE.clear();
        assertNull(DB.INSTANCE.get("John", TimedByteString.class));

        InputStream is = new ByteArrayInputStream(os.toByteArray());
        Loader loader = new Loader(is);
        loader.load();

        TimedByteString john = DB.INSTANCE.get("John", TimedByteString.class);
        assertEquals("Nash", john.value().toString());
        assertTrue(john.isValid());

        Thread.sleep(201);
        assertFalse(john.isValid());
    }

    @Test
    public void test_save_load_timed_string_invalid() throws InvalidFileFormat,
            InterruptedException {
        DB.INSTANCE.put("John", new TimedByteString(new BinaryString("Nash"),
                System.currentTimeMillis() + 200));

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Saver saver = new Saver(os);
        saver.save();

        DB.INSTANCE.clear();
        Thread.sleep(201);

        InputStream is = new ByteArrayInputStream(os.toByteArray());
        Loader loader = new Loader(is);
        loader.load();

        assertNull(DB.INSTANCE.get("John", TimedByteString.class));
    }

    @Test
    public void test_save_load_element_set() throws InvalidFileFormat,
            InterruptedException {
        ElementSet elementSet = new TreeElementSet();

        Element element = new Element("Five", 5.0);
        elementSet.insert(element);

        element = new Element("TwentyThreePointSevenFive", 23.75);
        elementSet.insert(element);

        element = new Element("One Thousand Eight Hundred And Ninenty Six",
                1896.0);
        elementSet.insert(element);

        element = new Element("Minus One Hunded And Seventy Five", -175.0);
        elementSet.insert(element);

        DB.INSTANCE.put("Numbers", elementSet);

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Saver saver = new Saver(os);
        saver.save();

        DB.INSTANCE.clear();
        assertNull(DB.INSTANCE.get("Numbers", ElementSet.class));

        InputStream is = new ByteArrayInputStream(os.toByteArray());
        Loader loader = new Loader(is);
        loader.load();

        ElementSet es = DB.INSTANCE.get("Numbers", ElementSet.class);
        Iterator<Element> iter = es.iterator();
        Element e = iter.next();
        assertEquals("Minus One Hunded And Seventy Five", e.getMember());
        assertEquals(Double.valueOf(-175.0), e.getScore());

        e = iter.next();
        assertEquals("Five", e.getMember());
        assertEquals(Double.valueOf(5.0), e.getScore());

        e = iter.next();
        assertEquals("TwentyThreePointSevenFive", e.getMember());
        assertEquals(Double.valueOf(23.75), e.getScore());
        
        e = iter.next();
        assertEquals("One Thousand Eight Hundred And Ninenty Six", e.getMember());
        assertEquals(Double.valueOf(1896), e.getScore());
        
        assertFalse(iter.hasNext());
    }

}
