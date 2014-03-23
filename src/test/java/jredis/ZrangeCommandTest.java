package jredis;

import jredis.exception.InvalidCommand;

import org.junit.Before;
import org.junit.Test;

public class ZrangeCommandTest {
    
    private static final String[] NO_ARGS = {};
    private static final String[] ONE_ARG = {"Numbers"};
    private static final String[] TWO_ARGS = {"Numbers", "1"};
    private static final String[] THREE_ARGS = {"Numbers", "4", "9"};

    private static final String[] FOUR_ARGS = {"Numbers", "4", "9", "WITHSCORES"};
    private static final String[] LOWER_CASE_WITHSCORES = {"Numbers", "4", "9", "withscores"};
    private static final String[] INVALID_WITHSCORES = {"Numbers", "4", "9", "with scores"};

    private static final String[] STRING_START = {"Numbers", "KLM", "9"};
    private static final String[] FLOAT_START = {"Numbers", "4.05", "9"};

    private static final String[] STRING_STOP = {"Numbers", "2", "QWE"};
    private static final String[] FLOAT_STOP = {"Numbers", "4", "6.49"};
    
    @Before
    public void setup() {
        DataMap.INSTANCE.clear();
    }

    @Test(expected = InvalidCommand.class)
    public void test_no_args() throws InvalidCommand {
        new ZrangeCommand(NO_ARGS);
    }

    @Test(expected = InvalidCommand.class)
    public void test_one_arg() throws InvalidCommand {
        new ZrangeCommand(ONE_ARG);
    }

    @Test(expected = InvalidCommand.class)
    public void test_two_args() throws InvalidCommand {
        new ZrangeCommand(TWO_ARGS);
    }

    @Test
    public void test_three_args() throws InvalidCommand {
        new ZrangeCommand(THREE_ARGS).execute();
    }

    @Test
    public void test_four_args() throws InvalidCommand {
        new ZrangeCommand(FOUR_ARGS).execute();
    }

    @Test
    public void test_lower_withscores() throws InvalidCommand {
        new ZrangeCommand(LOWER_CASE_WITHSCORES).execute();
    }

    @Test(expected = InvalidCommand.class)
    public void test_string_start() throws InvalidCommand {
        new ZrangeCommand(STRING_START);
    }

    @Test(expected = InvalidCommand.class)
    public void test_float_start() throws InvalidCommand {
        new ZrangeCommand(FLOAT_START);
    }

    @Test(expected = InvalidCommand.class)
    public void test_string_stop() throws InvalidCommand {
        new ZrangeCommand(STRING_STOP);
    }

    @Test(expected = InvalidCommand.class)
    public void test_float_stop() throws InvalidCommand {
        new ZrangeCommand(FLOAT_STOP);
    }

    @Test(expected = InvalidCommand.class)
    public void test_invalid_withscores() throws InvalidCommand {
        new ZrangeCommand(INVALID_WITHSCORES);
    }

}
