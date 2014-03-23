package jredis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import jredis.exception.InvalidCommand;

import org.junit.Before;
import org.junit.Test;

public class SetGetCommandsTest {
    
    public static String[] SET = {"Rahul", "Dravid"};
    public static String[] GET = {"Rahul"};

    public static String[] SET_NX = {"Rahul", "Gandhi", "NX"};
    public static String[] SET_NX_NX = {"Rahul", "Gandhi", "NX", "NX"};
    public static String[] SET_XX = {"Rahul", "Gandhi", "XX"};
    public static String[] SET_NX_XX = {"Rahul", "Gandhi", "NX", "XX"};

    public static String[] SET_PX = {"Rahul", "Dravid", "PX", "100"};
    public static String[] SET_EX = {"Rahul", "Dravid", "EX", "2"};
    public static String[] SET_EX_PX_COMBINATION = {"Rahul", "Dravid", "EX", "100", "PX", "100"};

    public static String[] SET_PX_NO_TIME = {"Rahul", "Dravid", "PX"};
    public static String[] SET_EX_NO_TIME = {"Rahul", "Dravid", "EX"};
    public static String[] SET_PX_INVALID_TIME = {"Rahul", "Dravid", "PX", "NX"};
    public static String[] SET_EX_INVALID_TIME = {"Rahul", "Dravid", "EX", "XX"};

    public static String[] SET_PX_XX_COMBINATION = {"Rahul", "Gandhi", "PX", "100", "XX"};
    public static String[] SET_PX_NX_COMBINATION = {"Rahul", "Gandhi", "PX", "100", "NX"};

    @Before
    public void setup() {
        DataMap.INSTANCE.clear();
    }

    @Test
    public void test_put_get() throws InvalidCommand {
        Command<String> command = new SetCommand(SET);
        assertEquals("OK",  command.execute().value());
        
        command = new GetCommand(GET);
        assertEquals("Dravid",  command.execute().value());
    }

    @Test
    public void test_get_null() throws InvalidCommand {
        Command<String> command = new GetCommand(new String[]{"MangoMan"});
        assertNull(command.execute().value());
    }

    @Test
    public void test_setnx() throws InvalidCommand {
        Command<String> command = new SetCommand(SET);
        assertEquals("OK",  command.execute().value());

        command = new SetCommand(SET_NX);
        assertNull(command.execute().value());

        command = new GetCommand(GET);
        assertEquals("Dravid",  command.execute().value());
    }

    @Test
    public void test_setnx_negative() throws InvalidCommand {
        Command<String> command = new SetCommand(SET_NX);
        assertEquals("OK",  command.execute().value());

        command = new GetCommand(GET);
        assertEquals("Gandhi",  command.execute().value());
    }

    @Test
    public void test_setxx() throws InvalidCommand {
        Command<String> command = new SetCommand(SET_XX);
        assertNull(command.execute().value());

        command = new GetCommand(GET);
        assertNull(command.execute().value());
    }

    @Test
    public void test_setxx_negative() throws InvalidCommand {
        Command<String> command = new SetCommand(SET);
        assertEquals("OK",  command.execute().value());

        command = new SetCommand(SET_XX);
        assertEquals("OK",  command.execute().value());

        command = new GetCommand(GET);
        assertEquals("Gandhi",  command.execute().value());
    }

    @Test
    public void test_setNxXx() throws InvalidCommand {
        Command<String> command = new SetCommand(SET_NX_XX);
        assertNull(command.execute().value());

        command = new GetCommand(GET);
        assertNull(command.execute().value());
    }

    @Test
    public void test_setNxNx() throws InvalidCommand {
        Command<String> command = new SetCommand(SET);
        assertEquals("OK",  command.execute().value());

        command = new SetCommand(SET_NX_NX);
        assertNull(command.execute().value());

        command = new GetCommand(GET);
        assertEquals("Dravid",  command.execute().value());
    }

