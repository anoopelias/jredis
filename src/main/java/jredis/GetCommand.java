package jredis;

import jredis.exception.InvalidCommand;

public class GetCommand implements Command {
    
    private String[] args;
    
    public GetCommand(String[] args) {
        this.args = args;
    }

    @Override
    public String execute() throws InvalidCommand {
        if(args.length != 1)
            throw new InvalidCommand("Invalid number of arguments");
        
        return DataMap.INSTANCE.get(args[0]);
    }

}
