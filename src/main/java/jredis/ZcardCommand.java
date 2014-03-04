package jredis;

import jredis.exception.InvalidCommand;

public class ZcardCommand implements Command<Integer> {

    private String key;

    public ZcardCommand(String[] args) throws InvalidCommand {
        if (args.length != 1)
            throw new InvalidCommand("Invalid set of args");

        key = args[0];
    }

    @Override
    public Response<Integer> execute() throws InvalidCommand {
        synchronized (DataMap.INSTANCE) {
            ElementSet map = DataMap.INSTANCE.get(key, ElementSet.class);
            return new ResponseNumber((map == null) ? 0 : map.size());
        }
    }

}
