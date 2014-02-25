package jredis;

import static org.junit.Assert.assertNotNull;

import java.util.TreeMap;

import org.junit.Test;

public class ValueSortedMapTest {
    
    @Test
    public void test() {
        ValueSortedMap map = new ValueSortedMap();
        map.put("ABC", 1.3);
        assertNotNull(map.put("ABC", 1.5));
        
        TreeMap<String, Double> tmap = new TreeMap<>();
        tmap.put("ABC", 1.3);
        assertNotNull(tmap.put("ABC", 1.6));
    }

}
