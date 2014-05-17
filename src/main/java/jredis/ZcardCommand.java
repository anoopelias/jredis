package jredis;

import jredis.exception.InvalidCommand;

public class ZcardCommand implements Command<Integer> {

    private String key;

    public ZcardCommand(String[] args) throws InvalidCommand {
        if (args.length != 1)
            throw new InvalidCommand("Invalid set of args");

        key = args[0];
    }

    public ZcardCommand(BinaryString[] args) throws InvalidCommand {
        this(Protocol.toStringArray(args));
    }

    @Override
    public Response<Integer> execute() throws InvalidCommand {
        synchronized (DB.INSTANCE) {
            ElementSet map = ZsetHelper.get(key);
            return new ResponseNumber((map == null) ? 0 : map.size());
        }
    }

}
