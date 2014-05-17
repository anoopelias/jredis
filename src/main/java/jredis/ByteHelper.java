package jredis;

import jredis.domain.BinaryString;
import jredis.domain.TimedBinaryString;
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
    public static BinaryString get(String key) throws InvalidCommand {
        try {
            TimedBinaryString value = DB.INSTANCE.get(key,
                    TimedBinaryString.class);
            
            if(value != null && !value.isValid()) {
                DB.INSTANCE.remove(key);
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
    public static BinaryString getOrCreate(String key) throws InvalidCommand {
        BinaryString byteString = get(key);
        
        if (byteString == null) {
            byteString = new BinaryString();
            TimedBinaryString timedByteString = new TimedBinaryString(byteString);
            DB.INSTANCE.put(key, timedByteString);
        }
        
        return byteString;
    }

}
