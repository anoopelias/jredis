package jredis;

import jredis.exception.InvalidCommand;

/**
 * Get command implementation.
 * 
 * @author anoopelias
 *
 */
public class GetCommand implements Command<String> {
    
    private String key;
    
    /**
     * Construct get command with args.
     * 
     * @param args
     */
    public GetCommand(String[] args) throws InvalidCommand {
        if(args.length != 1)
            throw new InvalidCommand("Invalid number of arguments");
        
        key = args[0];
    }

    @Override
    public Response<String> execute() {
        StringValue value = DataMap.INSTANCE.get(key);
        return new ResponseString((value == null) ? null : value.value());
    }

}
