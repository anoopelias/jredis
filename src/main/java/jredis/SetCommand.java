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

    /**
     * Read all the options from the input.
     * 
     * @param options
     * @throws InvalidCommand
     */
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

    /**
     * Extract expiry value from inputs
     * 
     * @param args
     * @param i
     * @param factor
     * @return
     * @throws InvalidCommand
     */
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
        TimedByteString byteString = createValue();

        synchronized (DB.INSTANCE) {

            if (isNx && hasKey(key))
                return new ResponseString();

            if (isXx && !hasKey(key))
                return new ResponseString();

            DB.INSTANCE.put(key, byteString);
        }
        return new ResponseOk();
    }

    /**
     * Create a Timed value that needs to be put in to DB.
     * 
     * @return
     */
    private TimedByteString createValue() {

        TimedByteString stringValue = null;
        BinaryString byteString = new BinaryString(value);
        if (expiry != null)
            stringValue = new TimedByteString(byteString, expiry + System.currentTimeMillis());
        else
            stringValue = new TimedByteString(byteString);

        return stringValue;
    }

    /**
     * Check if the key already exist as some object.
     * 
     * @param key
     * @return
     */
    private boolean hasKey(String key) {
        return DB.INSTANCE.get(key, Object.class) != null;
    }

}
