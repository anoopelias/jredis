package jredis;

import jredis.exception.InvalidCommand;

public class CommandFactory {
    
    public static CommandFactory INSTANCE = new CommandFactory();
    
    public Command createCommand(String name, String[] args) throws InvalidCommand {
        
        Command c = null;
        if("GET".equals(name))
            c = new GetCommand(args);
        else if("SET".equals(name))
            c = new SetCommand(args);
        return c;

    }

}
