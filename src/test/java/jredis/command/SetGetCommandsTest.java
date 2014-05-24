package jredis.command;

import static jredis.TestUtil.h;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import jredis.DB;
import jredis.domain.BinaryString;
import jredis.exception.InvalidCommand;

import org.junit.Before;
import org.junit.Test;

public class SetGetCommandsTest {

    public static BinaryString[] SET = h(new String[]{ "Rahul", "Dravid" });
    public static BinaryString[] GET = h(new String[] { "Rahul" });

    public static BinaryString[] SET_NX = h(new String[]{ "Rahul", "Gandhi", "NX" });
    public static BinaryString[] SET_NX_NX = h(new String[]{ "Rahul", "Gandhi", "NX", "NX" });
    public static BinaryString[] SET_XX = h(new String[]{ "Rahul", "Gandhi", "XX" });
    public static BinaryString[] SET_NX_XX = h(new String[]{ "Rahul", "Gandhi", "NX", "XX" });

    public static BinaryString[] SET_PX = h(new String[]{ "Rahul", "Dravid", "PX", "100" });
    public static BinaryString[] SET_EX = h(new String[]{ "Rahul", "Dravid", "EX", "2" });
    public static BinaryString[] SET_EX_PX_COMBINATION = h(new String[]{ "Rahul", "Dravid", "EX",
            "100", "PX", "100" });

    public static BinaryString[] SET_PX_NO_TIME = h(new String[]{ "Rahul", "Dravid", "PX" });
    public static BinaryString[] SET_EX_NO_TIME = h(new String[]{ "Rahul", "Dravid", "EX" });
    public static BinaryString[] SET_PX_INVALID_TIME = h(new String[]{ "Rahul", "Dravid", "PX",
            "NX" });
    public static BinaryString[] SET_EX_INVALID_TIME = h(new String[]{ "Rahul", "Dravid", "EX",
            "XX" });

    public static BinaryString[] SET_PX_XX_COMBINATION = h(new String[]{ "Rahul", "Gandhi", "PX",
            "100", "XX" });
    public static BinaryString[] SET_PX_NX_COMBINATION = h(new String[]{ "Rahul", "Gandhi", "PX",
            "100", "NX" });

    @Before
    public void setup() {
        DB.INSTANCE.clear();
    }

    @Test
    public void test_put_get() throws InvalidCommand {
        Command<String> command = new SetCommand(SET);
        assertEquals("OK", command.execute().value());

        command = new GetCommand(GET);
        assertEquals("Dravid", command.execute().value());
    }

    @Test
    public void test_get_null() throws InvalidCommand {
        Command<String> command = new GetCommand(
                h(new String[] { "MangoMan" }));
        assertNull(command.execute().value());
    }

    @Test
    public void test_setnx() throws InvalidCommand {
        Command<String> command = new SetCommand(SET);
        assertEquals("OK", command.execute().value());

        command = new SetCommand(SET_NX);
        assertNull(command.execute().value());

        command = new GetCommand(GET);
        assertEquals("Dravid", command.execute().value());
    }

    @Test
    public void test_setnx_negative() throws InvalidCommand {
        Command<String> command = new SetCommand(SET_NX);
        assertEquals("OK", command.execute().value());

        command = new GetCommand(GET);
        assertEquals("Gandhi", command.execute().value());
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
        assertEquals("OK", command.execute().value());

        command = new SetCommand(SET_XX);
        assertEquals("OK", command.execute().value());

        command = new GetCommand(GET);
        assertEquals("Gandhi", command.execute().value());
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
        assertEquals("OK", command.execute().value());

        command = new SetCommand(SET_NX_NX);
        assertNull(command.execute().value());

        command = new GetCommand(GET);
        assertEquals("Dravid", command.execute().value());
    }

    @Test
    public void test_setpx() throws InvalidCommand, InterruptedException {
        Command<String> command = new SetCommand(SET_PX);
        assertEquals("OK", command.execute().value());

        command = new GetCommand(GET);
        assertEquals("Dravid", command.execute().value());

        Thread.sleep(101);
        assertNull(command.execute().value());
    }

    @Test
    public void test_setpx_overwrite() throws InvalidCommand,
            InterruptedException {
        Command<String> command = new SetCommand(SET_PX);
        assertEquals("OK", command.execute().value());

        command = new GetCommand(GET);
        assertEquals("Dravid", command.execute().value());

        command = new SetCommand(SET_XX);
        assertEquals("OK", command.execute().value());

        command = new GetCommand(GET);
        assertEquals("Gandhi", command.execute().value());

        Thread.sleep(101);
        command = new GetCommand(GET);
        assertEquals("Gandhi", command.execute().value());
    }

    @Test
    public void test_setex() throws InvalidCommand, InterruptedException {
        Command<String> command = new SetCommand(SET_EX);
        assertEquals("OK", command.execute().value());

        command = new GetCommand(GET);
        assertEquals("Dravid", command.execute().value());

        Thread.sleep(2001);
        assertNull(command.execute().value());
    }

    @Test
    public void test_setPx_combination() throws InvalidCommand,
            InterruptedException {
        Command<String> command = new SetCommand(SET);
        assertEquals("OK", command.execute().value());

        Command<String> getCommand = new GetCommand(GET);
        assertEquals("Dravid", getCommand.execute().value());

        command = new SetCommand(SET_PX_NX_COMBINATION);
        assertNull(command.execute().value());

        assertEquals("Dravid", getCommand.execute().value());
        Thread.sleep(101);
        assertEquals("Dravid", getCommand.execute().value());

        command = new SetCommand(SET_PX_XX_COMBINATION);
        assertEquals("OK", command.execute().value());

        assertEquals("Gandhi", getCommand.execute().value());
        Thread.sleep(101);
        assertNull(getCommand.execute().value());
    }

    @Test
    public void test_setExPx_combination() throws InvalidCommand,
            InterruptedException {
        Command<String> command = new SetCommand(SET_EX_PX_COMBINATION);
        assertEquals("OK", command.execute().value());

        Command<String> getCommand = new GetCommand(GET);
        assertEquals("Dravid", getCommand.execute().value());

        Thread.sleep(101);
        assertNull(getCommand.execute().value());
    }

    @Test(expected = InvalidCommand.class)
    public void test_set_nx_with_key_as_zset() throws InvalidCommand {
        BinaryString[] addArgs = h(new String[]{ "Numbers", "1.0", "One" });
        Command<?> command = new ZaddCommand(addArgs);
        assertEquals(1, command.execute().value());

        BinaryString[] setArgs = h(new String[]{ "Numbers", "MyNumber", "NX" });
        command = new SetCommand(setArgs);
        assertNull(command.execute().value());

        BinaryString[] cardArgs = h(new String[]{ "Numbers" });
        command = new ZcardCommand(cardArgs);
        assertEquals(1, command.execute().value());

        String[] setArgs2 = { "Numbers", "MyNumber", "XX" };
        command = new SetCommand(h(setArgs2));
        assertEquals("OK", command.execute().value());

        command = new ZcardCommand(cardArgs);
        assertEquals(1, command.execute().value());
    }

    @Test(expected = InvalidCommand.class)
    public void test_get_with_key_as_zset() throws InvalidCommand {
        BinaryString[] addArgs = h(new String[]{ "Numbers", "1.0", "One" });
        Command<?> command = new ZaddCommand(addArgs);
        assertEquals(1, command.execute().value());

        BinaryString[] getArgs = h(new String[]{ "Numbers", "MyNumber" });
        command = new GetCommand(getArgs);
        command.execute().value();

    }

    /*
     * TODO: Some missing test cases 1. With class cast.
     */

}
