package jredis;

import jredis.exception.InvalidCommand;

/**
 * Getbit command implementation.
 * 
 * @author anoopelias
 * 
 */
public class GetbitCommand implements Command<Boolean> {

    private String key;
    private int offset;

    /**
     * Construct get bit command using its args.
     * 
     * @param args
     * @throws InvalidCommand
     */
    public GetbitCommand(String[] args) throws InvalidCommand {

        if (args.length != 2)
            throw new InvalidCommand("Invalid Number of args");

        key = args[0];

        try {
            offset = Integer.parseInt(args[1]);

            if (offset < 0)
                throw new InvalidCommand("Offset below range");

        } catch (NumberFormatException e) {
            throw new InvalidCommand("Unparsable offset");
        }

    }

    @Override
    public Response<Boolean> execute() throws InvalidCommand {
        BitString bitString = DataMap.INSTANCE.get(key, BitString.class);
        
        return new ResponseBit((bitString == null) ? false
                : bitString.get(offset));
    }

}
