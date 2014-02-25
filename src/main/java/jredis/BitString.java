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
     */
    public void set(Integer offset, boolean value) {
        if(value)
            ones.add(offset);
        else
            ones.remove(offset);
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
