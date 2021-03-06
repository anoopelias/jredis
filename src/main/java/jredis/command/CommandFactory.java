package jredis.command;

import jredis.Logger;
import jredis.domain.BinaryString;
import jredis.exception.InvalidCommand;

/**
 * A simple factory to instantiate commands.
 * 
 * @author anoopelias
 * 
 */
public class CommandFactory {

    public static CommandFactory INSTANCE = new CommandFactory();

    /**
     * Creates a command based on input.
     * 
     * @param name
     * @param args
     * @return
     * @throws InvalidCommand
     */
    public Command<?> createCommand(String name, BinaryString[] args)
            throws InvalidCommand {

        Command<?> c = null;

        if ("GET".equals(name))
            c = new GetCommand(args);
        else if ("SET".equals(name))
            c = new SetCommand(args);
        else if ("GETBIT".equals(name))
            c = new GetbitCommand(args);
        else if ("SETBIT".equals(name))
            c = new SetbitCommand(args);
        else if ("ZADD".equals(name))
            c = new ZaddCommand(args);
        else if ("ZCARD".equals(name))
            c = new ZcardCommand(args);
        else if ("ZCOUNT".equals(name))
            c = new ZcountCommand(args);
        else if ("ZRANGE".equals(name))
            c = new ZrangeCommand(args);
        else if ("QUIT".equals(name))
            c = new QuitCommand(args);
        else if ("SAVE".equals(name))
            c = new SaveCommand(args);
        else
            throw new InvalidCommand("Cannot find command type");

        if(Logger.isDebug())
            print(c, name, args);

        return c;

    }

    private void print(Command<?> c, String name, BinaryString[] args) {
        StringBuilder sb = new StringBuilder("Command : " + c + " " + name + " ");
        if (args != null) {
            for (BinaryString arg : args) {
                sb.append(arg + " ");
            }
        }
        Logger.debug(sb.toString());
    }

}
