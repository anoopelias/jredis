package jredis.command;

import jredis.Protocol;
import jredis.domain.BinaryString;
import jredis.exception.InvalidCommand;

import org.junit.Test;

public class ZaddCommandTest {
    
    private BinaryString[] NO_ARGS = {};
    private BinaryString[] ONE_ARG = Protocol.toBinaryStrings(new String[]{"Numbers"});
    private BinaryString[] TWO_ARGS = Protocol.toBinaryStrings(new String[]{"Numbers", "1.05"});
    private BinaryString[] FOUR_ARGS = Protocol.toBinaryStrings(new String[]{"Numbers", "1.05", "One", "Two"});
    private BinaryString[] INTEGER_SCORE = Protocol.toBinaryStrings(new String[]{"Numbers", "1", "One"});
    private BinaryString[] NEGATIVE_SCORE = Protocol.toBinaryStrings(new String[]{"Numbers", "-1.5", "One"});

    private BinaryString[] STRING_SCORE = Protocol.toBinaryStrings(new String[]{"Numbers", "1K", "One"});


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
