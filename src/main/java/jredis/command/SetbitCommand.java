package jredis.command;

import jredis.DB;
import jredis.data.Response;
import jredis.data.ResponseBit;
import jredis.domain.BinaryString;
import jredis.exception.InvalidCommand;

/**
 * Setbit command implementation.
 * 
 * @author anoopelias
 * 
 */
public class SetbitCommand implements Command<Boolean> {

    private BinaryString key;
    private int offset;
    private boolean value;

    public SetbitCommand(BinaryString[] args) throws InvalidCommand {
        if (args.length != 3)
            throw new InvalidCommand("Invalid number of arguments");

        try {
            key = args[0];
            offset = Integer.parseInt(args[1].toString());
            int val = Integer.parseInt(args[2].toString());

            if (val < 0 || val > 1)
                throw new InvalidCommand("Value is not a bit");

            if (offset < 0)
                throw new InvalidCommand("Offset below range");

            if (val == 1)
                value = true;

        } catch (NumberFormatException e) {
            throw new InvalidCommand("Unparsable offset / value");

        }
    }

    @Override
    public Response<Boolean> execute() throws InvalidCommand {
        synchronized (DB.INSTANCE) {
            BinaryString byteString = BitHelper.getOrCreate(key);

            /*
             * TODO: Probably we can move the set method outside synchronization
             * block if we synchronize ByteString instead.
             */
            return new ResponseBit(byteString.setBit(offset, value));
        }
    }

}
