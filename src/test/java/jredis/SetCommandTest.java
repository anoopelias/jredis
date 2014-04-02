package jredis;

import jredis.exception.InvalidCommand;

import org.junit.Before;
import org.junit.Test;

public class SetCommandTest {
    
    private String[] NO_ARGS = {};
    private String[] ONE_ARG = {"Numbers"};
    private String[] TWO_ARGS = {"Numbers", "1.05"};
    
    private String[] THREE_ARGS_NUM = {"Numbers", "1.05", "85"};
    private String[] THREE_ARGS_STR = {"Numbers", "1.05", "FX"};

    private String[] NX = {"Numbers", "1.05", "NX"};
    private String[] XX = {"Numbers", "1.05", "XX"};
    private String[] NX_XX = {"Numbers", "1.05", "NX", "XX"};

    private String[] PX = {"Numbers", "1.05", "PX", "1000"};
    private String[] EX = {"Numbers", "1.05", "EX", "10"};
    private String[] PX_EX = {"Numbers", "1.05", "PX", "1000", "EX", "10"};

    private String[] PX_REV_ORDER = {"Numbers", "1.05", "1000", "PX"};
    private String[] EX_REV_ORDER = {"Numbers", "1.05", "10", "EX"};

    private String[] PX_NO_ARG = {"Numbers", "1.05", "PX"};
    private String[] EX_NO_ARG = {"Numbers", "1.05", "EX"};

    private String[] PX_STRING_ARG = {"Numbers", "1.05", "PX", "AVC"};
    private String[] EX_STRING_ARG = {"Numbers", "1.05", "EX", "HIJ"};

    private String[] PX_NX = {"Numbers", "1.05", "PX", "1000", "NX"};
    private String[] EX_XX = {"Numbers", "1.05", "XX", "EX", "10"};

    private String[] PX_XX_INVALID = {"Numbers", "1.05", "PX", "XX", "1000"};
    private String[] EX_NX_INVALID = {"Numbers", "1.05", "NX", "10", "EX"};
    
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
