package jredis.command;

import jredis.DB;
import jredis.domain.BinaryString;
import jredis.domain.ElementSet;
import jredis.exception.InvalidCommand;

/**
 * Helper class for zset based commands.
 * 
 * @author anoopelias
 *
 */
public class ZsetHelper {

    public static ElementSet get(BinaryString key) throws InvalidCommand {
        try {
            return DB.INSTANCE.get(key, ElementSet.class);
        } catch (ClassCastException e) {
            throw new InvalidCommand("Key already set as another type");
        }
    }

}
