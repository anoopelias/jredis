package jredis;

import jredis.exception.InvalidCommand;

/**
 * Setbit command implementation.
 * 
 * @author anoopelias
 * 
 */
public class SetbitCommand implements Command<Boolean> {

    private String key;
    private int offset;
    private boolean value;

    /**
     * Construct Setbit Command from arguments.
     * 
     * @param args
     * @throws InvalidCommand
     */
    public SetbitCommand(String[] args) throws InvalidCommand {

        if (args.length != 3)
            throw new InvalidCommand("Invalid number of arguments");

        try {
            key = args[0];
            offset = Integer.parseInt(args[1]);
            int val = Integer.parseInt(args[2]);

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
        synchronized (DataMap.INSTANCE) {
            BitString bitString = BitHelper.get(key);
            if (bitString == null) {
                bitString = new BitString();
                DataMap.INSTANCE.put(key, bitString);
            }

            /*
             * TODO: Probably we can move the set method outside synchronization
             * block if we synchronize BitString instead.
             */
            return new ResponseBit(bitString.set(offset, value));
        }
    }

}
