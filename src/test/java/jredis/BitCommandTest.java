package jredis;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import jredis.exception.InvalidCommand;

import org.junit.Before;
import org.junit.Test;

public class BitCommandTest {
    private static String[] SETBIT = {"Keys", "7", "1"};
    private static String[] GETBIT = {"Keys", "7"};

    private static String[] SETBIT_ZERO = {"Keys", "200", "0"};
    private static String[] GETBIT_ZERO = {"Keys", "200"};
    
    private static String[] NO_ARGS = {}; 
    private static String[] ONE_ARG = {"Keys"};
    
    private static String[] SETBIT_INVALID_OFFSET = {"Keys", "K", "1"};
    private static String[] SETBIT_OFFSET_BELOW_0 = {"Keys", "-1", "1"};
    private static String[] SETBIT_INVALID_VALUE = {"Keys", "7", "K"};
    private static String[] SETBIT_VALUE_ABOVE_1 = {"Keys", "7", "2"};
    private static String[] SETBIT_VALUE_BELOW_0 = {"Keys", "7", "-1"};

    private static String[] GETBIT_INVALID_OFFSET = {"Keys", "K"};
    private static String[] GETBIT_OFFSET_BELOW_0 = {"Keys", "-1"};

    @Before
    public void setup() {
        DataMap.INSTANCE.clear();
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
     * TODO: Out of range offset tests
     */

    
}
