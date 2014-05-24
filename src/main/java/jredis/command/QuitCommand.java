package jredis.command;

import jredis.data.Response;
import jredis.data.ResponseOk;
import jredis.domain.BinaryString;
import jredis.exception.InvalidCommand;

/**
 * Handle quit operation.
 * 
 * @author anoopelias
 *
 */
public class QuitCommand implements Command<String> {
    
    /**
     * Constructor with no args expected.
     * 
     * @param args
     * @throws InvalidCommand
     */
    public QuitCommand(BinaryString[] args) throws InvalidCommand {
        if(args != null && args.length > 0)
            throw new InvalidCommand("Invalid args");
    }

    @Override
    public Response<String> execute() throws InvalidCommand {
        return new ResponseOk();
    }

}
