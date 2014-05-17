package jredis.command;

import jredis.command.ZcardCommand;
import jredis.exception.InvalidCommand;

import org.junit.Test;

public class ZcardCommandTest {

    private String[] NO_ARGS = {};
    private String[] ONE_ARG = { "Number" };
    private String[] TWO_ARGS = { "Number", "1.05"};

    @Test(expected = InvalidCommand.class)
    public void test_no_args() throws InvalidCommand {
        new ZcardCommand(NO_ARGS);
    }

    @Test
    public void test_one_arg() throws InvalidCommand {
        new ZcardCommand(ONE_ARG).execute();
    }

    @Test(expected = InvalidCommand.class)
    public void test_two_args() throws InvalidCommand {
        new ZcardCommand(TWO_ARGS);
    }

}
