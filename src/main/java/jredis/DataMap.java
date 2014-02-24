package jredis;

import java.util.HashMap;
import java.util.Map;

/**
 * Data map to store data.
 * 
 * @author anoopelias
 *
 */
public class DataMap {

    public static DataMap INSTANCE = new DataMap();

    private DataMap() {
    }

    private Map<String, StringValue> data = new HashMap<String, StringValue>();

    /**
     * Put data in to the key value store.
     * 
     * This method is synchronized. It is better synchronize this method than to
     * use a synchronized HashMap because this will allow external commands to join the
     * synchronization.
     * 
     * @param key
     * @param value
     */
    public synchronized void put(String key, StringValue value) {
        data.put(key, value);
    }

    /**
     * Get value from the key store.
     * 
     * This method is synchronized.
     * 
     * @param key
     * @return
     */
    public synchronized StringValue get(String key) {
        StringValue value = data.get(key);
        
        if(value == null)
            return null;
        
        if(!value.isValid()) {
            data.remove(key);
            return null;
        }
        
        return value;
    }
    
    
    /**
     * This method clears all the data in the data store.
     * 
     */
    public synchronized void clear() {
        data = new HashMap<String, StringValue>();
    }

}
