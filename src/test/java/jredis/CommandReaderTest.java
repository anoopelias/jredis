package jredis;

import static jredis.TestUtil.toInputStream;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import jredis.exception.InvalidCommand;

import org.junit.Before;
import org.junit.Test;

public class CommandReaderTest {

    private static String CMD_INVALID_ARG_COUNT = "*k\r\n";

    private static String[] CMD_GET = { "GET", "Jeff" };
    private static String[] CMD_SET = { "SET", "Elon", "Musk" };
    private static String CMD_NONE = "";

    private static String[] CMD_INVALID_TYPE = { "LOOK", "Elon" };

    private static String CMD_WRONG_FIRST_CHAR = "$k\r\n";

    @Before
    public void setup() {
        DataMap.INSTANCE.clear();
    }

    @Test
    public void test_read_get() throws InvalidCommand {
        CommandReader reader = new CommandReader(toInputStream(CMD_GET));
        Command<?> c = reader.next();
        assertTrue(c instanceof GetCommand);
        assertNull(reader.next());
    }

    @Test
    public void test_read_set() throws InvalidCommand {

        CommandReader reader = new CommandReader(toInputStream(CMD_SET));
        Command<?> c = reader.next();
        assertTrue(c instanceof SetCommand);
        assertNull(reader.next());
    }

    @Test
    public void test_read_none() throws InvalidCommand {

        CommandReader reader = new CommandReader(toInputStream(CMD_NONE));
        assertNull(reader.next());
    }

    @Test(expected = InvalidCommand.class)
    public void test_invalid_arg_count() throws InvalidCommand {
        CommandReader reader = new CommandReader(
                toInputStream(CMD_INVALID_ARG_COUNT));
        reader.next();
    }

    @Test(expected = InvalidCommand.class)
    public void test_invalid_type() throws InvalidCommand {
        CommandReader reader = new CommandReader(
                toInputStream(CMD_INVALID_TYPE));
        reader.next();
    }

    @Test(expected = InvalidCommand.class)
    public void test_no_starting_star() throws InvalidCommand {
        CommandReader reader = new CommandReader(toInputStream(CMD_WRONG_FIRST_CHAR));
        reader.next();
    }

    /*
     * TODO : InvalidCommand test cases 
     * 5. Commands where there is not enough arguments as specified initially. (For eg. the
     * stream finishes earlier) 
     * 6. Commands which ends on just \n and not on \r\n 
     * 7. Commands which specify an invalid byte size of an arg 
     * 8. Commands with byte size which do not start in $ 
     * 9. Commands with inconsistent number of bytes as the command itself has specified
     */

    /*
     * TODO: Some more functional issues to cover. 
     * 1. CommandReader recovery in case of an error. 
     */

}
