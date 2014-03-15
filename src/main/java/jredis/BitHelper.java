package jredis;

import jredis.exception.InvalidCommand;

/**
 * Helper class for bit operation.
 * 
 * @author anoopelias
 *
 */
public class BitHelper {

    public static BitString get(String key) throws InvalidCommand {
        try {
            return DataMap.INSTANCE.get(key, BitString.class);
        } catch (ClassCastException e) {
            throw new InvalidCommand("Key already set as another type");
        }
    }
    
}
