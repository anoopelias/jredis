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

    public GetCommand(BinaryString[] args) throws InvalidCommand {
        this(Protocol.toStringArray(args));
    }

    @Override
    public Response<String> execute() throws InvalidCommand {
        
        synchronized(DB.INSTANCE) {
            BinaryString value = ByteHelper.get(key);
            
            if(value == null)
                return new ResponseString();
            
            return new ResponseString(value.toString());
        }
        
    }

}
