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
        else if("ZADD".equals(name))
            c = new ZaddCommand(args);
        else if("ZCARD".equals(name))
            c = new ZcardCommand(args);
        else if("ZCOUNT".equals(name))
            c = new ZcountCommand(args);
        else if("ZRANGE".equals(name))
            c = new ZrangeCommand(args);
        
        if(Server.isDebug()) {
            print(c, name, args);
        }
        
        return c;

    }

    private void print(Command<?> c, String name, String[] args) {
        System.out.print("Command : " + c + " " + name + " ");
        for(String arg : args) {
            System.out.print(arg + " ");
        }
        System.out.println();
    }

}
