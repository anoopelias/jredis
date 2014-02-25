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
        
        synchronized(DataMap.INSTANCE) {
            
            StringValue value = DataMap.INSTANCE.get(key, StringValue.class);
            
            if(value == null)
                return new ResponseString(null);
            
            if(!value.isValid()) {
                DataMap.INSTANCE.remove(key);
                return new ResponseString(null);
            }
            return new ResponseString(value.value());
        }
        
    }

}
