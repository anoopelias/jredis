package jredis;

import java.util.HashSet;

/**
 * Response of type ElementRange
 * 
 * @author anoopelias
 *
 */
public class ResponseElementRange implements Response<ElementRange> {
    
    private ElementRange elementRange;
    
    /**
     * Null constructor.
     * 
     */
    public ResponseElementRange() {
    }

    /**
     * Construct based on an ElementRange.
     * 
     * @param elementRange
     */
    public ResponseElementRange(ElementRange elementRange) {
        this.elementRange = elementRange;
    }

    @Override
    public ElementRange value() {
        if(elementRange == null)
            return new ElementRange(new HashSet<Element>(), false);
        
        return elementRange;
    }

    @Override
    public String toProtocolString() {
        // TODO Auto-generated method stub
        return null;
    }

}
