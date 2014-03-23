package jredis;

import jredis.exception.InvalidCommand;

/**
 * Helper class for bit operation.
 * 
 * @author anoopelias
 * 
 */
public class ByteHelper {

    public static ByteString get(String key) throws InvalidCommand {
        try {
            TimedByteString value = DataMap.INSTANCE.get(key,
                    TimedByteString.class);
            
            return (value == null)? null : value.value();
            
        } catch (ClassCastException e) {
            throw new InvalidCommand("Key already set as another type");
        }
    }

}
