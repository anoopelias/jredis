package jredis;

import jredis.exception.InvalidCommand;

/**
 * Get command implementation.
 * 
 * @author anoopelias
 *
 */
public class GetCommand implements Command {
    
    private String[] args;
    
    /**
     * Construct get command with args.
     * 
     * @param args
     */
    public GetCommand(String[] args) {
        this.args = args;
    }

    @Override
    public String execute() throws InvalidCommand {
        if(args.length != 1)
            throw new InvalidCommand("Invalid number of arguments");
        
        StringValue value = DataMap.INSTANCE.get(args[0]);
        return (value == null) ? null : value.value();
    }

}
