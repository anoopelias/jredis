package jredis;

import java.util.HashSet;
import java.util.Set;

/**
 * A string of bits storage.
 * 
 * @author anoopelias
 *
 */
public class BitString {
    
    private Set<Integer> ones = new HashSet<>();
    
    /**
     * Set a specific offset on the string.
     * 
     * @param offset
     * @param value
     * @return current value
     */
    public boolean set(Integer offset, boolean value) {
        boolean current;
        
        if(value)
            current = !ones.add(offset);
        else
            current = ones.remove(offset);
        
        return current;
    }
    
    /**
     * Get the value of the bit string at a specific offset.
     * 
     * @param offset
     * @return
     */
    public boolean get(Integer offset) {
        
        if(ones.contains(offset))
            return true;
        
        return false;
    }
}
