package jredis.command;

import jredis.Protocol;
import jredis.domain.BinaryString;
import jredis.exception.InvalidCommand;

import org.junit.Test;

public class GetCommandTest {
    
    private BinaryString[] NO_ARGS = {};
    private BinaryString[] ONE_ARG = Protocol.toBinaryStrings(new String[]{"Numbers"});
    private BinaryString[] TWO_ARGS = Protocol.toBinaryStrings(new String[]{"Numbers", "1.05"});
    
    @Test(expected=InvalidCommand.class)
    public void test_no_args() throws InvalidCommand {
        new GetCommand(NO_ARGS);
    }

    @Test
    public void test_one_arg() throws InvalidCommand {
        Command<?> setCommand = new GetCommand(ONE_ARG);
        setCommand.execute();
    }

    @Test(expected=InvalidCommand.class)
    public void test_two_args() throws InvalidCommand {
        new GetCommand(TWO_ARGS);
    }



}
