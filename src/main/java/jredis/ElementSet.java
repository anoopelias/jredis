package jredis;

import java.util.Set;

public interface ElementSet extends Iterable<Element> {
    
    /**
     * Insert an element in to the set.
     * 
     * @param element
     * @return true if inserted, false if updated
     */
    public boolean insert(Element element);
    
    /**
     * Get the number of elements.
     * 
     * @return number of elements
     */
    public int size();
    
    /**
     * Sublist of elements at the given range of scores, both inclusive.
     * 
     * @param from
     * @param to
     * @return
     */
    public Set<Element> subListByScore(Double from, Double to);

    /**
     * Sublist of elements at the given range of rank, both inclusive.
     * 
     * @param from
     * @param to
     * @return
     */
    public Set<Element> subListByRank(Double from, Double to);

}
