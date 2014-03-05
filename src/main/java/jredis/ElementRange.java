package jredis;

import java.util.Iterator;
import java.util.Set;

/**
 * Data structure to hold the response to a range request.
 * 
 * @author anoopelias
 * 
 */
public class ElementRange {

    private boolean scored;
    private Set<Element> elements;

    /**
     * Construct and element range response.
     * 
     * @param elements
     * @param hasScores
     */
    public ElementRange(Set<Element> elements, boolean scored) {
        this.elements = elements;
        this.scored = scored;
    }

    /**
     * Check if the data has scores in it.
     * 
     * @return
     */
    public boolean isScored() {
        return scored;
    }

    public Set<Element> getElements() {
        return elements;
    }
    
    

}
