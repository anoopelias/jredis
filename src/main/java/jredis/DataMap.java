package jredis;

import java.util.HashMap;
import java.util.Map;

public class DataMap {

    public static DataMap INSTANCE = new DataMap();

    private DataMap() {
    }

    private Map<String, String> data = new HashMap<String, String>();

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
    public synchronized void put(String key, String value) {
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
    public synchronized String get(String key) {
        return data.get(key);
    }
    
    
    /**
     * This method clears all the data in the data store.
     * 
     */
    public synchronized void clear() {
        data = new HashMap<String, String>();
    }

}
