package jredis;

import jredis.exception.InvalidCommand;

import org.junit.Test;

public class ZaddCommandTest {
    
    private String[] NO_ARGS = {};
    private String[] ONE_ARG = {"Numbers"};
    private String[] TWO_ARGS = {"Numbers", "1.05"};
    private String[] FOUR_ARGS = {"Numbers", "1.05", "One", "Two"};
    private String[] INTEGER_SCORE = {"Numbers", "1", "One"};
    private String[] NEGATIVE_SCORE = {"Numbers", "-1.5", "One"};

    private String[] STRING_SCORE = {"Numbers", "1K", "One"};


    @Test(expected=InvalidCommand.class)
    public void test_no_args() throws InvalidCommand {
        new ZaddCommand(NO_ARGS);
    }

    @Test(expected=InvalidCommand.class)
    public void test_one_arg() throws InvalidCommand {
        new ZaddCommand(ONE_ARG);
    }

    @Test(expected=InvalidCommand.class)
    public void test_two_args() throws InvalidCommand {
        new ZaddCommand(TWO_ARGS);
    }

    @Test(expected=InvalidCommand.class)
    public void test_four_args() throws InvalidCommand {
        new ZaddCommand(FOUR_ARGS);
    }

    @Test(expected=InvalidCommand.class)
    public void test_string_score() throws InvalidCommand {
        new ZaddCommand(STRING_SCORE);
    }

    @Test
    public void test_integer_score() throws InvalidCommand {
        new ZaddCommand(INTEGER_SCORE);
    }

    @Test
    public void test_negative_score() throws InvalidCommand {
        new ZaddCommand(NEGATIVE_SCORE);
    }

}
