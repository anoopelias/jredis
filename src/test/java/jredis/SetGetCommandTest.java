package jredis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import jredis.exception.InvalidCommand;

import org.junit.Before;
import org.junit.Test;

public class SetGetCommandTest {
    
    public static String[] SET_ARGS = {"Rahul", "Dravid"};
    public static String[] GET_ARGS = {"Rahul"};

    public static String[] SET_NX_ARGS = {"Rahul", "Gandhi", "NX"};
    public static String[] SET_NX_NX_ARGS = {"Rahul", "Gandhi", "NX", "NX"};
    public static String[] SET_XX_ARGS = {"Rahul", "Gandhi", "XX"};
    public static String[] SET_NX_XX_ARGS = {"Rahul", "Gandhi", "NX", "XX"};
    public static String[] SET_NX_KX_ARGS = {"Rahul", "Gandhi", "NX", "KX"};

    public static String[] SET_PX_ARGS = {"Rahul", "Dravid", "PX", "100"};
    public static String[] SET_EX_ARGS = {"Rahul", "Dravid", "EX", "2"};

    public static String[] SET_PX_NO_TIME_ARGS = {"Rahul", "Dravid", "PX"};
    public static String[] SET_EX_NO_TIME_ARGS = {"Rahul", "Dravid", "EX"};
    public static String[] SET_PX_INVALID_TIME_ARGS = {"Rahul", "Dravid", "PX", "NX"};
    public static String[] SET_EX_INVALID_TIME_ARGS = {"Rahul", "Dravid", "EX", "XX"};

    @Before
    public void setup() {
        DataMap.INSTANCE.clear();
    }

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

    @Test
    public void test_setnx() throws InvalidCommand {
        Command command = new SetCommand(SET_ARGS);
        assertEquals("OK",  command.execute());

        command = new SetCommand(SET_NX_ARGS);
        assertNull(command.execute());

        command = new GetCommand(GET_ARGS);
        assertEquals("Dravid",  command.execute());
    }

    @Test
    public void test_setnx_negative() throws InvalidCommand {
        Command command = new SetCommand(SET_NX_ARGS);
        assertEquals("OK",  command.execute());

        command = new GetCommand(GET_ARGS);
        assertEquals("Gandhi",  command.execute());
    }

    @Test
    public void test_setxx() throws InvalidCommand {
        Command command = new SetCommand(SET_XX_ARGS);
        assertNull(command.execute());

        command = new GetCommand(GET_ARGS);
        assertNull(command.execute());
    }

    @Test
    public void test_setxx_negative() throws InvalidCommand {
        Command command = new SetCommand(SET_ARGS);
        assertEquals("OK",  command.execute());

        command = new SetCommand(SET_XX_ARGS);
        assertEquals("OK",  command.execute());

        command = new GetCommand(GET_ARGS);
        assertEquals("Gandhi",  command.execute());
    }

    @Test
    public void test_setNxXx() throws InvalidCommand {
        Command command = new SetCommand(SET_NX_XX_ARGS);
        assertNull(command.execute());

        command = new GetCommand(GET_ARGS);
        assertNull(command.execute());
    }

    @Test
    public void test_setnxnx() throws InvalidCommand {
        Command command = new SetCommand(SET_ARGS);
        assertEquals("OK",  command.execute());

        command = new SetCommand(SET_NX_NX_ARGS);
        assertNull(command.execute());

        command = new GetCommand(GET_ARGS);
        assertEquals("Dravid",  command.execute());
    }

    @Test(expected = InvalidCommand.class)
    public void test_set_unknown_argument() throws InvalidCommand {
        Command command = new GetCommand(SET_NX_KX_ARGS);
        command.execute();
    }

    @Test
    public void test_setpx() throws InvalidCommand, InterruptedException {
        Command command = new SetCommand(SET_PX_ARGS);
        assertEquals("OK",  command.execute());

        command = new GetCommand(GET_ARGS);
        assertEquals("Dravid",  command.execute());
        
        Thread.sleep(101);
        assertNull(command.execute());
    }

    @Test
    public void test_setpx_overwrite() throws InvalidCommand, InterruptedException {
        Command command = new SetCommand(SET_PX_ARGS);
        assertEquals("OK",  command.execute());

        command = new GetCommand(GET_ARGS);
        assertEquals("Dravid",  command.execute());

        command = new SetCommand(SET_XX_ARGS);
        assertEquals("OK",  command.execute());

        command = new GetCommand(GET_ARGS);
        assertEquals("Gandhi",  command.execute());

        Thread.sleep(101);
        command = new GetCommand(GET_ARGS);
        assertEquals("Gandhi",  command.execute());
    }

    @Test
    public void test_setex() throws InvalidCommand, InterruptedException {
        Command command = new SetCommand(SET_EX_ARGS);
        assertEquals("OK",  command.execute());

        command = new GetCommand(GET_ARGS);
        assertEquals("Dravid",  command.execute());
        
        Thread.sleep(2001);
        assertNull(command.execute());
    }

    @Test(expected = InvalidCommand.class)
    public void test_get_px_no_time() throws InvalidCommand {
        Command command = new SetCommand(SET_PX_NO_TIME_ARGS);
        command.execute();
    }

    @Test(expected = InvalidCommand.class)
    public void test_get_ex_no_time() throws InvalidCommand {
        Command command = new SetCommand(SET_EX_NO_TIME_ARGS);
        command.execute();
    }

    @Test(expected = InvalidCommand.class)
    public void test_get_px_invalid_time() throws InvalidCommand {
        Command command = new SetCommand(SET_PX_INVALID_TIME_ARGS);
        command.execute();
    }

    @Test(expected = InvalidCommand.class)
    public void test_get_ex_invalid_time() throws InvalidCommand {
        Command command = new SetCommand(SET_EX_INVALID_TIME_ARGS);
        command.execute();
    }

}
