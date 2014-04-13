package jredis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

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
        DB.INSTANCE.put("John", new TimedByteString(new ByteString("Nash")));
        
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
    public void test_save_load_timed_string() throws InvalidFileFormat, InterruptedException {
        DB.INSTANCE.put("John", new TimedByteString(new ByteString("Nash"), System.currentTimeMillis() + 200));
        
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

}
