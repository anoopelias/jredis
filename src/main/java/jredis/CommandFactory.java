package jredis;

public class CommandFactory {
    
    public static CommandFactory INSTANCE = new CommandFactory();
    
    public Command createCommand(String name, String[] args) {
        
        Command c = null;
        if("GET".equals(name))
            c = new GetCommand(args);
        else if("SET".equals(name))
            c = new SetCommand(args);
        return c;

    }

}
