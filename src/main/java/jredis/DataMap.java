package jredis;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DataMap {

    public static DataMap INSTANCE = new DataMap();

    private DataMap() {
    }

    /*
     * It seems ConcurrentHashMap gives better performance only if we are
     * iterating thru the keys. A synchornized Map gives better performance than
     * ConcurrentHashMap in this case as it avoids segmentation overhead.
     */
    private Map<String, String> data = Collections
            .synchronizedMap(new HashMap<String, String>());

    public void put(String key, String value) {
        data.put(key, value);
    }

    public String get(String key) {
        return data.get(key);
    }

}
