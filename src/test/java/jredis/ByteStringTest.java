package jredis;

import static org.junit.Assert.assertEquals;

import java.io.UnsupportedEncodingException;

import org.junit.Test;

public class ByteStringTest {
    
    @Test
    public void test_string() {
        ByteString byteString = new ByteString("Mathew Perry");
        assertEquals("Mathew Perry", byteString.toString());
    }

    @Test
    public void test_bytes() throws UnsupportedEncodingException {
        ByteString byteString = new ByteString("Mathew Perry".getBytes(Protocol.CHARSET));
        assertEquals("Mathew Perry", byteString.toString());
    }

}
