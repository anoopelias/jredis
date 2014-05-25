package jredis.domain;

import java.util.Set;


/**
 * An element set interface for sorted set of elements.
 * 
 * @author anoopelias
 *
 */
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
     * Subset count of elements at the given range of scores, both inclusive.
     * 
     * @param from
     * @param to
     * @return
     */
    public int subsetSizeByScore(Double from, Double to);
    
    /**
     * Subset elements at the given range of scores, both inclusive.
     * 
     * @param from
     * @param to
     * @return
     */
    public Iterable<Element> subsetByScore(Double from, Double to);

    /**
     * Subset of elements at the given range of rank, both inclusive.
     * 
     * @param from
     * @param to
     * @return
     */
    public Set<Element> subsetByRank(int from, int to);

}
