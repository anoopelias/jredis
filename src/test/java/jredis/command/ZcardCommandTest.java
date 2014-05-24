package jredis.command;

import static jredis.TestUtil.h;
import jredis.domain.BinaryString;
import jredis.exception.InvalidCommand;

import org.junit.Test;

public class ZcardCommandTest {

    private BinaryString[] NO_ARGS = {};
    private BinaryString[] ONE_ARG = h(new String[]{ "Number" });
    private BinaryString[] TWO_ARGS = h(new String[]{ "Number", "1.05"});

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
