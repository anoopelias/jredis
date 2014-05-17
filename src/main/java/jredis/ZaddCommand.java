package jredis;

import jredis.exception.InvalidCommand;

public class ZaddCommand implements Command<Integer> {

    private String key;
    private double score;
    private String value;

    public ZaddCommand(String[] args) throws InvalidCommand {
        if (args.length != 3)
            throw new InvalidCommand("Invalid arg length");
        
        try {
            key = args[0];
            score = Protocol.parseDouble(args[1]);
            value = args[2];
        } catch (NumberFormatException e) {
            throw new InvalidCommand("Unparsable score");
        }
    }

    public ZaddCommand(BinaryString[] args) throws InvalidCommand {
        this(Protocol.toStringArray(args));
    }

    @Override
    public Response<Integer> execute() throws InvalidCommand {

        boolean inserted;

        synchronized (DB.INSTANCE) {
            ElementSet map = ZsetHelper.get(key);
            if (map == null) {
                map = new TreeElementSet();
                DB.INSTANCE.put(key, map);
            }

            inserted = map.insert(new Element(value, score));
        }

        return new ResponseNumber(inserted ? 1 : 0);
    }

}
