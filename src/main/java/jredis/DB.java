package jredis;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import jredis.domain.BinaryString;

/**
 * Data map to store data.
 * 
 * @author anoopelias
 *
 */
public class DB implements Iterable<BinaryString> {

    public static DB INSTANCE = new DB();

    private DB() {
    }

    private Map<BinaryString, Object> data = new HashMap<BinaryString, Object>();

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
    public synchronized <T> void put(BinaryString key, T value) {
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
    public synchronized <T> T get(BinaryString key, Class<T> type) {
        return type.cast(data.get(key));
    }
    

    /**
     * Remove key from the key store.
     * 
     * This method is synchronized.
     * 
     * @param key
     * @return
     */
    public synchronized void remove(BinaryString key) {
        data.remove(key);
    }

    /**
     * This method clears all the data in the data store.
     * 
     */
    public synchronized void clear() {
        data = new HashMap<BinaryString, Object>();
    }
    
    /**
     * Iterator to iterate through all the keys.
     * 
     */
    public synchronized Iterator<BinaryString> iterator() {
        return data.keySet().iterator();
    }

}
