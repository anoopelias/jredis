package jredis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import jredis.exceptions.InvalidCommand;

import org.junit.Test;

public class SetGetCommandTest {
    
    public static String[] SET_ARGS = {"Rahul", "Dravid"};
    public static String[] GET_ARGS = {"Rahul"};
    
    @Test
    public void test_put_get() throws InvalidCommand {
        Command command = new SetCommand(SET_ARGS);
        assertEquals("OK",  command.execute());
        
        command = new GetCommand(GET_ARGS);
        assertEquals("Dravid",  command.execute());
    }

    @Test(expected = InvalidCommand.class)
    public void test_get_invalid_params() throws InvalidCommand {
        Command command = new GetCommand(SET_ARGS);
        command.execute();
    }

    @Test(expected = InvalidCommand.class)
    public void test_put_invalid_params() throws InvalidCommand {
        Command command = new SetCommand(GET_ARGS);
        command.execute();
    }

    @Test
    public void test_get_null() throws InvalidCommand {
        Command command = new GetCommand(new String[]{"MangoMan"});
        assertNull(command.execute());
    }


}
