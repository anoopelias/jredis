package jredis;

import java.util.Iterator;

/**
 * Data structure to hold the response to a range request.
 * 
 * @author anoopelias
 * 
 */
public class ElementRange implements Iterable<Element> {

    private boolean scored;
    private Iterable<Element> elements;

    /**
     * Construct and element range response.
     * 
     * @param elements
     * @param hasScores
     */
    public ElementRange(Iterable<Element> elements, boolean scored) {
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

    @Override
    public Iterator<Element> iterator() {
        return elements.iterator();
    }
    
    

}
