package jredis;

import jredis.exception.InvalidCommand;

/**
 * Set command to set a value. 
 * 
 * @author anoopelias
 *
 */
public class SetCommand implements Command {

    private String key;
    private String value;

    private boolean isNx;
    private boolean isXx;

    private Long expiry;

    /**
     * Constructor to create SetCommand with its args.
     * 
     * @param args
     * @throws InvalidCommand
     */
    public SetCommand(String[] args) throws InvalidCommand {
        if (args.length < 2)
            throw new InvalidCommand("Not enough args");

        key = args[0];
        value = args[1];

        for (int i = 2; i < args.length; i++) {

            if ("NX".equals(args[i]))
                isNx = true;
            else if ("XX".equals(args[i]))
                isXx = true;
            else if ("PX".equals(args[i])) {
                expiry = Long.parseLong(args[i + 1]);
                i++;
            } else
                throw new InvalidCommand("Unknown argument :" + args[i]);
        }

    }


    @Override
    public String execute() {
        StringValue stringValue = createValue();

        synchronized (DataMap.INSTANCE) {

            if (isNx && hasKey(key))
                return null;

            if (isXx && !hasKey(key))
                return null;

            DataMap.INSTANCE.put(key, stringValue);
            return "OK";
        }
    }

    private StringValue createValue() {

        StringValue stringValue = null;
        if (expiry != null)
            stringValue = new StringValue(value, expiry
                    + System.currentTimeMillis());
        else
            stringValue = new StringValue(value);

        return stringValue;
    }

    private boolean hasKey(String key) {
        return DataMap.INSTANCE.get(key) != null;
    }

}
