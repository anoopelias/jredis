package jredis.command;

import jredis.command.Command;
import jredis.command.GetCommand;
import jredis.exception.InvalidCommand;

import org.junit.Test;

public class GetCommandTest {
    
    private String[] NO_ARGS = {};
    private String[] ONE_ARG = {"Numbers"};
    private String[] TWO_ARGS = {"Numbers", "1.05"};
    
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
