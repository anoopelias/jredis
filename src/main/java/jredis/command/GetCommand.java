package jredis.command;

import jredis.Logger;
import jredis.Protocol;
import jredis.Timer;
import jredis.data.Response;
import jredis.data.ResponseString;
import jredis.domain.BinaryString;
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
        if (args.length != 1)
            throw new InvalidCommand("Invalid number of arguments");

        key = args[0];
    }

    public GetCommand(BinaryString[] args) throws InvalidCommand {
        this(Protocol.toStringArray(args));
    }

    @Override
    public Response<String> execute() throws InvalidCommand {
        
        BinaryString value = BitHelper.get(key);

        if (value == null)
            return new ResponseString();
        
        Response<String> resp = new ResponseString(value); 
        
        return resp;
    }

}
