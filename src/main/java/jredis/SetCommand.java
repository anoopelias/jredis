package jredis;

import jredis.exceptions.InvalidCommand;

public class SetCommand implements Command {
    
    private String[] args;
    
    public SetCommand(String[] args) {
        this.args = args;
    }

    @Override
    public String execute() throws InvalidCommand {
        if(args.length != 2)
            throw new InvalidCommand();
        DataMap.INSTANCE.put(args[0], args[1]);
        return "OK";
    }
}
