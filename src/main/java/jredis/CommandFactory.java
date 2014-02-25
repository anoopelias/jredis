package jredis;

import jredis.exception.InvalidCommand;

public class CommandFactory {
    
    public static CommandFactory INSTANCE = new CommandFactory();
    
    public Command<?> createCommand(String name, String[] args) throws InvalidCommand {
        
        Command<?> c = null;
        
        if("GET".equals(name))
            c = new GetCommand(args);
        else if("SET".equals(name))
            c = new SetCommand(args);
        else if("GETBIT".equals(name))
            c = new GetbitCommand(args);
        else if("SETBIT".equals(name))
            c = new SetbitCommand(args);
        return c;

    }

}
