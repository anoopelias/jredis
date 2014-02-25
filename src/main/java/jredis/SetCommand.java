package jredis;

import java.util.Arrays;

import jredis.exception.InvalidCommand;

/**
 * Set command to set a value.
 * 
 * @author anoopelias
 * 
 */
public class SetCommand implements Command<String> {

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
        
        setOptions(Arrays.copyOfRange(args, 2, args.length));
    }

    private void setOptions(String[] options) throws InvalidCommand {
        for (int i = 0; i < options.length; i++) {

            if ("NX".equals(options[i]))
                isNx = true;
            
            else if ("XX".equals(options[i]))
                isXx = true;
            
            else if ("PX".equals(options[i]))
                i = setExpiry(options, i, 1);
            
            else if ("EX".equals(options[i]))
                i = setExpiry(options, i, 1000);
            
            else
                throw new InvalidCommand("Unknown option :" + options[i]);
        }
    }

    private int setExpiry(String[] args, int i, int factor)
            throws InvalidCommand {
        
        if (args.length < (i + 2))
            throw new InvalidCommand("Expiry time not available");
        
        try {
            expiry = Long.parseLong(args[i + 1]) * factor;
        } catch (NumberFormatException e) {
            throw new InvalidCommand("Invalid Expiry time");
        }
        return ++i;
    }

    @Override
    public Response<String> execute() {
        StringValue stringValue = createValue();

        synchronized (DataMap.INSTANCE) {

            if (isNx && hasKey(key))
                return null;

            if (isXx && !hasKey(key))
                return null;

            DataMap.INSTANCE.put(key, stringValue);
            return new ResponseString("OK");
        }
    }

    private StringValue createValue() {

        StringValue stringValue = null;
        if (expiry != null)
            stringValue = new StringValue(value, expiry);
        else
            stringValue = new StringValue(value);

        return stringValue;
    }

    private boolean hasKey(String key) {
        return DataMap.INSTANCE.get(key) != null;
    }

}
