package jredis.command;

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

    private BinaryString key;

    /**
     * Construct get command with args.
     * 
     * @param args
     */
    public GetCommand(BinaryString[] args) throws InvalidCommand {
        if (args.length != 1)
            throw new InvalidCommand("Invalid number of arguments");

        key = args[0];
    }

    @Override
    public Response<String> execute() throws InvalidCommand {
        
        BinaryString value = BitHelper.get(key.toString());

        if (value == null)
            return new ResponseString();
        
        Response<String> resp = new ResponseString(value); 
        
        return resp;
    }

}