    @Test
    public void test_setpx() throws InvalidCommand, InterruptedException {
        Command<String> command = new SetCommand(SET_PX);
        assertEquals("OK",  command.execute().value());

        command = new GetCommand(GET);
        assertEquals("Dravid",  command.execute().value());
        
        Thread.sleep(101);
        assertNull(command.execute().value());
    }

    @Test
    public void test_setpx_overwrite() throws InvalidCommand, InterruptedException {
        Command<String> command = new SetCommand(SET_PX);
        assertEquals("OK",  command.execute().value());

        command = new GetCommand(GET);
        assertEquals("Dravid",  command.execute().value());

        command = new SetCommand(SET_XX);
        assertEquals("OK",  command.execute().value());

        command = new GetCommand(GET);
        assertEquals("Gandhi",  command.execute().value());

        Thread.sleep(101);
        command = new GetCommand(GET);
        assertEquals("Gandhi",  command.execute().value());
    }

    @Test
    public void test_setex() throws InvalidCommand, InterruptedException {
        Command<String> command = new SetCommand(SET_EX);
        assertEquals("OK",  command.execute().value());

        command = new GetCommand(GET);
        assertEquals("Dravid",  command.execute().value());
        
        Thread.sleep(2001);
        assertNull(command.execute().value());
    }

    @Test
    public void test_setPx_combination() throws InvalidCommand, InterruptedException {
        Command<String> command = new SetCommand(SET);
        assertEquals("OK",  command.execute().value());

        Command<String> getCommand = new GetCommand(GET);
        assertEquals("Dravid",  getCommand.execute().value());

        command = new SetCommand(SET_PX_NX_COMBINATION);
        assertNull(command.execute().value());

        assertEquals("Dravid",  getCommand.execute().value());
        Thread.sleep(101);
        assertEquals("Dravid",  getCommand.execute().value());

        command = new SetCommand(SET_PX_XX_COMBINATION);
        assertEquals("OK",  command.execute().value());

        assertEquals("Gandhi",  getCommand.execute().value());
        Thread.sleep(101);
        assertNull(getCommand.execute().value());
    }

    @Test
    public void test_setExPx_combination() throws InvalidCommand, InterruptedException {
        Command<String> command = new SetCommand(SET_EX_PX_COMBINATION);
        assertEquals("OK",  command.execute().value());

        Command<String> getCommand = new GetCommand(GET);
        assertEquals("Dravid",  getCommand.execute().value());

        Thread.sleep(101);
        assertNull(getCommand.execute().value());
    }

    @Test(expected=InvalidCommand.class)
    public void test_set_nx_with_key_as_zset() throws InvalidCommand {
        String[] addArgs = {"Numbers", "1.0", "One"};
        Command<?> command = new ZaddCommand(addArgs);
        assertEquals(1, command.execute().value());
        
        String[] setArgs = {"Numbers", "MyNumber", "NX"};
        command = new SetCommand(setArgs);
        assertNull(command.execute().value());

        String[] cardArgs = {"Numbers"};
        command = new ZcardCommand(cardArgs);
        assertEquals(1, command.execute().value());

        String[] setArgs2 = {"Numbers", "MyNumber", "XX"};
        command = new SetCommand(setArgs2);
        assertEquals("OK", command.execute().value());

        command = new ZcardCommand(cardArgs);
        assertEquals(1, command.execute().value());
    }

    @Test(expected=InvalidCommand.class)
    public void test_get_with_key_as_zset() throws InvalidCommand {
        String[] addArgs = {"Numbers", "1.0", "One"};
        Command<?> command = new ZaddCommand(addArgs);
        assertEquals(1, command.execute().value());
        
        String[] getArgs = {"Numbers", "MyNumber"};
        command = new GetCommand(getArgs);
        command.execute().value();
        
    }

    /*
     * TODO: Some missing test cases
     * 1. With class cast.
     * 
     */


}
