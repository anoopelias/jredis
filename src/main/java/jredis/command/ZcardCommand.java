package jredis.command;

import jredis.DB;
import jredis.data.Response;
import jredis.data.ResponseNumber;
import jredis.domain.BinaryString;
import jredis.domain.ElementSet;
import jredis.exception.InvalidCommand;

public class ZcardCommand implements Command<Integer> {

    private BinaryString key;

    public ZcardCommand(BinaryString[] args) throws InvalidCommand {
        if (args.length != 1)
            throw new InvalidCommand("Invalid set of args");

        key = args[0];
    }

    @Override
    public Response<Integer> execute() throws InvalidCommand {
        synchronized (DB.INSTANCE) {
            ElementSet map = ZsetHelper.get(key);
            return new ResponseNumber((map == null) ? 0 : map.size());
        }
    }

}
