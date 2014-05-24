package jredis.command;

import jredis.data.Response;
import jredis.data.ResponseBit;
import jredis.domain.BinaryString;
import jredis.exception.InvalidCommand;

/**
 * Getbit command implementation.
 * 
 * @author anoopelias
 * 
 */
public class GetbitCommand implements Command<Boolean> {

    private BinaryString key;
    private int offset;

    /**
     * Construct get bit command using its args.
     * 
     * @param args
     * @throws InvalidCommand
     */
    public GetbitCommand(BinaryString[] args) throws InvalidCommand {
        if (args.length != 2)
            throw new InvalidCommand("Invalid Number of args");

        key = args[0];

        try {
            offset = Integer.parseInt(args[1].toString());

            if (offset < 0)
                throw new InvalidCommand("Offset below range");

        } catch (NumberFormatException e) {
            throw new InvalidCommand("Unparsable offset");
        }
    }

    @Override
    public Response<Boolean> execute() throws InvalidCommand {
        BinaryString byteString = BitHelper.get(key);
        
        return new ResponseBit((byteString == null) ? false
                : byteString.getBit(offset));
    }

}
