package jredis.command;

import static jredis.TestUtil.h;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import jredis.DB;
import jredis.domain.BinaryString;
import jredis.exception.InvalidCommand;

import org.junit.Before;
import org.junit.Test;

public class BitCommandsTest {
    private static BinaryString[] SETBIT = h(new String[]{"Keys", "7", "1"});
    private static BinaryString[] GETBIT = h(new String[]{"Keys", "7"});

    private static BinaryString[] SETBIT_ZERO = h(new String[]{"Keys", "200", "0"});
    private static BinaryString[] GETBIT_ZERO = h(new String[]{"Keys", "200"});
    
    private static BinaryString[] NO_ARGS = h(new String[]{}); 
    private static BinaryString[] ONE_ARG = h(new String[]{"Keys"});
    
    private static BinaryString[] SETBIT_INVALID_OFFSET = h(new String[]{"Keys", "K", "1"});
    private static BinaryString[] SETBIT_OFFSET_BELOW_0 = h(new String[]{"Keys", "-1", "1"});
    private static BinaryString[] SETBIT_INVALID_VALUE = h(new String[]{"Keys", "7", "K"});
    private static BinaryString[] SETBIT_VALUE_ABOVE_1 = h(new String[]{"Keys", "7", "2"});
    private static BinaryString[] SETBIT_VALUE_BELOW_0 = h(new String[]{"Keys", "7", "-1"});

    private static BinaryString[] GETBIT_INVALID_OFFSET = h(new String[]{"Keys", "K"});
    private static BinaryString[] GETBIT_OFFSET_BELOW_0 = h(new String[]{"Keys", "-1"});

    @Before
    public void setup() {
        DB.INSTANCE.clear();
    }

    @Test
    public void test_setbit_getbit() throws InvalidCommand {
        SetbitCommand setbitCommand = new SetbitCommand(SETBIT);
        assertFalse(setbitCommand.execute().value());

        GetbitCommand getbitCommand = new GetbitCommand(GETBIT);
        assertTrue(getbitCommand.execute().value());
    }

    @Test
    public void test_setbit_getbit_zero() throws InvalidCommand {
        SetbitCommand setbitCommand = new SetbitCommand(SETBIT_ZERO);
        assertFalse(setbitCommand.execute().value());

        GetbitCommand getbitCommand = new GetbitCommand(GETBIT_ZERO);
        assertFalse(getbitCommand.execute().value());
    }

    @Test
    public void test_setbit_current_value() throws InvalidCommand {
        SetbitCommand setbitCommand = new SetbitCommand(SETBIT);
        assertFalse(setbitCommand.execute().value());
        assertTrue(setbitCommand.execute().value());
    }
    
    @Test
    public void test_getbit_null() throws InvalidCommand {
        GetbitCommand getbitCommand = new GetbitCommand(GETBIT);
        assertFalse(getbitCommand.execute().value());
    }

    @Test(expected=InvalidCommand.class)
    public void test_setbit_noargs() throws InvalidCommand {
        SetbitCommand setbitCommand = new SetbitCommand(NO_ARGS);
        setbitCommand.execute();
    }

    @Test(expected=InvalidCommand.class)
    public void test_setbit_onearg() throws InvalidCommand {
        SetbitCommand setbitCommand = new SetbitCommand(ONE_ARG);
        setbitCommand.execute();
    }

    @Test(expected=InvalidCommand.class)
    public void test_setbit_twoargs() throws InvalidCommand {
        SetbitCommand setbitCommand = new SetbitCommand(GETBIT);
        setbitCommand.execute();
    }

    @Test(expected=InvalidCommand.class)
    public void test_setbit_string_offset() throws InvalidCommand {
        SetbitCommand setbitCommand = new SetbitCommand(SETBIT_INVALID_OFFSET);
        setbitCommand.execute();
    }

    @Test(expected=InvalidCommand.class)
    public void test_setbit_offset_below_range() throws InvalidCommand {
        SetbitCommand setbitCommand = new SetbitCommand(SETBIT_OFFSET_BELOW_0);
        setbitCommand.execute();
    }

    @Test(expected=InvalidCommand.class)
    public void test_setbit_string_value() throws InvalidCommand {
        SetbitCommand setbitCommand = new SetbitCommand(SETBIT_INVALID_VALUE);
        setbitCommand.execute();
    }

    @Test(expected=InvalidCommand.class)
    public void test_setbit_value_above_one() throws InvalidCommand {
        SetbitCommand setbitCommand = new SetbitCommand(SETBIT_VALUE_ABOVE_1);
        setbitCommand.execute();
    }

    @Test(expected=InvalidCommand.class)
    public void test_setbit_value_below_zero() throws InvalidCommand {
        SetbitCommand setbitCommand = new SetbitCommand(SETBIT_VALUE_BELOW_0);
        setbitCommand.execute();
    }
    
    @Test(expected=InvalidCommand.class)
    public void test_getbit_noargs() throws InvalidCommand {
        GetbitCommand getbitCommand = new GetbitCommand(NO_ARGS);
        getbitCommand.execute();
    }

    @Test(expected=InvalidCommand.class)
    public void test_getbit_onearg() throws InvalidCommand {
        GetbitCommand getbitCommand = new GetbitCommand(ONE_ARG);
        getbitCommand.execute();
    }

    @Test(expected=InvalidCommand.class)
    public void test_getbit_invalid_offset() throws InvalidCommand {
        GetbitCommand getbitCommand = new GetbitCommand(GETBIT_INVALID_OFFSET);
        getbitCommand.execute();
    }

    @Test(expected=InvalidCommand.class)
    public void test_getbit_offset_below_range() throws InvalidCommand {
        GetbitCommand getbitCommand = new GetbitCommand(GETBIT_OFFSET_BELOW_0);
        getbitCommand.execute();
    }

    /*
     * TODO: 
     * 1. Out of range offset tests
     * 2. Set and then Getbit.
     * 3. Expired key available on getbit.
     * 4. Expired key available on setbit.
     */

    
}
