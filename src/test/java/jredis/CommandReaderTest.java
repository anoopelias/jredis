package jredis;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;

import jredis.exceptions.InvalidCommand;

import org.junit.Test;

public class CommandReaderTest {

    private static String CMD_GET = "*3\r\n$3\r\nGET\r\n$4\r\nJeff\r\n";
    private static String CMD_INVALID_ARG_COUNT = "*k\r\n";

    @Test
    public void test_read_number_of_args() throws UnsupportedEncodingException,
            InvalidCommand {
        
        CommandReader reader = new CommandReader(new ByteArrayInputStream(
                CMD_GET.getBytes("UTF-8")));
        Command c = reader.next();
        assertTrue(c instanceof GetCommand);
    }

    @Test(expected = InvalidCommand.class)
    public void test_invalid_arg_count() throws UnsupportedEncodingException,
            InvalidCommand {
        new CommandReader(new ByteArrayInputStream(
                CMD_INVALID_ARG_COUNT.getBytes("UTF-8")));
    }

}
