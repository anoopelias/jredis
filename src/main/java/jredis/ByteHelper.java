package jredis;

import jredis.exception.InvalidCommand;

/**
 * Helper class for bit operation.
 * 
 * @author anoopelias
 * 
 */
public class ByteHelper {

    /**
     * Get a specific key.
     * 
     * @param key
     * @return
     * @throws InvalidCommand if key already exists as another type.
     */
    public static ByteString get(String key) throws InvalidCommand {
        try {
            TimedByteString value = DataMap.INSTANCE.get(key,
                    TimedByteString.class);
            
            if(value != null && !value.isValid()) {
                DataMap.INSTANCE.remove(key);
                return null;
            }

            return (value == null) ? null : value.value();

        } catch (ClassCastException e) {
            throw new InvalidCommand("Key already set as another type");
        }
    }

    /**
     * Get a specific key, If it is null, create one and return.
     * 
     * @param key
     * @return
     * @throws InvalidCommand
     */
    public static ByteString getOrCreate(String key) throws InvalidCommand {
        ByteString byteString = get(key);
        
        if (byteString == null) {
            byteString = new ByteString();
            TimedByteString timedByteString = new TimedByteString(byteString);
            DataMap.INSTANCE.put(key, timedByteString);
        }
        
        return byteString;
    }

}
