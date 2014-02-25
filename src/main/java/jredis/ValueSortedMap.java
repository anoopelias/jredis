package jredis;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import com.google.common.base.Functions;
import com.google.common.collect.Ordering;

/**
 * Copied from StackOverflow,
 * 
 * http://stackoverflow.com/questions/109383/how-to-sort-a-mapkey-value-on-the-
 * values-in-java/3420912#3420912
 * 
 * 
 * @author anoopelias
 * 
 * @param <K>
 * @param <V>
 */
public class ValueSortedMap extends
        TreeMap<String, Double> {

    private static final long serialVersionUID = 7477985130020172347L;

    private final Map<String, Double> valueMap;

    public ValueSortedMap() {
        this(new HashMap<String, Double>());
    }

    private ValueSortedMap(HashMap<String, Double> valueMap) {
        super(Ordering.natural().onResultOf(Functions.forMap(valueMap))
                .compound(Ordering.natural()));
        this.valueMap = valueMap;
    }

    @Override
    public Double put(String k, Double v) {
        
        Double ret = null;
        if (valueMap.containsKey(k)) {
            ret = remove(k);
        }
        valueMap.put(k, v);
        super.put(k, v);
        
        return ret;
    }
}
