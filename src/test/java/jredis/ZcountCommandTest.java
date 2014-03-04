package jredis;

import jredis.exception.InvalidCommand;

import org.junit.Test;

public class ZcountCommandTest {

    private String[] NO_ARGS = {};
    private String[] ONE_ARG = {"Numbers"};
    private String[] TWO_ARGS = {"Numbers", "1.05"};
    private String[] THREE_ARGS = {"Numbers", "1.05", "5.05"};

    private String[] STRING_FROM = {"Numbers", "HIJK", "5.05"};
    private String[] STRING_TO = {"Numbers", "1.05F", "Bazingaa!"};

    @Test(expected = InvalidCommand.class)
    public void test_no_args() throws InvalidCommand {
        new ZcountCommand(NO_ARGS);
    }

    @Test(expected = InvalidCommand.class)
    public void test_one_arg() throws InvalidCommand {
        new ZcountCommand(ONE_ARG);
    }

    @Test(expected = InvalidCommand.class)
    public void test_two_args() throws InvalidCommand {
        new ZcountCommand(TWO_ARGS);
    }

    @Test
    public void test_three_args() throws InvalidCommand {
        new ZcountCommand(THREE_ARGS);
    }

    @Test(expected = InvalidCommand.class)
    public void test_string_from() throws InvalidCommand {
        new ZcountCommand(STRING_FROM);
    }

    @Test(expected = InvalidCommand.class)
    public void test_string_to() throws InvalidCommand {
        new ZcountCommand(STRING_TO);
    }

}
