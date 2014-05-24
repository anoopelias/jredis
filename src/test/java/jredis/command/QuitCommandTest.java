package jredis.command;

import static jredis.TestUtil.h;
import static org.junit.Assert.assertEquals;
import jredis.domain.BinaryString;
import jredis.exception.InvalidCommand;

import org.junit.Test;

public class QuitCommandTest {
    
    private static BinaryString[] NO_ARGS = {};
    private static BinaryString[] ONE_ARG = h(new String[]{"Now"});
    
    @Test
    public void test_quit() throws InvalidCommand {
        Command<?> command = new QuitCommand(NO_ARGS);
        assertEquals("OK", command.execute().value());
    }

    @Test
    public void test_quit_null() throws InvalidCommand {
        Command<?> command = new QuitCommand(new BinaryString[0]);
        assertEquals("OK", command.execute().value());
    }

    @Test(expected=InvalidCommand.class)
    public void test_quit_with_args() throws InvalidCommand {
        Command<?> command = new QuitCommand(ONE_ARG);
        assertEquals("OK", command.execute().value());
    }

}
