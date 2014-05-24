package jredis.command;

import static jredis.TestUtil.h;
import jredis.DB;
import jredis.domain.BinaryString;
import jredis.exception.InvalidCommand;

import org.junit.Before;
import org.junit.Test;

public class SetCommandTest {
    
    private BinaryString[] NO_ARGS = {};
    private BinaryString[] ONE_ARG = h(new String[]{"Numbers"});
    private BinaryString[] TWO_ARGS = h(new String[]{"Numbers", "1.05"});
    
    private BinaryString[] THREE_ARGS_NUM = h(new String[]{"Numbers", "1.05", "85"});
    private BinaryString[] THREE_ARGS_STR = h(new String[]{"Numbers", "1.05", "FX"});

    private BinaryString[] NX = h(new String[]{"Numbers", "1.05", "NX"});
    private BinaryString[] XX = h(new String[]{"Numbers", "1.05", "XX"});
    private BinaryString[] NX_XX = h(new String[]{"Numbers", "1.05", "NX", "XX"});

    private BinaryString[] PX = h(new String[]{"Numbers", "1.05", "PX", "1000"});
    private BinaryString[] EX = h(new String[]{"Numbers", "1.05", "EX", "10"});
    private BinaryString[] PX_EX = h(new String[]{"Numbers", "1.05", "PX", "1000", "EX", "10"});

    private BinaryString[] PX_REV_ORDER = h(new String[]{"Numbers", "1.05", "1000", "PX"});
    private BinaryString[] EX_REV_ORDER = h(new String[]{"Numbers", "1.05", "10", "EX"});

    private BinaryString[] PX_NO_ARG = h(new String[]{"Numbers", "1.05", "PX"});
    private BinaryString[] EX_NO_ARG = h(new String[]{"Numbers", "1.05", "EX"});

    private BinaryString[] PX_STRING_ARG = h(new String[]{"Numbers", "1.05", "PX", "AVC"});
    private BinaryString[] EX_STRING_ARG = h(new String[]{"Numbers", "1.05", "EX", "HIJ"});

    private BinaryString[] PX_NX = h(new String[]{"Numbers", "1.05", "PX", "1000", "NX"});
    private BinaryString[] EX_XX = h(new String[]{"Numbers", "1.05", "XX", "EX", "10"});

    private BinaryString[] PX_XX_INVALID = h(new String[]{"Numbers", "1.05", "PX", "XX", "1000"});
    private BinaryString[] EX_NX_INVALID = h(new String[]{"Numbers", "1.05", "NX", "10", "EX"});
    
    @Before
    public void setup() {
        DB.INSTANCE.clear();
    }

    @Test(expected=InvalidCommand.class)
    public void test_no_args() throws InvalidCommand {
        new SetCommand(NO_ARGS);
    }

    @Test(expected=InvalidCommand.class)
    public void test_one_arg() throws InvalidCommand {
        new SetCommand(ONE_ARG);
    }

    @Test
    public void test_two_args() throws InvalidCommand {
        Command<?> setCommand = new SetCommand(TWO_ARGS);
        setCommand.execute();
    }

    @Test(expected=InvalidCommand.class)
    public void test_three_args_num() throws InvalidCommand {
        new SetCommand(THREE_ARGS_NUM);
    }

    @Test(expected=InvalidCommand.class)
    public void test_three_args_str() throws InvalidCommand {
        new SetCommand(THREE_ARGS_STR);
    }

    @Test
    public void test_nx() throws InvalidCommand {
        Command<?> setCommand = new SetCommand(NX);
        setCommand.execute();
    }

    @Test
    public void test_xx() throws InvalidCommand {
        Command<?> setCommand = new SetCommand(XX);
        setCommand.execute();
    }
    
    @Test
    public void test_nx_xx() throws InvalidCommand {
        Command<?> setCommand = new SetCommand(NX_XX);
        setCommand.execute();
    }


    @Test
    public void test_px() throws InvalidCommand {
        Command<?> setCommand = new SetCommand(PX);
        setCommand.execute();
    }

    @Test
    public void test_ex() throws InvalidCommand {
        Command<?> setCommand = new SetCommand(EX);
        setCommand.execute();
    }

    @Test
    public void test_ex_px() throws InvalidCommand {
        Command<?> setCommand = new SetCommand(PX_EX);
        setCommand.execute();
    }

    @Test(expected=InvalidCommand.class)
    public void test_px_rev_order() throws InvalidCommand {
        new SetCommand(PX_REV_ORDER);
    }

    @Test(expected=InvalidCommand.class)
    public void test_ex_rev_order() throws InvalidCommand {
        new SetCommand(EX_REV_ORDER);
    }

    @Test(expected=InvalidCommand.class)
    public void test_px_no_arg() throws InvalidCommand {
        new SetCommand(PX_NO_ARG);
    }

    @Test(expected=InvalidCommand.class)
    public void test_ex_no_arg() throws InvalidCommand {
        new SetCommand(EX_NO_ARG);
    }

    @Test(expected=InvalidCommand.class)
    public void test_px_string_arg() throws InvalidCommand {
        new SetCommand(PX_STRING_ARG);
    }

    @Test(expected=InvalidCommand.class)
    public void test_ex_string_arg() throws InvalidCommand {
        new SetCommand(EX_STRING_ARG);
    }

    @Test
    public void test_px_nx() throws InvalidCommand {
        Command<?> setCommand = new SetCommand(PX_NX);
        setCommand.execute();
    }

    @Test
    public void test_ex_xx() throws InvalidCommand {
        Command<?> setCommand = new SetCommand(EX_XX);
        setCommand.execute();
    }

    @Test(expected=InvalidCommand.class)
    public void test_px_xx_invalid() throws InvalidCommand {
        new SetCommand(PX_XX_INVALID);
    }

    @Test(expected=InvalidCommand.class)
    public void test_ex_nx_invalid() throws InvalidCommand {
        new SetCommand(EX_NX_INVALID);
    }

}
