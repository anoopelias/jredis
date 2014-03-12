package jredis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;

import jredis.exception.InvalidCommand;

import org.junit.Test;

public class CommandReaderTest {

    private static String CMD_GET = "*2\r\n$3\r\nGET\r\n$4\r\nJeff\r\n";
    private static String CMD_GET_ELON = "*2\r\n$3\r\nGET\r\n$4\r\nElon\r\n";
    private static String CMD_SET = "*3\r\n$3\r\nSET\r\n$4\r\nElon\r\n$4\r\nMusk\r\n";
    private static String CMD_INVALID_ARG_COUNT = "*k\r\n";
    private static String CMD_NONE = "";

    @Test
    public void test_read_get() throws UnsupportedEncodingException,
            InvalidCommand {
        
        CommandReader reader = new CommandReader(new ByteArrayInputStream(
                CMD_GET.getBytes("UTF-8")));
        Command<?> c = reader.next();
        assertTrue(c instanceof GetCommand);
        assertNull(reader.next());
    }

    @Test
    public void test_read_set() throws UnsupportedEncodingException,
            InvalidCommand {
        
        CommandReader reader = new CommandReader(new ByteArrayInputStream(
                CMD_SET.getBytes("UTF-8")));
        Command<?> c = reader.next();
        assertTrue(c instanceof SetCommand);
        assertNull(reader.next());
    }

    @Test
    public void test_set_get() throws UnsupportedEncodingException,
            InvalidCommand {
        
        CommandReader reader = new CommandReader(new ByteArrayInputStream(
                CMD_SET.getBytes("UTF-8")));
        Command<?> c = reader.next();
        assertEquals("OK", c.execute().value());
        
        reader = new CommandReader(new ByteArrayInputStream(
                CMD_GET_ELON.getBytes("UTF-8")));
        c = reader.next();
        assertEquals("Musk", c.execute().value());
    }

    @Test
    public void test_read_none() throws UnsupportedEncodingException,
            InvalidCommand {
        
        CommandReader reader = new CommandReader(new ByteArrayInputStream(
                CMD_NONE.getBytes("UTF-8")));
        assertNull(reader.next());
    }

    @Test(expected = InvalidCommand.class)
    public void test_invalid_arg_count() throws UnsupportedEncodingException,
            InvalidCommand {
        CommandReader reader = new CommandReader(new ByteArrayInputStream(
                CMD_INVALID_ARG_COUNT.getBytes("UTF-8")));
        reader.next();
    }

    /* TODO : InvalidCommand test cases
     * 3. Commands which doesn't start with a *
     * 4. Commands which do not belong to any specified commands
     * 5. Commands where there is not enough arguments as specified initially. (For eg. the stream finishes earlier)
     * 6. Commands which ends on just \n and not on \r\n
     * 7. Commands which specify an invalid byte size of an arg
     * 8. Commands with byte size which do not start in $
     * 9. Commands with inconsistent number of bytes as the command itself has specified
     */
    
    /*
     * TODO: Some more functional issues to cover. 
     * 1. CommandReader recovery in case of an error.
     * 2. CommandReader with new line inside keys and values
     * 
     */

}
