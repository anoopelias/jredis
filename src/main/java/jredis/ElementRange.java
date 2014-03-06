package jredis;

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
     * Construct and element range.
     * 
     * @param elements
     * @param hasScores
     */
    public ElementRange(Set<Element> elements) {
        this.elements = elements;
    }

    /**
     * Construct and element range if it is scored.
     * 
     * @param elements
     * @param hasScores
     */
    public ElementRange(Set<Element> elements, boolean scored) {
        this(elements);
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

    /**
     * Get the element in the range in order.
     * 
     * @return
     */
    public Set<Element> getElements() {
        return elements;
    }
    
    

}
